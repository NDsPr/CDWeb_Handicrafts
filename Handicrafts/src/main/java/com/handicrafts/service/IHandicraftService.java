package com.handicrafts.service;

import org.springframework.data.domain.Pageable;
import java.util.List;

public interface IHandicraftService {
    public List<HandicraftDTO> findByCategoryCode(String categoryCode, Pageable pageable);

    public List<HandicraftDTO> findAllByAuthorCode(String code, Pageable pageable);

    public List<HandicraftDTO> findAll(Pageable pageable);

    public List<HandicraftDTO> findAll();

    public List<HandicraftDTO> findAllContainTitle(String title, Pageable pageable);

    public List<HandicraftDTO> findHotHandicraft(boolean isActive, boolean isHot);

    public List<HandicraftDTO> findNewHandicraft(boolean isActive, boolean isNew);

    public List<HandicraftDTO> findAllHotHandicraft(boolean isActive, boolean isHot, Pageable pageable);

    public List<HandicraftDTO> findAllNewHandicraft(boolean isActive, boolean isNew, Pageable pageable);

    public List<HandicraftDTO> findByCategoryIdAnQuantityGreaterThan(int categoryId, int quantity);

    public List<HandicraftDTO> findByPriceBetween(int from, int to, Pageable pageable);

    public List<HandicraftDTO> findByPriceGreaterThan(int from, Pageable pageable);

    public List<HandicraftDTO> findAllByActiveAndDicount(boolean active, double discountFrom, double discountTo, Pageable pageable);

    public int countAllByActiveAndDiscount(boolean active, double discountFrom, double discountTo);

    // count
    public int countByCategory(String code);

    public int countByAuthorCode(String code);

    public int countAllByActive(boolean isActive);

    public int countAllByTitleContains(String titles);

    public int countAllByHot(boolean isActive, boolean isHot);

    public int countAllByNews(boolean isActive, boolean isNew);

    public int countAllByPriceBetween(int from, int to);

    public int countAllByPriceGreaterThan(int from);

    public List<String> autoCompleteTitle(String title);

    public void save(HandicraftDTO handicraft);

    public HandicraftDTO findById(int id);

    public void deleteById(int id);

    public void updateQuantity(int quantity, int id);
}
