package com.handicrafts.controller.admin.client_view;

import com.handicrafts.bean.CustomizeBean;
import com.handicrafts.dao.CustomizeDAO;
import com.handicrafts.util.BlankInputUtil;
import com.handicrafts.util.DetectTypeFileUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(value = {"/admin/client-home-management"})
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,
        maxFileSize = 1024 * 1024 * 10,
        maxRequestSize = 1024 * 1024 * 100
)

// TODO: Code JavaScript đếm số ký tự nhập vào ô (Nếu có thời gian)
public class ClientHomeManagementController extends HttpServlet {
    private final CustomizeDAO customizeDAO = new CustomizeDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        CustomizeBean customizeBean = customizeDAO.getCustomizeInfo();
        req.setAttribute("customizeBean", customizeBean);
        req.getRequestDispatcher("/client-home-management.jsp").forward(req, resp);
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String welcomeTitle = req.getParameter("welcomeTitle");
        String welcomeDes = req.getParameter("welcomeDes");
        String productTitle = req.getParameter("productTitle");
        String productDes = req.getParameter("productDes");
        String prTitle1 = req.getParameter("prTitle1");
        String prDes1 = req.getParameter("prDes1");
        String prIcon1 = req.getParameter("prIcon1");
        String prContentTitle1 = req.getParameter("prContentTitle1");
        String prContentDes1 = req.getParameter("prContentDes1");
        Part prLink1 = req.getPart("prLink1");
        String prLink1InStorage = req.getParameter("prLink1InStorage");
        String prTitle2 = req.getParameter("prTitle2");
        String prDes2 = req.getParameter("prDes2");
        String prContent2 = req.getParameter("prContent2");
        Part prLink2 = req.getPart("prLink2");
        String prLink2InStorage = req.getParameter("prLink2InStorage");
        Part background = req.getPart("background");
        String backgroundInStorage = req.getParameter("backgroundInStorage");
        String footerLeft = req.getParameter("footerLeft");
        String footerMiddle = req.getParameter("footerMiddle");
        String facebookLink = req.getParameter("facebookLink");
        String twitterLink = req.getParameter("twitterLink");
        String instagramLink = req.getParameter("instagramLink");
        String linkedinLink = req.getParameter("linkedinLink");

        // Mảng kiểm tra lỗi input
        String[] inputCheck = {welcomeTitle, welcomeDes, productTitle, productDes,
                prTitle1, prDes1, prIcon1, prContentTitle1, prContentDes1,
                prTitle2, prDes2, prContent2,
                footerLeft, footerMiddle};

        // Mảng kiểm tra lỗi image
        Part[] partCheck = {prLink1, prLink2, background};

        // Mảng giữ lỗi
        List<String> blankErrors = new ArrayList<>();
        List<String> imageErrors = new ArrayList<>();
        String notEqualError = null;

        // Biến bắt lỗi
        boolean isValid = true;

        // Kiểm tra lỗi để trống cho input
        for (String input : inputCheck) {
            if (BlankInputUtil.isBlank(input)) {
                if (isValid) {
                    isValid = false;
                }
                blankErrors.add("e");
            } else {
                blankErrors.add(null);
            }
        }

        // Kiểm tra lỗi không phải file ảnh
        for (Part part : partCheck) {
            // Nếu không tải lên file (part.getSize <= 0) thì không lỗi (có thể sửa ảnh hoặc là không)
            if (part.getSize() <= 0) {
                imageErrors.add(null);
            }
            // Nếu có Part, và Part Không là ảnh thì thêm lỗi
            else if (!DetectTypeFileUtil.isImage(part)) {
                if (isValid) {
                    isValid = false;
                }
                imageErrors.add("e");
            } else {
                imageErrors.add(null);
            }
        }

