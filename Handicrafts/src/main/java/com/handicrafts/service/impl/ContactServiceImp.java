package com.handicrafts.service.impl;


import com.handicrafts.dto.ContactDTO;
import com.handicrafts.repository.ContactRepository;
import com.handicrafts.service.IContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

// ContactServiceImpl
@Service
class ContactServiceImp implements IContactService {

    @Autowired
    private ContactRepository contactRepository;

    @Override
    public int createContact(ContactDTO dto) {
        return contactRepository.createContact(dto);
    }

    @Override
    public ContactDTO findContactById(int id) {
        return contactRepository.findContactById(id);
    }

    @Override
    public List<ContactDTO> getContactsDatatable(int start, int length, String columnOrder, String orderDir, String search) {
        return contactRepository.getContactsDatatable(start, length, columnOrder, orderDir, search);
    }

    @Override
    public int getRecordsTotal() {
        return contactRepository.getRecordsTotal();
    }

    @Override
    public int getRecordsFiltered(String search) {
        return contactRepository.getRecordsFiltered(search);
    }

    @Override
    public int delete(int id) {
        return contactRepository.deleteContact(id);
    }
    }