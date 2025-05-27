package com.handicrafts.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.handicrafts.converter.HandicraftConverter;
import com.handicrafts.dto.HandicraftDTO;
import com.handicrafts.dto.HandicraftImageDTO;
import com.handicrafts.entity.HandicraftEntity;
import com.handicrafts.entity.HandicraftImageEntity;
import com.handicrafts.repository.CategoryRepository;
import com.handicrafts.repository.HandicraftImageRepository;
import com.handicrafts.repository.HandicraftRepository;
import com.handicrafts.repository.CategoryRepository;
import com.handicrafts.service.IHandicraftService;

import java.util.ArrayList;
import java.util.List;

@Service
public class HandicraftServiceImpl implements IHandicraftService{
    @Autowired
    private HandicraftRepository handicraftRepo;
    @Autowired
    private HandicraftConverter handicraftConverter;
    @Autowired
    private HandicraftImageRepository handicraftImageRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private Category_typeRepository category_typeRepository;

    @Override
    public List<HandicraftDTO> findByCategoryCode(String categoryCode, Pageable pageable) {
        List<HandicraftDTO> results = new ArrayList<>();
        for (HandicraftEntity b : handicraftRepo.findByCategoryCode(categoryCode, pageable).getContent()) {
            results.add(handicraftConverter.toDTO(b));
        }
        return results;
    }

    @Override
    public List<HandicraftDTO> findAllByCategory_typeCode(String code, Pageable pageable) {
        List<HandicraftDTO> results = new ArrayList<>();
        for (HandicraftEntity b : handicraftRepo.findAllByCategory_typeCategory_typeCode(code, pageable).getContent()) {
            results.add(handicraftConverter.toDTO(b));
        }
        return results;
    }

    @Override
    public List<HandicraftDTO> findAll(Pageable pageable) {
        List<HandicraftDTO> results = new ArrayList<>();

        //hàm findAll(pageable) sẽ trả về Page<HandicraftEntity>, để chuyển Page thành List thì dùng hàm getContent()
        for (HandicraftEntity b : handicraftRepo.findAll(pageable).getContent()) {
            results.add(handicraftConverter.toDTO(b));
        }
        return results;
    }

    @Override
    public List<HandicraftDTO> findAll() {
        List<HandicraftDTO> results = new ArrayList<>();
        for (HandicraftEntity b : handicraftRepo.findAll()) {
            results.add(handicraftConverter.toDTO(b));
        }
        return results;
    }

    @Override
    public List<HandicraftDTO> findAllContainTitle(String title, Pageable pageable) {
        List<HandicraftDTO> results = new ArrayList<>();
        for (HandicraftEntity b : handicraftRepo.findAllByTitleContains(title, pageable).getContent()) {
            results.add(handicraftConverter.toDTO(b));
        }
        return results;
    }

    @Override
    public List<HandicraftDTO> findHotHandicraft(boolean isActive, boolean isHot) {
        List<HandicraftDTO> results = new ArrayList<>();
        for (HandicraftEntity b : handicraftRepo.findFirst8ByActiveAndHotOrderByIdDesc(isActive, isHot)) {
            results.add(handicraftConverter.toDTO(b));
        }
        return results;
    }

    @Override
    public List<HandicraftDTO> findNewHandicraft(boolean isActive, boolean isNew) {
        List<HandicraftDTO> results = new ArrayList<>();
        for (HandicraftEntity b : handicraftRepo.findFirst8ByActiveAndNewsOrderByIdDesc(isActive, isNew)) {
            results.add(handicraftConverter.toDTO(b));
        }
        return results;
    }

    @Override
    public List<HandicraftDTO> findAllHotHandicraft(boolean isActive, boolean isHot, Pageable pageable) {
        List<HandicraftDTO> results = new ArrayList<>();
        for (HandicraftEntity b : handicraftRepo.findAllByActiveAndHot(isActive, isHot, pageable).getContent()) {
            results.add(handicraftConverter.toDTO(b));
        }
        return results;
    }

    @Override
    public List<HandicraftDTO> findAllNewHandicraft(boolean isActive, boolean isNew, Pageable pageable) {
        List<HandicraftDTO> results = new ArrayList<>();
        for (HandicraftEntity b : handicraftRepo.findAllByActiveAndNews(isActive, isNew, pageable).getContent()) {
            results.add(handicraftConverter.toDTO(b));
        }
        return results;
    }

