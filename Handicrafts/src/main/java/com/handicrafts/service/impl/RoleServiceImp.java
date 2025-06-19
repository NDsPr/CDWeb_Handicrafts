package com.handicrafts.service.impl;

import com.handicrafts.dto.RoleDTO;
import com.handicrafts.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.handicrafts.service.IRoleService;

@Service
public class RoleServiceImp implements IRoleService {
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public RoleDTO findRolebyName(String name) {
        // Giả định rằng RoleRepository trực tiếp trả về RoleDTO
        // tương tự như cách CategoryRepository trả về CategoryDTO
        return roleRepository.findRoleByName(name);
    }
}
