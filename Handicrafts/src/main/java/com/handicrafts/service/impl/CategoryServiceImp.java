package com.handicrafts.service.impl;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import com.handicrafts.converter.CategoryConverter;
import com.handicrafts.dto.CategoryDTO;
import com.handicrafts.entity.CategoryEntity;
import com.handicrafts.repository.CategoryRepository;
import com.handicrafts.service.ICategoryService;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceImp implements ICategoryService {
    @Autowired
    private CategoryRepository catRepo;
    @Autowired
    private CategoryConverter catConverter;

    @Override
    public List<CategoryDTO> findAll() {
        //repository sẽ lây ra tất cả các category trong dtb
        List<CategoryEntity> catEntities = catRepo.findAll();
        List<CategoryDTO> results = new ArrayList<>();

        //tiến hành convert từ entity sang dto rồi add vào list
        for (CategoryEntity c : catEntities) {
            results.add(catConverter.toDTO(c));
        }
        return results;
    }

    @Override
    public CategoryDTO findById(int id) {
        return catConverter.toDTO(catRepo.findByCategoryID(id));
    }

    @Override
    public void save(CategoryDTO categoryDTO) {
        catRepo.save(catConverter.toEntity(categoryDTO));
    }

    @Override
    public void deleteByCatId(int id) {
        catRepo.deleteByCategoryID(id);
    }

    @Override
    public void updateCat(CategoryDTO cat) {
        catRepo.updateCategory(cat.getName(), cat.getCode(), cat.getCreatedAt(), cat.getUpdatedAt(), cat.getCategoryID());
    }

    @Override
    public List<CategoryDTO> findTenCat() {
        List<CategoryEntity> catEntities = catRepo.findFirst10ByOrderByCategoryIDAsc();
        List<CategoryDTO> results = new ArrayList<>();

        //tiến hành convert từ entity sang dto rồi add vào list
        for (CategoryEntity c : catEntities) {
            results.add(catConverter.toDTO(c));
        }
        return results;
    }
}
