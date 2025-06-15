package com.handicrafts.util;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.*;
import com.handicrafts.bean.ProductImageBean;

import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


// dl=0: Vào view mode
// dl=1: Cho phép download ảnh khi ấn vào link
// raw=1: Hiển thị ảnh dưới dạng raw (Có thế thêm vào src của img)
public class CloudStorageUtil {
    private static final ResourceBundle accessTokenBundle = ResourceBundle.getBundle("storage-token");

    public static ProductImageBean uploadOneImageToCloudStorage(Part part) throws IOException {
        ProductImageBean image = new ProductImageBean();
        // Kiểm tra xem part có phải là input field không
        if (!isFormField(part)) {
            // Nếu file có kiểu MIME là ảnh (bắt đầu dạng image) thì mới upload
            if (part.getContentType().startsWith("image")) {
                String imageName = part.getSubmittedFileName();
                InputStream imageInputStream = part.getInputStream();
                String imageBaseLink = uploadAndGetBaseLink(imageInputStream, imageName);
                String imageRawLink = convertToRawLink(imageBaseLink);
                if (imageRawLink != null) {
                    image = new ProductImageBean();
                    // Set nameInStorage cho link tại đây luôn
                    image.setNameInStorage(imageName);
                    image.setLink(imageRawLink);
                }
            }
        }
        return image;
    }

    public static List<ProductImageBean> uploadListImageToCloudStorage(List<Part> imageParts) throws IOException {
        List<ProductImageBean> imageList = new ArrayList<>();
        for (Part part : imageParts) {
            imageList.add(uploadOneImageToCloudStorage(part));
        }
        return imageList;
    }

    private static String convertToRawLink(String baseLink) {
        // Phòng trường hợp khi baseLink bị sai (Không chứa dl=0) thì trả về null để bắt lỗi
        // replace khi không tồn tại sẽ trả về link ban đầu
        if (baseLink.contains("dl=0")) {
            return baseLink.replace("dl=0", "raw=1");
        }
        return null;
    }

    private static String uploadAndGetBaseLink(InputStream inputStream, String fileName) {
        DbxRequestConfig config = DbxRequestConfig.newBuilder("handicrafts_Group33").build();
        DbxClientV2 client = new DbxClientV2(config, accessTokenBundle.getString("access-token"));

        try {
            // Upload file lên Dropbox
            FileMetadata metadata = client.files().uploadBuilder("/handicrafts_Repository/" + fileName)
                    .withMode(WriteMode.ADD)
                    .uploadAndFinish(inputStream);

            // Lấy link tới file đã upload
            return client.sharing().createSharedLinkWithSettings(metadata.getPathLower()).getUrl();
        } catch (DbxException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean delete(String imageNameContainExtension) {
        DbxRequestConfig config = DbxRequestConfig.newBuilder("handicrafts_Group33").build();
        DbxClientV2 client = new DbxClientV2(config, accessTokenBundle.getString("access-token"));
        try {
            String pathLinkToDelete = "/handicrafts_Repository/" + imageNameContainExtension;
            DeleteResult deleteResult = client.files().deleteV2(pathLinkToDelete);
            return deleteResult != null;
        } catch (DbxException e) {
            System.err.println("Error deleting file: " + e.getMessage());
        }
        return false;
    }

    private static boolean isFormField(Part part) {
        String contentType = part.getHeader("content-type");
        return contentType == null;
    }
}
