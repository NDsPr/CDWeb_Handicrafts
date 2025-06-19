package com.handicrafts.service.impl;

import com.handicrafts.dto.CategoryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.handicrafts.converter.CategoryConverter;
import com.handicrafts.repository.CategoryRepository;
import com.handicrafts.service.ICategoryService;

import java.util.List;

@Service
public class CategoryServiceImp implements ICategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryConverter categoryConverter;

    @Override
    public List<CategoryDTO> findAll() {
        // Repository trực tiếp trả về List<CategoryDTO> thay vì List<CategoryEntity>
        return categoryRepository.findAllCategories();
    }

    @Override
    public CategoryDTO findById(int id) {
        // Cần phải cập nhật CategoryRepository để có phương thức findById
        // Hiện tại chưa có phương thức này trong repository
        throw new UnsupportedOperationException("Method findById not implemented yet");
    }

    @Override
    public void save(CategoryDTO categoryDTO) {
        // Cần phải cập nhật CategoryRepository để có phương thức save
        // Hiện tại chưa có phương thức này trong repository
        throw new UnsupportedOperationException("Method save not implemented yet");
    }

    @Override
    public void deleteByCatId(int id) {
        // Cần phải cập nhật CategoryRepository để có phương thức delete
        // Hiện tại chưa có phương thức này trong repository
        throw new UnsupportedOperationException("Method deleteByCatId not implemented yet");
    }

    @Override
    public void updateCat(CategoryDTO cat) {
        // Cần phải cập nhật CategoryRepository để có phương thức update
        // Hiện tại chưa có phương thức này trong repository
        throw new UnsupportedOperationException("Method updateCat not implemented yet");
    }

    @Override
    public List<CategoryDTO> findTenCat() {
        // Cần phải cập nhật CategoryRepository để có phương thức findTenCat
        // Hiện tại chưa có phương thức này trong repository
        throw new UnsupportedOperationException("Method findTenCat not implemented yet");
    }
}