        // Kiểm tra lỗi khi 2 trong 3 (tiêu đề nội dung 1/icon1/nội dung 1) không bằng nhau về số lượng
        if (splitByComma(prIcon1).length != splitByTilde(prContentTitle1).length ||
                splitByComma(prIcon1).length != splitByTilde(prContentDes1).length ||
                splitByTilde(prContentTitle1).length != splitByTilde(prContentDes1).length) {
            if (isValid) {
                isValid = false;
            }
            notEqualError = "e";
        }

        CustomizeBean customizeBean = new CustomizeBean();
        // Nếu không có lỗi thì lưu vào database
        if (isValid) {
            String linkImage1 = null;
            String linkImage2 = null;
            String backgroundImage = null;
//            if (prLink1.getSize() > 0 && prLink1.getContentType().startsWith("image")) {
//                linkImage1 = CloudStorageUtil.uploadCustomizeImageToStorage(prLink1.getSubmittedFileName(), prLink1.getInputStream());
//            }
//            if (prLink2.getSize() > 0 && prLink1.getContentType().startsWith("image")) {
//                linkImage2 = CloudStorageUtil.uploadCustomizeImageToStorage(prLink2.getSubmittedFileName(), prLink2.getInputStream());
//            }
//            if (background.getSize() > 0 && prLink1.getContentType().startsWith("image")) {
//                backgroundImage = CloudStorageUtil.uploadCustomizeImageToStorage(background.getSubmittedFileName(), background.getInputStream());
//            }

            // Thực hiện xóa các khoảng trắng ở đầu và cuối chuỗi cho content và icon
            String clearSpaceIcon1 = clearSpaceHeaderAndFooter(prIcon1);
            String clearSpaceContentTitle1 = clearSpaceHeaderAndFooter(prContentTitle1);
            String clearSpaceContentDes1 = clearSpaceHeaderAndFooter(prContentDes1);
            String clearSpaceContent2 = clearSpaceHeaderAndFooter(prContent2);

            // Thêm màu chủ đạo của icon
            String colorIconList = colorIconList(clearSpaceIcon1);

            customizeBean.setWelcomeTitle(welcomeTitle.trim());
            customizeBean.setWelcomeDes(welcomeDes);
            customizeBean.setProductTitle(productTitle.trim());
            customizeBean.setProductDes(productDes.trim());
            customizeBean.setPrTitle1(prTitle1.trim());
            customizeBean.setPrDes1(prDes1.trim());
            customizeBean.setPrIcon1(colorIconList);
            customizeBean.setPrContentTitle1(clearSpaceContentTitle1);
            customizeBean.setPrContentDes1(clearSpaceContentDes1);
            if (linkImage1 != null) {
                // Xóa ảnh cũ trong Cloud Storage
//                CloudStorageUtil.deleteCustomize(prLink1InStorage);
                // Cập nhật ảnh mới
                customizeBean.setPrLink1(linkImage1);
                customizeBean.setPrLink1InStorage(prLink1.getSubmittedFileName());
            } else {
                // Set lại link cũ gửi từ request về
                customizeBean.setPrLink1(findOldImage1Link());
                customizeBean.setPrLink1InStorage(prLink1InStorage);
            }
            customizeBean.setPrTitle2(prTitle2.trim());
            customizeBean.setPrDes2(prDes2);
            customizeBean.setPrContent2(clearSpaceContent2);
            if (linkImage2 != null) {
                // Xóa ảnh cũ trong Cloud Storage
//                CloudStorageUtil.deleteCustomize(prLink2InStorage);
                // Cập nhật ảnh mới
                customizeBean.setPrLink2(linkImage2);
                customizeBean.setPrLink2InStorage(prLink2.getSubmittedFileName());
            } else {
                customizeBean.setPrLink2(findOldImage2Link());
                customizeBean.setPrLink2InStorage(prLink2InStorage);
            }
            if (backgroundImage != null) {
                // Xóa ảnh cũ trong Cloud Storage
//                CloudStorageUtil.deleteCustomize(backgroundInStorage);
                // Cập nhật ảnh mới
                customizeBean.setBackground(backgroundImage);
                customizeBean.setBackgroundInStorage(background.getSubmittedFileName());
            } else {
                customizeBean.setBackground(findOldBackgroundLink());
                customizeBean.setBackgroundInStorage(backgroundInStorage);
            }
            customizeBean.setFooterLeft(footerLeft);
            customizeBean.setFooterMiddle(footerMiddle);

            // Link thì nên bỏ khoảng trống ở đầu và cuối
            customizeBean.setFacebookLink(facebookLink.trim());
            customizeBean.setTwitterLink(twitterLink.trim());
            customizeBean.setInstagramLink(instagramLink.trim());
            customizeBean.setLinkedinLink(linkedinLink.trim());

            int affectRows = customizeDAO.updateCustomize(customizeBean);
            if (affectRows == -1) {
                String serverError = "e";
                req.setAttribute("customizeBean", customizeBean);
                req.setAttribute("serverError", serverError);
                req.getRequestDispatcher("/client-home-management.jsp").forward(req, resp);
            } else if (affectRows >= 0) {
                String update = "success";
                resp.sendRedirect(req.getContextPath() + "/admin/client-home-management?update=" + update);
            }
        } else {
            customizeBean = customizeDAO.getCustomizeInfo();
            // Có lỗi thì gửi danh sách các ô bị lỗi lên
            req.setAttribute("blankErrors", blankErrors);
            req.setAttribute("imageErrors", imageErrors);
            req.setAttribute("notEqualError", notEqualError);
            req.setAttribute("customizeBean", customizeDAO.getCustomizeInfo());
            req.getRequestDispatcher("/client-home-management.jsp").forward(req, resp);
        }
    }

    private String clearSpaceHeaderAndFooter(String input) {
        return input.trim();
    }

    private String findOldImage1Link() {
        return customizeDAO.findOldImage1Link();
    }

    private String findOldImage2Link() {
        return customizeDAO.findOldImage2Link();
    }

    private String findOldBackgroundLink() {
        return customizeDAO.findOldBackgroundLink();
    }

    private String colorIconList(String iconListInput) {
        StringBuilder sb = new StringBuilder();
        String[] listIcon = iconListInput.split(",\\s*");
        for (int i = 0; i < listIcon.length; i++) {
            sb.append(addWebColorForIcon(listIcon[i]));
            if (i != listIcon.length - 1) {
                sb.append(",");
            }
        }
        return sb.toString();
    }

    private String addWebColorForIcon(String icon) {
        String result;
        // Sử dụng thư viện Jsoup
        // Chuyển đổi chuỗi HTML thành một đối tượng Document của Jsoup
        Document doc = Jsoup.parse(icon);

        // Lấy phần tử <i> từ Document
        Element iconElement = doc.select("i").first();

        // Thêm thuộc tính style vào phần tử <i>
        if (iconElement != null) {
            String style = iconElement.attr("style");
            if (style == null || !style.contains("color")) {
                // Thêm thuộc tính style vào phần tử <i>
                iconElement.attr("style", "color: #e3bd74;");
            }
        }

        // In ra chuỗi HTML đã được sửa đổi
        result = iconElement.outerHtml();
        return result;
    }

    // Hàm lấy ra danh sách văn bản phân cách bằng dấu "~" (Content)
    private String[] splitByTilde(String input) {
        // Trước tiên cần loại bỏ khoảng trắng ở đầu và cuối chuỗi
        String inputClearSpace = clearSpaceHeaderAndFooter(input);
        return inputClearSpace.split("~\\s*");
    }

    // Hàm lấy ra danh sách văn bản phân cách bằng dấu "," (Icon)
    private String[] splitByComma(String input) {
        // Trước tiên cần loại bỏ khoảng trắng ở đầu và cuối chuỗi
        String inputClearSpace = clearSpaceHeaderAndFooter(input);
        return inputClearSpace.split(",\\s*");
    }
}
