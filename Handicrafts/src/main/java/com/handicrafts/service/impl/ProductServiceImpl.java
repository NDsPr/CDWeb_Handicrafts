package com.handicrafts.service.impl;

import com.handicrafts.dto.ProductDTO;
import com.handicrafts.repository.ProductRepository;
import com.handicrafts.service.IProductService;
import com.handicrafts.util.TransferDataUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements IProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public ProductDTO findById(int id) {
        return productRepository.findProductById(id);
    }

    @Override
    public List<ProductDTO> findAll() {
        return productRepository.findAllProducts();
    }

    @Override
    public int save(ProductDTO productDTO) {
        return productRepository.createProduct(productDTO);
    }

    @Override
    public int update(ProductDTO productDTO) {
        return productRepository.updateProduct(productDTO);
    }

    @Override
    public int delete(int id) {
        return productRepository.disableProduct(id); // hoặc gọi delete nếu bạn có xóa cứng
    }

    @Override
    public List<ProductDTO> search(String keyword) {
        return productRepository.findAllProducts();
    }

    @Override
    public List<ProductDTO> findSixProductsForSuggest(int productId, int categoryTypeId, int currentPos) {
        return List.of();
    }
    @Override
    public List<ProductDTO> getProductsDatatable(int start, int length, String columnOrder, String orderDir, String searchValue) {
        return productRepository.findProductsForDatatable(start, length, columnOrder, orderDir, searchValue);
    }

    @Override
    public int getRecordsTotal() {
        return productRepository.countAllProducts();
    }

    @Override
    public int getRecordsFiltered(String searchValue) {
        return productRepository.countFilteredProducts(searchValue);
    }

    @Override
    public int disableProduct(int id) {
        return productRepository.disableProductById(id);
    }
}