    @Override
    public List<HandicraftDTO> findByCategoryIdAnQuantityGreaterThan(int categoryId, int quantity) {
        List<HandicraftDTO> results = new ArrayList<>();
        for (HandicraftEntity b : handicraftRepo.findFirst5ByCategoryCategoryIDAndQuantitySoldGreaterThan(categoryId, quantity)) {
            results.add(handicraftConverter.toDTO(b));
        }
        return results;
    }

    @Override
    public List<HandicraftDTO> findByPriceBetween(int from, int to, Pageable pageable) {
        List<HandicraftDTO> results = new ArrayList<>();
        for (HandicraftEntity b : handicraftRepo.findAllByPriceBetween(from, to, pageable).getContent()) {
            results.add(handicraftConverter.toDTO(b));
        }
        return results;
    }

    @Override
    public List<HandicraftDTO> findByPriceGreaterThan(int from, Pageable pageable) {
        List<HandicraftDTO> results = new ArrayList<>();
        for (HandicraftEntity b : handicraftRepo.findAllByPriceGreaterThan(from, pageable).getContent()) {
            results.add(handicraftConverter.toDTO(b));
        }
        return results;
    }

    @Override
    public List<HandicraftDTO> findAllByActiveAndDicount(boolean active, double from, double to, Pageable pageable) {
        List<HandicraftDTO> results = new ArrayList<>();
        for (HandicraftEntity b : handicraftRepo.findAllByActiveAndDiscountPercentBetween(true, from, to, pageable).getContent()) {
            results.add(handicraftConverter.toDTO(b));
        }
        return results;
    }

    @Override
    public int countAllByActiveAndDiscount(boolean active, double from, double to) {
        return handicraftRepo.countAllByActiveAndDiscountPercentBetween(active, from, to);
    }

    @Override
    public int countByCategory(String code) {
        return handicraftRepo.countAllByCategoryCode(code);
    }

    @Override
    public int countByCategory_typeCode(String code) {
        return handicraftRepo.countAllByCategory_typeCategory_typeCode(code);
    }

    @Override
    public int countAllByActive(boolean isActive) {
        return handicraftRepo.countAllByActive(isActive);
    }

    @Override
    public int countAllByHot(boolean isActive, boolean isHot) {
        return handicraftRepo.countAllByActiveAndHot(isActive, isHot);
    }

    @Override
    public int countAllByNews(boolean isActive, boolean isNew) {
        return handicraftRepo.countAllByActiveAndNews(isActive, isNew);
    }

    @Override
    public int countAllByTitleContains(String title) {
        return handicraftRepo.countAllByTitleContains(title);
    }

    @Override
    public int countAllByPriceBetween(int from, int to) {
        return handicraftRepo.countAllByPriceBetween(from, to);
    }

    @Override
    public int countAllByPriceGreaterThan(int from) {
        return handicraftRepo.countAllByPriceGreaterThan(from);
    }

    @Override
    public List<String> autoCompleteTitle(String title) {
        List<HandicraftEntity> handicrafts = handicraftRepo.findAllByActiveAndTitleContains(true, title);
        List<String> result = new ArrayList<>();
        for (HandicraftEntity b : handicrafts) {
            result.add(b.getTitle());
        }
        return result;
    }

    @Override
    public void save(HandicraftDTO handicraft) {
        HandicraftEntity handicraftEntity = new HandicraftEntity();
        if (handicraft.getId() != 0) {
            handicraftEntity = handicraftConverter.fromDtoToEntity(handicraft, handicraftRepo.findById(handicraft.getId()));
            handicraftEntity.setCategory(categoryRepository.findByCategoryID(handicraft.getCategory().getCategoryID()));
            handicraftEntity.setCategory_type(category_typeRepository.findByCategory_typeID(handicraft.getCategory_type().getCategory_typeID()));
        } else
            handicraftEntity = handicraftConverter.toEntity(handicraft);
        handicraftEntity = handicraftRepo.save(handicraftEntity);
        for (HandicraftImageDTO i : handicraft.getImages()) {
            HandicraftImageEntity image = new HandicraftImageEntity();
            image.setPath(i.getPath());
            if (handicraft.getId() != 0) image.setHandicraft(handicraftRepo.findById(handicraft.getId()));
            else image.setHandicraft(handicraftRepo.findFirstByOrderByIdDesc());
            handicraftImageRepository.save(image);
        }
    }

    @Override
    public HandicraftDTO findById(int id) {
        return handicraftConverter.toDTO(handicraftRepo.findById(id));
    }

    @Override
    public void deleteById(int id) {
        handicraftRepo.deleteById(id);
    }

    @Override
    public void updateQuantity(int quantity, int id) {
        handicraftRepo.updateQuantity(quantity, id);
    }
}
