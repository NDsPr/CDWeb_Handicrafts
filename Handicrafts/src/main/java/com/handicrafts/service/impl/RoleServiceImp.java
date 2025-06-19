package com.handicrafts.service.impl;

import com.handicrafts.dto.RoleDTO;
import com.handicrafts.entity.RoleEntity;
import com.handicrafts.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.handicrafts.service.IRoleService;

import java.util.Optional;

@Service
public class RoleServiceImp implements IRoleService {
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public RoleDTO findRolebyName(String name) {
        Optional<RoleEntity> roleEntityOptional = roleRepository.findByName(name);

        if (roleEntityOptional.isEmpty()) {
            return null;
        }

        RoleEntity roleEntity = roleEntityOptional.get();

        // Chuyển đổi từ RoleEntity sang RoleDTO
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setId(roleEntity.getRoleID());
        roleDTO.setName(roleEntity.getName());

        // Các trường còn lại trong RoleDTO sẽ là null vì RoleEntity không có các trường này
        // Nếu cần thiết, bạn có thể đặt giá trị mặc định cho chúng
        roleDTO.setDescription(null);
        roleDTO.setStatus(null);
        roleDTO.setCreatedDate(null);
        roleDTO.setCreatedBy(null);
        roleDTO.setModifiedDate(null);
        roleDTO.setModifiedBy(null);

        return roleDTO;
    }
}
