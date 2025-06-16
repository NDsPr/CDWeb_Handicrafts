package com.handicrafts.service;

import com.handicrafts.dto.CategoryTypeDTO;

import java.util.List;

public interface ICategoryTypeService {
    public List<CategoryTypeDTO> findAll();

    public void save(CategoryTypeDTO category);

    public void update(CategoryTypeDTO category, int id);

    public CategoryTypeDTO findById(int id);

    public void delete(int id);
}
