// Spring Boot version of ContactAPI (RestController)
package com.handicrafts.api.admin;

import com.handicrafts.dto.ContactDTO;
import com.handicrafts.dto.DatatableDTO;
import com.handicrafts.dto.UserDTO;
import com.handicrafts.service.IContactService;
import com.handicrafts.service.ILogService;
import com.handicrafts.util.SendEmailUtil;
import com.handicrafts.util.SessionUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/contact")
public class ContactAPI {

    @Autowired
    private IContactService contactService;

    @Autowired
    private ILogService<ContactDTO> logService;

    @GetMapping
    public ResponseEntity<DatatableDTO<ContactDTO>> getContactsDatatable(
            @RequestParam int draw,
            @RequestParam int start,
            @RequestParam int length,
            @RequestParam(value = "search[value]", required = false) String searchValue,
            @RequestParam(value = "order[0][column]", defaultValue = "0") String orderBy,
            @RequestParam(value = "order[0][dir]", defaultValue = "asc") String orderDir,
            @RequestParam(value = "columns[{orderBy}][data]", defaultValue = "id") String columnOrder
    ) {
        List<ContactDTO> contacts = contactService.getContactsDatatable(start, length, columnOrder, orderDir, searchValue);
        int recordsTotal = contactService.getRecordsTotal();
        int recordsFiltered = contactService.getRecordsFiltered(searchValue);
        draw++;

        DatatableDTO<ContactDTO> datatable = new DatatableDTO<>(contacts, recordsTotal, recordsFiltered, draw);
        return ResponseEntity.ok(datatable);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteContact(@RequestParam int id, HttpServletRequest request) {
        String status;
        String notify;

        ContactDTO prevContact = contactService.findContactById(id);
        int affectedRow = contactService.delete(id);

        if (affectedRow < 1) {
            ContactDTO currentContact = contactService.findContactById(id);
            logService.log(request, "admin-delete-contact", "FAIL", 2, prevContact, currentContact);
            status = "error";
            notify = "Có lỗi khi xóa contact!";
        } else {
            logService.log(request, "admin-delete-contact", "SUCCESS", 1, prevContact, null);
            status = "success";
            notify = "Xóa contact thành công!";

            UserDTO user = (UserDTO) SessionUtil.getInstance().getValue(request, "user");
            SendEmailUtil.sendDeleteNotify(user.getId(), user.getEmail(), prevContact.getId(), "Contact");
        }

        return ResponseEntity.ok("{" +
                "\"status\": \"" + status + "\"," +
                "\"notify\": \"" + notify + "\"}");
    }
}