package com.handicrafts.controller.admin.client_view;

import com.handicrafts.dto.CustomizeDTO;
import com.handicrafts.entity.CustomizeEntity;
import com.handicrafts.service.impl.CustomizeService;
import com.handicrafts.service.impl.FileStorageService;
import com.handicrafts.util.BlankInputUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin/client-home-management")
public class ClientHomeManagementController {

    @Autowired
    private CustomizeService customizeService;

    @Autowired
    private FileStorageService fileStorageService;

    @GetMapping
    public String getClientHomeManagement(Model model, @RequestParam(required = false) String update) {
        CustomizeDTO customizeDTO = customizeService.getCustomizeInfo();
        model.addAttribute("customizeDTO", customizeDTO);

        if (update != null && update.equals("success")) {
            model.addAttribute("updateSuccess", true);
        }

        return "admin/client-home-management";
    }

    @PostMapping
    public String updateClientHomeManagement(
            @RequestParam String welcomeTitle,
            @RequestParam String welcomeDes,
            @RequestParam String productTitle,
            @RequestParam String productDes,
            @RequestParam String prTitle1,
            @RequestParam String prDes1,
            @RequestParam String prIcon1,
            @RequestParam String prContentTitle1,
            @RequestParam String prContentDes1,
            @RequestParam(required = false) MultipartFile prLink1,
            @RequestParam String prLink1InStorage,
            @RequestParam String prTitle2,
            @RequestParam String prDes2,
            @RequestParam String prContent2,
            @RequestParam(required = false) MultipartFile prLink2,
            @RequestParam String prLink2InStorage,
            @RequestParam(required = false) MultipartFile background,
            @RequestParam String backgroundInStorage,
            @RequestParam String footerLeft,
            @RequestParam String footerMiddle,
            @RequestParam String facebookLink,
            @RequestParam String twitterLink,
            @RequestParam String instagramLink,
            @RequestParam String linkedinLink,
            Model model,
            RedirectAttributes redirectAttributes) {

        // Mảng kiểm tra lỗi input
        String[] inputCheck = {welcomeTitle, welcomeDes, productTitle, productDes,
                prTitle1, prDes1, prIcon1, prContentTitle1, prContentDes1,
                prTitle2, prDes2, prContent2,
                footerLeft, footerMiddle};

        // Mảng kiểm tra lỗi image
        MultipartFile[] fileCheck = {prLink1, prLink2, background};

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
        for (MultipartFile file : fileCheck) {
            // Nếu không tải lên file (file == null hoặc file.isEmpty()) thì không lỗi
            if (file == null || file.isEmpty()) {
                imageErrors.add(null);
            }
            // Nếu có file, và file không là ảnh thì thêm lỗi
            else if (!isImage(file)) {
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

        // Nếu không có lỗi thì lưu vào database
        if (isValid) {
            CustomizeDTO customizeDTO = customizeService.getCustomizeInfo();
            String linkImage1 = null;
            String linkImage2 = null;
            String backgroundImage = null;

            // Thực hiện xóa các khoảng trắng ở đầu và cuối chuỗi cho content và icon
            String clearSpaceIcon1 = clearSpaceHeaderAndFooter(prIcon1);
            String clearSpaceContentTitle1 = clearSpaceHeaderAndFooter(prContentTitle1);
            String clearSpaceContentDes1 = clearSpaceHeaderAndFooter(prContentDes1);
            String clearSpaceContent2 = clearSpaceHeaderAndFooter(prContent2);

            // Thêm màu chủ đạo của icon
            String colorIconList = colorIconList(clearSpaceIcon1);

            customizeDTO.setWelcomeTitle(welcomeTitle.trim());
            customizeDTO.setWelcomeDes(welcomeDes);
            customizeDTO.setProductTitle(productTitle.trim());
            customizeDTO.setProductDes(productDes.trim());
            customizeDTO.setPrTitle1(prTitle1.trim());
            customizeDTO.setPrDes1(prDes1.trim());
            customizeDTO.setPrIcon1(colorIconList);
            customizeDTO.setPrContentTitle1(clearSpaceContentTitle1);
            customizeDTO.setPrContentDes1(clearSpaceContentDes1);

            // Xử lý upload file ảnh 1
            if (prLink1 != null && !prLink1.isEmpty()) {
                try {
                    // Xóa file cũ nếu có
                    if (prLink1InStorage != null && !prLink1InStorage.isEmpty()) {
                        fileStorageService.deleteFile(prLink1InStorage);
                    }

                    // Upload file mới
                    linkImage1 = fileStorageService.uploadFile(prLink1);
                    if (linkImage1 != null) {
                        customizeDTO.setPrLink1(linkImage1);
                        customizeDTO.setPrLink1InStorage(prLink1.getOriginalFilename());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    model.addAttribute("fileUploadError", "Error uploading prLink1 file");
                    model.addAttribute("customizeDTO", customizeService.getCustomizeInfo());
                    return "admin/client-home-management";
                }
            } else {
                // Giữ nguyên link cũ
                customizeDTO.setPrLink1(customizeService.findOldImage1Link());
                customizeDTO.setPrLink1InStorage(prLink1InStorage);
            }

            customizeDTO.setPrTitle2(prTitle2.trim());
            customizeDTO.setPrDes2(prDes2);
            customizeDTO.setPrContent2(clearSpaceContent2);

            // Xử lý upload file ảnh 2
            if (prLink2 != null && !prLink2.isEmpty()) {
                try {
                    // Xóa file cũ nếu có
                    if (prLink2InStorage != null && !prLink2InStorage.isEmpty()) {
                        fileStorageService.deleteFile(prLink2InStorage);
                    }

                    // Upload file mới
                    linkImage2 = fileStorageService.uploadFile(prLink2);
                    if (linkImage2 != null) {
                        customizeDTO.setPrLink2(linkImage2);
                        customizeDTO.setPrLink2InStorage(prLink2.getOriginalFilename());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    model.addAttribute("fileUploadError", "Error uploading prLink2 file");
                    model.addAttribute("customizeDTO", customizeService.getCustomizeInfo());
                    return "admin/client-home-management";
                }
            } else {
                // Giữ nguyên link cũ
                customizeDTO.setPrLink2(customizeService.findOldImage2Link());
                customizeDTO.setPrLink2InStorage(prLink2InStorage);
            }

            // Xử lý upload file background
            if (background != null && !background.isEmpty()) {
                try {
                    // Xóa file cũ nếu có
                    if (backgroundInStorage != null && !backgroundInStorage.isEmpty()) {
                        fileStorageService.deleteFile(backgroundInStorage);
                    }

                    // Upload file mới
                    backgroundImage = fileStorageService.uploadFile(background);
                    if (backgroundImage != null) {
                        customizeDTO.setBackground(backgroundImage);
                        customizeDTO.setBackgroundInStorage(background.getOriginalFilename());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    model.addAttribute("fileUploadError", "Error uploading background file");
                    model.addAttribute("customizeDTO", customizeService.getCustomizeInfo());
                    return "admin/client-home-management";
                }
            } else {
                // Giữ nguyên link cũ
                customizeDTO.setBackground(customizeService.findOldBackgroundLink());
                customizeDTO.setBackgroundInStorage(backgroundInStorage);
            }

            customizeDTO.setFooterLeft(footerLeft);
            customizeDTO.setFooterMiddle(footerMiddle);

            // Link thì nên bỏ khoảng trống ở đầu và cuối
            customizeDTO.setFacebookLink(facebookLink.trim());
            customizeDTO.setTwitterLink(twitterLink.trim());
            customizeDTO.setInstagramLink(instagramLink.trim());
            customizeDTO.setLinkedinLink(linkedinLink.trim());

            // Lưu thông tin vào database
            boolean updated = customizeService.updateCustomize(customizeDTO);

            if (!updated) {
                model.addAttribute("serverError", "e");
                model.addAttribute("customizeDTO", customizeService.getCustomizeInfo());
                return "admin/client-home-management";
            } else {
                redirectAttributes.addAttribute("update", "success");
                return "redirect:/admin/client-home-management";
            }
        } else {
            // Có lỗi thì gửi danh sách các ô bị lỗi lên
            model.addAttribute("blankErrors", blankErrors);
            model.addAttribute("imageErrors", imageErrors);
            model.addAttribute("notEqualError", notEqualError);
            model.addAttribute("customizeDTO", customizeService.getCustomizeInfo());
            return "admin/client-home-management";
        }
    }

    // Phương thức kiểm tra file có phải là ảnh hay không
    private boolean isImage(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && contentType.startsWith("image/");
    }

    private String clearSpaceHeaderAndFooter(String input) {
        return input.trim();
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
        // Sử dụng thư viện Jsoup
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

        // Trả về chuỗi HTML đã được sửa đổi
        return iconElement != null ? iconElement.outerHtml() : "";
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
