package com.handicrafts.service;

import com.handicrafts.dto.ContactDTO;
import com.handicrafts.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

public interface IContactService {
    int createContact(ContactDTO dto);
    ContactDTO findContactById(int id);
    List<ContactDTO> getContactsDatatable(int start, int length, String columnOrder, String orderDir, String search);
    int getRecordsTotal();
    int getRecordsFiltered(String search);
    int delete(int id);
}


