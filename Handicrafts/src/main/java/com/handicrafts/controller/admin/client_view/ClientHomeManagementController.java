package com.handicrafts.controller.admin.client_view;

import com.handicrafts.dto.UserDTO;
import com.handicrafts.entity.UserEntity;
import com.handicrafts.repository.UserRepository;
import com.handicrafts.service.IUserService;
import com.handicrafts.util.BlankInputUtil;
import com.handicrafts.util.DetectTypeFileUtil;
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
    private IUserService userService;

    @GetMapping
    public String getClientHomeManagement(Model model, @RequestParam(required = false) String update) {
        UserDTO userDTO = userService.getUserInfo();
        model.addAttribute("userDTO", userDTO);
        if (update != null && update.equals("success")) {
            model.addAttribute("updateSuccess", true);
        }
        return "client-home-management";
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
            RedirectAttributes redirectAttributes) throws IOException {

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
            // Nếu không tải lên file (file.isEmpty()) thì không lỗi
            if (file == null || file.isEmpty()) {
                imageErrors.add(null);
            }
            // Nếu có file, và file không là ảnh thì thêm lỗi
            else if (!DetectTypeFileUtil.isImage(file)) {
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

        UserDTO userDTO = new UserDTO();
        // Nếu không có lỗi thì lưu vào database
        if (isValid) {
            String linkImage1 = null;
            String linkImage2 = null;
            String backgroundImage = null;

            // Xử lý upload file ở đây
            // Thay thế CloudStorageUtil bằng service upload file của Spring
            // Ví dụ:
            // if (!prLink1.isEmpty()) {
            //     linkImage1 = fileStorageService.uploadFile(prLink1);
            // }

            // Thực hiện xóa các khoảng trắng ở đầu và cuối chuỗi cho content và icon
            String clearSpaceIcon1 = clearSpaceHeaderAndFooter(prIcon1);
            String clearSpaceContentTitle1 = clearSpaceHeaderAndFooter(prContentTitle1);
            String clearSpaceContentDes1 = clearSpaceHeaderAndFooter(prContentDes1);
            String clearSpaceContent2 = clearSpaceHeaderAndFooter(prContent2);

            // Thêm màu chủ đạo của icon
            String colorIconList = colorIconList(clearSpaceIcon1);

            userDTO.setWelcomeTitle(welcomeTitle.trim());
            userDTO.setWelcomeDes(welcomeDes);
            userDTO.setProductTitle(productTitle.trim());
            userDTO.setProductDes(productDes.trim());
            userDTO.setPrTitle1(prTitle1.trim());
            userDTO.setPrDes1(prDes1.trim());
            userDTO.setPrIcon1(colorIconList);
            userDTO.setPrContentTitle1(clearSpaceContentTitle1);
            userDTO.setPrContentDes1(clearSpaceContentDes1);

            if (linkImage1 != null) {
                // Xử lý xóa ảnh cũ ở đây nếu cần
                userDTO.setPrLink1(linkImage1);
                userDTO.setPrLink1InStorage(prLink1.getOriginalFilename());
            } else {
                // Set lại link cũ
                userDTO.setPrLink1(userService.findOldImage1Link());
                userDTO.setPrLink1InStorage(prLink1InStorage);
            }

            userDTO.setPrTitle2(prTitle2.trim());
            userDTO.setPrDes2(prDes2);
            userDTO.setPrContent2(clearSpaceContent2);

            if (linkImage2 != null) {
                // Xử lý xóa ảnh cũ ở đây nếu cần
                userDTO.setPrLink2(linkImage2);
                userDTO.setPrLink2InStorage(prLink2.getOriginalFilename());
            } else {
                userDTO.setPrLink2(userService.findOldImage2Link());
                userDTO.setPrLink2InStorage(prLink2InStorage);
            }

            if (backgroundImage != null) {
                // Xử lý xóa ảnh cũ ở đây nếu cần
                userDTO.setBackground(backgroundImage);
                userDTO.setBackgroundInStorage(background.getOriginalFilename());
            } else {
                userDTO.setBackground(userService.findOldBackgroundLink());
                userDTO.setBackgroundInStorage(backgroundInStorage);
            }

            userDTO.setFooterLeft(footerLeft);
            userDTO.setFooterMiddle(footerMiddle);

            // Link thì nên bỏ khoảng trống ở đầu và cuối
            userDTO.setFacebookLink(facebookLink.trim());
            userDTO.setTwitterLink(twitterLink.trim());
            userDTO.setInstagramLink(instagramLink.trim());
            userDTO.setLinkedinLink(linkedinLink.trim());

            boolean updated = userService.updateUser(userDTO);
            if (!updated) {
                model.addAttribute("userDTO", userDTO);
                model.addAttribute("serverError", "e");
                return "client-home-management";
            } else {
                redirectAttributes.addAttribute("update", "success");
                return "redirect:/admin/client-home-management";
            }
        } else {
            // Có lỗi thì gửi danh sách các ô bị lỗi lên
            model.addAttribute("blankErrors", blankErrors);
            model.addAttribute("imageErrors", imageErrors);
            model.addAttribute("notEqualError", notEqualError);
            model.addAttribute("userDTO", userService.getUserInfo());
            return "client-home-management";
        }
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
