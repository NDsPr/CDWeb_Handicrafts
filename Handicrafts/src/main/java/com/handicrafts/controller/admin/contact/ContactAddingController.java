package com.handicrafts.controller.admin.contact;

import com.handicrafts.dto.ContactDTO;
import com.handicrafts.repository.ContactRepository;
import com.handicrafts.util.BlankInputUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin/contact-management")
@RequiredArgsConstructor
public class ContactAddingController {

    private final ContactRepository contactRepository;

    @GetMapping("/adding")
    public String showAddContactForm() {
        return "adding-contact"; // View Thymeleaf hoặc JSP
    }

    @PostMapping("/adding")
    public String handleAddContact(
            @ModelAttribute("contact") ContactDTO contactDTO,
            Model model
    ) {
        List<String> errors = new ArrayList<>();
        boolean isValid = true;

        String[] inputsForm = {
                contactDTO.getEmail(),
                contactDTO.getFirstName(),
                contactDTO.getLastName(),
                contactDTO.getMessage()
        };

        for (String input : inputsForm) {
            if (BlankInputUtil.isBlank(input)) {
                errors.add("e");
                isValid = false;
            } else {
                errors.add(null);
            }
        }

        model.addAttribute("errors", errors);

        if (isValid) {
            contactRepository.createContact(contactDTO);
            return "redirect:/admin/contact-management/adding?success=success";
        } else {
            return "adding-contact";
        }
    }
}
