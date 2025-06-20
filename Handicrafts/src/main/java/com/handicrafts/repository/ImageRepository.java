package com.handicrafts.repository;

import com.handicrafts.dto.ProductImageDTO;
import com.handicrafts.entity.ImageEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ImageRepository {

    private static final Logger logger = LoggerFactory.getLogger(ImageRepository.class);

    @PersistenceContext
    private EntityManager entityManager;

    public ProductImageDTO findImageById(int id) {
        ImageEntity entity = entityManager.find(ImageEntity.class, id);
        if (entity == null) {
            logger.warn("No image found with ID: {}", id);
            return null;
        }
        return convertToDTO(entity);
    }

    @Transactional
    public int insertProductImage(ProductImageDTO imageDTO) {
        ImageEntity entity = convertToEntity(imageDTO);
        entityManager.persist(entity);
        entityManager.flush(); // Để lấy ID được sinh ra
        logger.info("Inserted new image with ID: {}", entity.getId());
        return entity.getId();
    }

    public List<ProductImageDTO> findAllImages() {
        Query query = entityManager.createQuery("SELECT i FROM ImageEntity i ORDER BY i.id DESC");
        List<ImageEntity> entities = query.getResultList();
        List<ProductImageDTO> dtos = new ArrayList<>();

        for (ImageEntity entity : entities) {
            dtos.add(convertToDTO(entity));
        }

        logger.info("Found {} images in total", dtos.size());
        return dtos;
    }

    @Transactional
    public boolean deleteImage(int id) {
        ImageEntity entity = entityManager.find(ImageEntity.class, id);
        if (entity == null) {
            logger.warn("Cannot delete image with ID: {} - not found", id);
            return false;
        }
        entityManager.remove(entity);
        logger.info("Deleted image with ID: {}", id);
        return true;
    }

    @Transactional
    public boolean updateImage(ProductImageDTO imageDTO) {
        ImageEntity entity = entityManager.find(ImageEntity.class, imageDTO.getId());
        if (entity == null) {
            logger.warn("Cannot update image with ID: {} - not found", imageDTO.getId());
            return false;
        }

        // Cập nhật các thuộc tính
        entity.setName(imageDTO.getName());
        entity.setProductId(imageDTO.getProductId());

        // Nếu có cập nhật link ảnh
        if (imageDTO.getLink() != null) {
            entity.setLink(imageDTO.getLink());
            entity.setNameInStorage(imageDTO.getNameInStorage());
        }

        entity.setModifiedDate(imageDTO.getModifiedDate());
        entity.setModifiedBy(imageDTO.getModifiedBy());

        entityManager.merge(entity);
        logger.info("Updated image with ID: {}", imageDTO.getId());
        return true;
    }

    // Phương thức chuyển đổi từ Entity sang DTO
    private ProductImageDTO convertToDTO(ImageEntity entity) {
        ProductImageDTO dto = new ProductImageDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setLink(entity.getLink());
        dto.setProductId(entity.getProductId());
        dto.setNameInStorage(entity.getNameInStorage());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setCreatedBy(entity.getCreatedBy());
        dto.setModifiedDate(entity.getModifiedDate());
        dto.setModifiedBy(entity.getModifiedBy());
        return dto;
    }

    // Phương thức chuyển đổi từ DTO sang Entity
    private ImageEntity convertToEntity(ProductImageDTO dto) {
        ImageEntity entity = new ImageEntity();
        // Không set ID khi tạo mới, để JPA tự sinh
        entity.setName(dto.getName());
        entity.setLink(dto.getLink());
        entity.setProductId(dto.getProductId());
        entity.setNameInStorage(dto.getNameInStorage());
        entity.setCreatedDate(dto.getCreatedDate());
        entity.setCreatedBy(dto.getCreatedBy());
        entity.setModifiedDate(dto.getModifiedDate());
        entity.setModifiedBy(dto.getModifiedBy());
        return entity;
    }

    public List<ProductImageDTO> findImagesByProductId(int productId) {
        Query query = entityManager.createQuery("SELECT i FROM ImageEntity i WHERE i.productId = :productId ORDER BY i.id ASC");
        query.setParameter("productId", productId);
        List<ImageEntity> entities = query.getResultList();
        List<ProductImageDTO> dtos = new ArrayList<>();

        for (ImageEntity entity : entities) {
            dtos.add(convertToDTO(entity));
        }

        logger.info("Found {} images for product ID: {}", dtos.size(), productId);
        return dtos;
    }

    public ProductImageDTO findOneByProductId(int productId) {
        try {
            String jpql = "SELECT i FROM ImageEntity i WHERE i.productId = :productId ORDER BY i.id ASC";
            Query query = entityManager.createQuery(jpql);
            query.setParameter("productId", productId);
            query.setMaxResults(1);

            // In ra câu truy vấn để debug
            logger.info("Query for product ID {}: {}", productId, jpql);
            logger.info("Parameter productId: {}", productId);

            List<ImageEntity> result = query.getResultList();

            // In ra kết quả truy vấn
            logger.info("Query result size for product {}: {}", productId, result.size());

            if (result.isEmpty()) {
                logger.warn("Không tìm thấy hình ảnh cho sản phẩm ID: {}", productId);
                return null;
            }

            ProductImageDTO dto = convertToDTO(result.get(0));
            logger.info("Tìm thấy hình ảnh cho sản phẩm {}: {}", productId, dto.getLink());
            return dto;
        } catch (Exception e) {
            logger.error("Lỗi khi tìm hình ảnh cho sản phẩm {}: {}", productId, e.getMessage());
            return null;
        }
    }

    public List<ProductImageDTO> getThumbnailByProductId(int productId) {
        try {
            String jpql = "SELECT i FROM ImageEntity i WHERE i.productId = :productId ORDER BY i.id ASC";
            List<ProductImageDTO> result = entityManager.createQuery(jpql, ImageEntity.class)
                    .setParameter("productId", productId)
                    .setMaxResults(1)
                    .getResultList()
                    .stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());

            logger.info("Found {} thumbnails for product ID: {}", result.size(), productId);
            return result;
        } catch (Exception e) {
            logger.error("Error getting thumbnail for product {}: {}", productId, e.getMessage());
            return new ArrayList<>();
        }
    }
}
