package com.handicrafts.service.impl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.handicrafts.converter.AuthorConverter;
import com.handicrafts.dto.AuthorDTO;
import com.handicrafts.entity.AuthorEntity;
import com.handicrafts.repository.AuthorRepository;
import com.handicrafts.service.IAuthorService;

import java.util.ArrayList;
import java.util.List;
@Service
public class Category_typeServiceImp implements ICategory_typeService {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CategoryConverter categoryConverter;

    @Override
    public List<Category_typeDTO> findAll() {
        List<Category_typeDTO> result = new ArrayList<>();
        for (Category_typeEntity a : category_typeRepository.findAll()) {
            result.add(category_typeConverter.toDTO(a));
        }
        return result;
    }

    @Override
    public void save(Category_typeDTO category_type) {
        authorRepository.save(ategory_typeConverter.toEntity(category_type));
    }

    @Override
    public void update(Category_typeDTO category_type, int id) {
        category_typeRepository.updateCategory_type(category_type.getName(), category_type.getCategory_typeCode(), category_type.getCreatedAt(), category_type.getUpdatedAt(), id);
    }

    @Override
    public Category_typeDTO findById(int id) {
        return category_typeConverter.toDTO(category_typeRepository.findByCategory_typeID(id));
    }

    @Override
    public void delete(int id) {
        category_typeRepository.deleteByCategory_typeID(id);
    }
}
