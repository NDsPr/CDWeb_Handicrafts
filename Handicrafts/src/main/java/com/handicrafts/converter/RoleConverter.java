package com.handicrafts.converter;

import com.handicrafts.dto.RoleDTO;
import com.handicrafts.entity.RoleEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

public class RoleConverter {
    @Autowired
    private ModelMapper modelMapper;
    public RoleEntity toEntity(RoleDTO roleDto){ return modelMapper.map(roleDto. RoleEntity.class);}
    public RoleDTO toDTO(RoleEntity roleEntity){return modelMapper.map(roleEntity, RoleDTO.class);}
}
