package com.handicrafts.service;

import com.handicrafts.dto.Category_typeDTO;

import java.util.List;

public interface ICategoryTypeService {
    public List<Category_typeDTO> findAll();

    public void save(Category_typeDTO category);

    public void update(Category_typeDTO category, int id);

    public Category_typeDTO findById(int id);

    public void delete(int id);
}
