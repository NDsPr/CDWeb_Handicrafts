package com.handicrafts.controller.image;

import com.handicrafts.dto.ProductImageDTO;
import com.handicrafts.service.ImageService;
import com.handicrafts.util.BlankInputUtil;
import com.handicrafts.util.CloudStorageUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Part;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AllImageAddingController {

    @Autowired
    private ImageService imageService;

    @GetMapping("/all-image-adding")
    public String showAddImageForm() {
        return "all-image-adding";
    }

    @PostMapping("/all-image-adding")
    public String addImages(
            @RequestParam("name") String name,
            @RequestParam("productId") String productId,
            @RequestParam("files") List<MultipartFile> files,
            Model model) throws IOException {

        String success;
        String[] inputsForm = new String[]{name, productId};
        ArrayList<String> errors = new ArrayList<>();

        // Biến bắt lỗi
        boolean isValid = true;

        // Kiểm tra lỗi cho các trường input
        for (String string : inputsForm) {
            if (BlankInputUtil.isBlank(string)) {
                errors.add("e");
                if (isValid) {
                    isValid = false;
                }
            } else {
                errors.add(null);
            }
        }

        // Kiểm tra lỗi cho input file ảnh
        if (files == null || files.isEmpty() || files.get(0).isEmpty()) {
            errors.add("e");
            if (isValid) {
                isValid = false;
            }
        } else {
            errors.add(null);
        }
        model.addAttribute("errors", errors);

        // Nếu không lỗi gì trong việc validate thì tiếp tục
        if (isValid) {
            List<MultipartFile> imageFiles = filterImageFiles(files);

            for (MultipartFile file : imageFiles) {
                String nameInStorage = file.getOriginalFilename();
                ProductImageDTO image = CloudStorageUtil.uploadOneImageToCloudStorage((Part) file);
                image.setName(name);
                image.setNameInStorage(nameInStorage);
                image.setProductId(Integer.parseInt(productId));
                image.setCreatedDate(new Timestamp(System.currentTimeMillis()));
                image.setCreatedBy("");
                image.setModifiedDate(new Timestamp(System.currentTimeMillis()));
                image.setModifiedBy("");

                // Thêm ảnh vào database
                imageService.insertProductImage(image);
            }

            success = "s";
            model.addAttribute("success", success);
        }

        return "all-image-adding";
    }

    // Lọc ra các file là ảnh
    private List<MultipartFile> filterImageFiles(List<MultipartFile> files) {
        List<MultipartFile> imageFiles = new ArrayList<>();
        for (MultipartFile file : files) {
            if (!file.isEmpty() && file.getContentType() != null && file.getContentType().startsWith("image")) {
                imageFiles.add(file);
            }
        }
        return imageFiles;
    }
}
