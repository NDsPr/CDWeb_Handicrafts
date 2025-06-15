package com.handicrafts.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.handicrafts.converter.RoleConverter;
import com.handicrafts.service.IRoleService;

@Service
public class RoleServiceImp implements IRoleService {
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private RoleConverter roleConverter;


    @Override
    public RoleDTO findRolebyName(String name) {
        return roleConverter.toDTO(roleRepository.findByName(name));
    }
}

