package com.handicrafts.converter;

import com.handicrafts.dto.CategoryDTO;
import com.handicrafts.entity.CategoryEntity;
import org.springframework.stereotype.Component;

@Component
public class CategoryConverter {

    public CategoryDTO toDTO(CategoryEntity entity) {
        if (entity == null) {
            return null;
        }

        CategoryDTO dto = new CategoryDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDes(entity.getDescription());
        dto.setProfilePic(entity.getImageUrl()); // Mapping imageUrl to profilePicture

        return dto;
    }

    public CategoryEntity toEntity(CategoryDTO dto) {
        if (dto == null) {
            return null;
        }

        CategoryEntity entity = new CategoryEntity();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDes());
        entity.setImageUrl(dto.getProfilePic()); // Mapping profilePicture to imageUrl

        // Set default values for required fields not present in DTO
        entity.setActive(true);

        return entity;
    }

    public CategoryEntity toEntity(CategoryEntity entity, CategoryDTO dto) {
        if (dto == null) {
            return entity;
        }

        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDes());
        entity.setImageUrl(dto.getProfilePic()); // Mapping profilePicture to imageUrl

        return entity;
    }
}
