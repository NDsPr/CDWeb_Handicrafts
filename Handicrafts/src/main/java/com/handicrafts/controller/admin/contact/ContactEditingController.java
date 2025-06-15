package com.handicrafts.controller.admin.contact;

import com.handicrafts.dto.ContactDTO;
import com.handicrafts.repository.ContactRepository;
import com.handicrafts.util.NumberValidateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/contact-management")
@RequiredArgsConstructor
public class ContactEditingController {

    private final ContactRepository contactRepository;

    @GetMapping("/editing")
    public String showEditForm(@RequestParam("id") int id,
                               @RequestParam(value = "success", required = false) String success,
                               Model model) {
        ContactDTO contact = contactRepository.findContactById(id);
        model.addAttribute("contactBean", contact);
        if (success != null) {
            model.addAttribute("success", success);
        }
        return "editing-contact";
    }

    @PostMapping("/editing")
    public String updateContact(@RequestParam("id") int id,
                                @RequestParam("email") String email,
                                @RequestParam("firstName") String firstName,
                                @RequestParam("lastName") String lastName,
                                @RequestParam("message") String message,
                                @RequestParam("status") String status) {

        int statusInt = NumberValidateUtil.toInt(status);

        ContactDTO contact = new ContactDTO();
        contact.setId(id);
        contact.setEmail(email);
        contact.setFirstName(firstName);
        contact.setLastName(lastName);
        contact.setMessage(message);
        contact.setStatus(statusInt);

        contactRepository.updateContact(contact);

        return "redirect:/admin/contact-management/editing?id=" + id + "&success=success";
    }
}
