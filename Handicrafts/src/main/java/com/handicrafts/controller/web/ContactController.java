package com.handicrafts.controller.web;

import com.handicrafts.constant.LogLevel;
import com.handicrafts.constant.LogState;
import com.handicrafts.dto.ContactDTO;
import com.handicrafts.dto.CustomizeDTO;
import com.handicrafts.repository.ContactRepository;
import com.handicrafts.repository.CustomizeRepository;

import com.handicrafts.service.ILogService;
import com.handicrafts.util.BlankInputUtil;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/contact")
public class ContactController {

    private final CustomizeRepository customizeRepository;
    private final ContactRepository contactRepository;
    private final ILogService<ContactDTO> logService;
    private final Environment environment;

    public ContactController(
            CustomizeRepository customizeRepository,
            ContactRepository contactRepository,
            ILogService<ContactDTO> logService,
            Environment environment) {
        this.customizeRepository = customizeRepository;
        this.contactRepository = contactRepository;
        this.logService = logService;
        this.environment = environment;
    }

    @GetMapping
    public String showContactPage(Model model) {
        CustomizeDTO customizeInfo = customizeRepository.getCustomizeInfo();
        model.addAttribute("customizeInfo", customizeInfo);
        return "contact";
    }

    @PostMapping
    public String handleContactForm(@ModelAttribute ContactDTO contactDTO, Model model, HttpServletRequest request) {
        CustomizeDTO customizeInfo = customizeRepository.getCustomizeInfo();
        model.addAttribute("customizeInfo", customizeInfo);

        boolean isValid = true;

        if (BlankInputUtil.isBlank(contactDTO.getEmail())) {
            model.addAttribute("emailError", "Email không được để trống");
            isValid = false;
        }
        if (BlankInputUtil.isBlank(contactDTO.getFirstName())) {
            model.addAttribute("firstNameError", "Tên không được để trống");
            isValid = false;
        }
        if (BlankInputUtil.isBlank(contactDTO.getLastName())) {
            model.addAttribute("lastNameError", "Họ không được để trống");
            isValid = false;
        }
        if (BlankInputUtil.isBlank(contactDTO.getMessage())) {
            model.addAttribute("messageError", "Nội dung không được để trống");
            isValid = false;
        }

        String msg;
        if (!isValid) {
            msg = "error";
        } else {
            int id = contactRepository.createContact(contactDTO);
            ContactDTO savedContact = contactRepository.findContactById(id);

            if (id <= 0) {
                logService.log(request, "user-contact", LogState.FAIL, LogLevel.ALERT, null, null);
                msg = "error";
            } else {
                logService.log(request, "user-contact", LogState.SUCCESS, LogLevel.WARNING, null, savedContact);
                msg = "success";
            }
        }

        model.addAttribute("msg", msg);
        return "contact";
    }
}
