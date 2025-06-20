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

@Controller
@RequestMapping("/admin")
public class AllImageEditingController {

    @Autowired
    private ImageService imageService;

    @GetMapping("/all-image-editing")
    public String showEditForm(@RequestParam("id") int id, Model model) {
        ProductImageDTO image = imageService.findImageById(id);
        model.addAttribute("image", image);
        return "addmin/all-image-editing";
    }

    @PostMapping("/all-image-editing")
    public String editImage(
            @RequestParam("id") int id,
            @RequestParam("name") String name,
            @RequestParam("productId") String productId,
            @RequestParam(value = "file", required = false) MultipartFile file,
            Model model) throws IOException {

        ProductImageDTO image = imageService.findImageById(id);
        String[] inputsForm = new String[]{name, productId};
        ArrayList<String> errors = new ArrayList<>();
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
        model.addAttribute("errors", errors);

        if (isValid) {
            // Cập nhật thông tin cơ bản
            image.setName(name);
            image.setProductId(Integer.parseInt(productId));
            image.setModifiedDate(new Timestamp(System.currentTimeMillis()));
            image.setModifiedBy("");

            // Nếu có file mới được upload
            if (file != null && !file.isEmpty() && file.getContentType() != null && file.getContentType().startsWith("image")) {
                // Xóa ảnh cũ trên cloud storage
                CloudStorageUtil.delete(image.getNameInStorage());

                // Upload ảnh mới
                String nameInStorage = file.getOriginalFilename();
                ProductImageDTO newImageInfo = CloudStorageUtil.uploadOneImageToCloudStorage((Part) file);

                image.setLink(newImageInfo.getLink());
                image.setNameInStorage(nameInStorage);
            }

            // Cập nhật vào database
            imageService.updateImage(image);
            model.addAttribute("success", "s");
        }

        model.addAttribute("image", image);
        return "addmin/all-image-editing";
    }
}
