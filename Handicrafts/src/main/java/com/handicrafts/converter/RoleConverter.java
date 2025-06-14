package com.handicrafts.converter;

import org.springframework.stereotype.Component;
import com.handicrafts.entity.RoleEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
@Component
public class RoleConverter {
    @Autowired
    private ModelMapper modelMapper;
    public RoleEntity toEntity(RoleDTO roleDto){ return modelMapper.map(roleDto, RoleEntity.class);}
    public RoleDTO toDTO(RoleEntity roleEntity){return modelMapper.map(roleEntity, RoleDTO.class);}
}
