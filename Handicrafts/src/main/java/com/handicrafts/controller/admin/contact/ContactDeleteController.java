package com.handicrafts.controller.admin.contact;

import com.handicrafts.repository.ContactRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/admin/contact-management")
@RequiredArgsConstructor
public class ContactDeleteController {

    private final ContactRepository contactRepository;

    @GetMapping("/delete")
    public String deleteContact(@RequestParam("id") int id, HttpServletRequest request) {
        int affectedRows = contactRepository.deleteContact(id);

        if (affectedRows > 0) {
            return "redirect:/admin/contact-management?success=true";
        } else {
            return "redirect:/admin/contact-management?error=true";
        }
    }
}
