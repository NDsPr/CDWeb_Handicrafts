package com.handicrafts.converter;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.handicrafts.dto.UserDTO;
import com.handicrafts.entity.UserEntity;

@Component
public class UserConverter {
    @Autowired
    private ModelMapper modelMapper;

    public UserEntity toEntity(UserDTO userDto) {
        return modelMapper.map(userDto, UserEntity.class);
    }

    public UserDTO toDTO(UserEntity userEntity) {
        return modelMapper.map(userEntity, UserDTO.class);
    }
}
