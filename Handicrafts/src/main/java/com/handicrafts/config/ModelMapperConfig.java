package com.handicrafts.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.DTO;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {
    @DTO
    public  ModelMapper getModelMapper(){return new ModelMapper();}
}
