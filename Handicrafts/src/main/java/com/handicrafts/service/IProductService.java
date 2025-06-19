package com.handicrafts.service;

import com.handicrafts.dto.ProductDTO;
import java.util.List;

public interface IProductService {
    ProductDTO findById(int id);
    List<ProductDTO> findAll();
    int save(ProductDTO productDTO);
    int update(ProductDTO productDTO);
    int delete(int id);
    List<ProductDTO> search(String keyword);
    List<ProductDTO> findSixProductsForSuggest(int productId, int categoryTypeId, int currentPos);
    List<ProductDTO> getProductsDatatable(int start, int length, String columnOrder, String orderDir, String searchValue);
    int getRecordsTotal();
    int getRecordsFiltered(String searchValue);
    int disableProduct(int id);
}
