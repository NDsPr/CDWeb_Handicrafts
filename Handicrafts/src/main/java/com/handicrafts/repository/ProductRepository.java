package com.handicrafts.repository;

import com.handicrafts.dto.ProductDTO;
import com.handicrafts.util.CloseResourceUtil;
import com.handicrafts.util.OpenConnectionUtil;
import com.handicrafts.util.SetParameterUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ProductRepository {
    private static final Logger logger = LoggerFactory.getLogger(ProductRepository.class);

    private final OpenConnectionUtil openConnectionUtil;

    @Autowired
    public ProductRepository(OpenConnectionUtil openConnectionUtil) {
        this.openConnectionUtil = openConnectionUtil;
    }

    @PersistenceContext
    private EntityManager entityManager;

    public List<ProductDTO> findAllProducts() {
        String sql = "SELECT id, name, description, categoryTypeId, originalPrice, discountPrice, " +
                "discountPercent, quantity, soldQuantity, avgRate, numReviews, size, otherSpec, keyword, status, " +
                "createdDate, createdBy, modifiedDate, modifiedBy " +
                "FROM products";

        List<ProductDTO> productList = new ArrayList<>();

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = openConnectionUtil.openConnection();
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                ProductDTO productDTO = new ProductDTO();
                productDTO.setId(resultSet.getInt("id"));
                productDTO.setName(resultSet.getString("name"));
                productDTO.setDescription(resultSet.getString("description"));
                productDTO.setCategoryTypeId(resultSet.getInt("categoryTypeId"));
                productDTO.setOriginalPrice(resultSet.getDouble("originalPrice"));
                productDTO.setDiscountPrice(resultSet.getDouble("discountPrice"));
                productDTO.setDiscountPercent(resultSet.getDouble("discountPercent"));
                productDTO.setQuantity(resultSet.getInt("quantity"));
                productDTO.setSoldQuantity(resultSet.getInt("soldQuantity"));
                productDTO.setAvgRate(resultSet.getDouble("avgRate"));
                productDTO.setNumReviews(resultSet.getInt("numReviews"));
                productDTO.setSize(resultSet.getString("size"));
                productDTO.setOtherSpec(resultSet.getString("otherSpec"));
                productDTO.setKeyword(resultSet.getString("keyword"));
                productDTO.setStatus(resultSet.getInt("status"));
                productDTO.setCreatedDate(resultSet.getTimestamp("createdDate"));
                productDTO.setCreatedBy(resultSet.getString("createdBy"));
                productDTO.setModifiedDate(resultSet.getTimestamp("modifiedDate"));
                productDTO.setModifiedBy(resultSet.getString("modifiedBy"));

                productList.add(productDTO);
            }

        } catch (SQLException e) {
            logger.error("Error finding all products", e);
        } finally {
            CloseResourceUtil.closeResource(resultSet, preparedStatement, connection);
        }
        return productList;
    }

    public ProductDTO findProductById(int id) {
        ProductDTO product = null;
        String sql = "SELECT id, name, description, categoryTypeId, originalPrice, discountPrice, " +
                "discountPercent, quantity, soldQuantity, avgRate, numReviews, size, otherSpec, status, keyword, " +
                "createdDate, createdBy, modifiedDate, modifiedBy " +
                "FROM products " +
                "WHERE id = ? AND status <> 0";

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = openConnectionUtil.openConnection();
            preparedStatement = connection.prepareStatement(sql);
            SetParameterUtil.setParameter(preparedStatement, id);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                product = new ProductDTO();
                product.setId(resultSet.getInt("id"));
                product.setName(resultSet.getString("name"));
                product.setDescription(resultSet.getString("description"));
                product.setCategoryTypeId(resultSet.getInt("categoryTypeId"));
                product.setOriginalPrice(resultSet.getDouble("originalPrice"));
                product.setDiscountPrice(resultSet.getDouble("discountPrice"));
                product.setDiscountPercent(resultSet.getDouble("discountPercent"));
                product.setQuantity(resultSet.getInt("quantity"));
                product.setSoldQuantity(resultSet.getInt("soldQuantity"));
                product.setAvgRate(resultSet.getDouble("avgRate"));
                product.setNumReviews(resultSet.getInt("numReviews"));
                product.setSize(resultSet.getString("size"));
                product.setOtherSpec(resultSet.getString("otherSpec"));
                product.setStatus(resultSet.getInt("status"));
                product.setKeyword(resultSet.getString("keyword"));
                product.setCreatedDate(resultSet.getTimestamp("createdDate"));
                product.setCreatedBy(resultSet.getString("createdBy"));
                product.setModifiedDate(resultSet.getTimestamp("modifiedDate"));
                product.setModifiedBy(resultSet.getString("modifiedBy"));
            }
        } catch (SQLException e) {
            logger.error("Error finding product by ID: {}", id, e);
        } finally {
            CloseResourceUtil.closeResource(resultSet, preparedStatement, connection);
        }
        return product;
    }

    public int updateProduct(ProductDTO productDTO) {
        int affectedRows = -1;
        String sql = "UPDATE products " +
                "SET name = ?, description = ?, categoryTypeId = ?, originalPrice = ?, discountPrice = ?, " +
                "discountPercent = ?, quantity = ?, size = ?, otherSpec = ?, status = ?, keyword = ? " +
                "WHERE id = ?";

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = openConnectionUtil.openConnection();
            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement(sql);

            SetParameterUtil.setParameter(preparedStatement,
                    productDTO.getName(),
                    productDTO.getDescription(),
                    productDTO.getCategoryTypeId(),
                    productDTO.getOriginalPrice(),
                    productDTO.getDiscountPrice(),
                    productDTO.getDiscountPercent(),
                    productDTO.getQuantity(),
                    productDTO.getSize(),
                    productDTO.getOtherSpec(),
                    productDTO.getStatus(),
                    productDTO.getKeyword(),
                    productDTO.getId());

            affectedRows = preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            logger.error("Error updating product with ID: {}", productDTO.getId(), e);
            try {
                if (connection != null) {
                    connection.rollback();
                }
            } catch (SQLException ex) {
                logger.error("Error rolling back transaction", ex);
                throw new RuntimeException(ex);
            }
        } finally {
            CloseResourceUtil.closeNotUseRS(preparedStatement, connection);
        }
        return affectedRows;
    }

    public int createProduct(ProductDTO productDTO) {
        int id = -1;
        String sql = "INSERT INTO products (name, description, categoryTypeId, originalPrice, discountPrice, " +
                "discountPercent, quantity, size, otherSpec, status, keyword) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = openConnectionUtil.openConnection();
            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            SetParameterUtil.setParameter(preparedStatement,
                    productDTO.getName(),
                    productDTO.getDescription(),
                    productDTO.getCategoryTypeId(),
                    productDTO.getOriginalPrice(),
                    productDTO.getDiscountPrice(),
                    productDTO.getDiscountPercent(),
                    productDTO.getQuantity(),
                    productDTO.getSize(),
                    productDTO.getOtherSpec(),
                    productDTO.getStatus(),
                    productDTO.getKeyword());

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                resultSet = preparedStatement.getGeneratedKeys();
                if (resultSet.next()) {
                    id = resultSet.getInt(1);
                }
            }
            connection.commit();
        } catch (SQLException e) {
            logger.error("Error creating product", e);
            try {
                if (connection != null) {
                    connection.rollback();
                }
            } catch (SQLException ex) {
                logger.error("Error rolling back transaction", ex);
                throw new RuntimeException(ex);
            }
        } finally {
            CloseResourceUtil.closeResource(resultSet, preparedStatement, connection);
        }
        return id;
    }

    public int deleteProduct(int id) {
        int affectedRows = -1;
        String sql = "DELETE FROM products WHERE id = ?";

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = openConnectionUtil.openConnection();
            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement(sql);
            SetParameterUtil.setParameter(preparedStatement, id);
            affectedRows = preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            logger.error("Error deleting product with ID: {}", id, e);
            try {
                if (connection != null) {
                    connection.rollback();
                }
                return -1;
            } catch (SQLException ex) {
                logger.error("Error rolling back transaction", ex);
                return -1;
            }
        } finally {
            CloseResourceUtil.closeNotUseRS(preparedStatement, connection);
        }
        return affectedRows;
    }

    public List<ProductDTO> findThreeProductByCategoryId(int categoryId) {
        List<ProductDTO> products = new ArrayList<>();
        // Loại bỏ avgRate và numReviews khỏi truy vấn SQL
        String sql = "SELECT p.id, p.name, p.categoryTypeId, p.originalPrice, p.discountPrice, p.discountPercent " +
                "FROM products p " +
                "JOIN category_types ct ON p.categoryTypeId = ct.id " +
                "WHERE ct.categoryId = ? AND p.status = 1 " +
                "ORDER BY p.modifiedDate DESC " +
                "LIMIT 3";

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = openConnectionUtil.openConnection();
            preparedStatement = connection.prepareStatement(sql);
            SetParameterUtil.setParameter(preparedStatement, categoryId);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                ProductDTO productDTO = new ProductDTO();
                productDTO.setId(resultSet.getInt("id"));
                productDTO.setName(resultSet.getString("name"));
                productDTO.setCategoryTypeId(resultSet.getInt("categoryTypeId"));
                productDTO.setOriginalPrice(resultSet.getDouble("originalPrice"));
                productDTO.setDiscountPrice(resultSet.getDouble("discountPrice"));
                productDTO.setDiscountPercent(resultSet.getDouble("discountPercent"));

                // Đặt giá trị mặc định
                productDTO.setAvgRate(0.0);
                productDTO.setNumReviews(0);

                products.add(productDTO);
            }

            logger.info("Found {} products for category ID: {}", products.size(), categoryId);

        } catch (SQLException e) {
            logger.error("Error finding three products by category ID: {}", categoryId, e);
        } finally {
            CloseResourceUtil.closeResource(resultSet, preparedStatement, connection);
        }
        return products;
    }



    public List<ProductDTO> findFourProductByTypeId(int categoryTypeId) {
        List<ProductDTO> products = new ArrayList<>();
        // Loại bỏ avgRate và numReviews khỏi truy vấn SQL nếu không có cột này
        String sql = "SELECT id, name, categoryTypeId, originalPrice, discountPrice, discountPercent " +
                "FROM products WHERE categoryTypeId = ? AND status = 1 " +
                "ORDER BY modifiedDate DESC " +
                "LIMIT 4";

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            logger.info("Attempting to find four products for categoryTypeId: {}", categoryTypeId);

            connection = openConnectionUtil.openConnection();
            preparedStatement = connection.prepareStatement(sql);
            SetParameterUtil.setParameter(preparedStatement, categoryTypeId);

            logger.info("Executing query for categoryTypeId: {}", categoryTypeId);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                ProductDTO productDTO = new ProductDTO();
                productDTO.setId(resultSet.getInt("id"));
                productDTO.setName(resultSet.getString("name"));
                productDTO.setCategoryTypeId(resultSet.getInt("categoryTypeId"));
                productDTO.setOriginalPrice(resultSet.getDouble("originalPrice"));
                productDTO.setDiscountPrice(resultSet.getDouble("discountPrice"));
                productDTO.setDiscountPercent(resultSet.getDouble("discountPercent"));

                // Đặt giá trị mặc định cho avgRate và numReviews
                productDTO.setAvgRate(0.0);
                productDTO.setNumReviews(0);

                products.add(productDTO);
            }

            logger.info("Successfully found {} products for categoryTypeId: {}", products.size(), categoryTypeId);

        } catch (SQLException e) {
            logger.error("Error finding four products by type ID: {}. Error message: {}",
                    categoryTypeId, e.getMessage(), e);
        } finally {
            CloseResourceUtil.closeResource(resultSet, preparedStatement, connection);
        }
        return products;
    }


    public List<ProductDTO> findByTypeIdAndLimit(int categoryTypeId, double[] range, String sort, int start, int offset) {
        List<ProductDTO> products = new ArrayList<>();
        String sql = modifiedQueryByTypeId(range, sort);

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = openConnectionUtil.openConnection();
            preparedStatement = connection.prepareStatement(sql);
            // Set điều kiện để setParameter (sort đã xử lý riêng trong modifiedQuery)
            if (range == null) {
                SetParameterUtil.setParameter(preparedStatement, categoryTypeId, start, offset);
            } else {
                SetParameterUtil.setParameter(preparedStatement, categoryTypeId, range[0], range[1], start, offset);
            }

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                ProductDTO productDTO = new ProductDTO();
                productDTO.setId(resultSet.getInt("id"));
                productDTO.setName(resultSet.getString("name"));
                productDTO.setCategoryTypeId(resultSet.getInt("categoryTypeId"));
                productDTO.setOriginalPrice(resultSet.getDouble("originalPrice"));
                productDTO.setDiscountPrice(resultSet.getDouble("discountPrice"));
                productDTO.setDiscountPercent(resultSet.getDouble("discountPercent"));

                products.add(productDTO);
            }
        } catch (SQLException e) {
            logger.error("Error finding products by type ID with limit. TypeID: {}, Range: {}, Sort: {}, Start: {}, Offset: {}",
                    categoryTypeId, range, sort, start, offset, e);
        } finally {
            CloseResourceUtil.closeResource(resultSet, preparedStatement, connection);
        }
        return products;
    }

    public List<ProductDTO> findByKeyAndLimit(String key, double[] range, String sort, int start, int offset) {
        List<ProductDTO> products = new ArrayList<>();
        String sql = modifiedQueryByKey(key, range, sort);
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = openConnectionUtil.openConnection();
            preparedStatement = connection.prepareStatement(sql);
            // Set điều kiện để setParameter (sort đã xử lý riêng trong modifiedQuery)
            if (range == null) {
                SetParameterUtil.setParameter(preparedStatement, start, offset);
            } else {
                SetParameterUtil.setParameter(preparedStatement, range[0], range[1], start, offset);
            }

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                ProductDTO productDTO = new ProductDTO();
                productDTO.setId(resultSet.getInt("id"));
                productDTO.setName(resultSet.getString("name"));
                productDTO.setCategoryTypeId(resultSet.getInt("categoryTypeId"));
                productDTO.setOriginalPrice(resultSet.getDouble("originalPrice"));
                productDTO.setDiscountPrice(resultSet.getDouble("discountPrice"));
                productDTO.setDiscountPercent(resultSet.getDouble("discountPercent"));

                products.add(productDTO);
            }
        } catch (SQLException e) {
            logger.error("Error finding products by key with limit. Key: {}, Range: {}, Sort: {}, Start: {}, Offset: {}",
                    key, range, sort, start, offset, e);
        } finally {
            CloseResourceUtil.closeResource(resultSet, preparedStatement, connection);
        }
        return products;
    }

    public int getTotalItemsByCategoryType(int categoryTypeId) {
        String sql = "SELECT COUNT(id) AS tongsanpham FROM products WHERE categoryTypeId = ?";

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = openConnectionUtil.openConnection();
            preparedStatement = connection.prepareStatement(sql);
            SetParameterUtil.setParameter(preparedStatement, categoryTypeId);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("tongsanpham");
            }
        } catch (SQLException e) {
            logger.error("Error getting total items by category type ID: {}", categoryTypeId, e);
        } finally {
            CloseResourceUtil.closeResource(resultSet, preparedStatement, connection);
        }
        return -1;
    }

    private String modifiedQueryByTypeId(double[] range, String sort) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT id, name, categoryTypeId, originalPrice, discountPrice, discountPercent, quantity, soldQuantity, avgRate, numReviews ")
                .append("FROM products WHERE categoryTypeId = ? AND status = 1 ");

        if (range != null) {
            sb.append(" AND (discountPrice BETWEEN ? AND ?) ");
        }

        if (!sort.equals("none")) {
            if (sort.equals("asc")) {
                sb.append("ORDER BY discountPrice ASC ");
            } else if (sort.equals("desc")) {
                sb.append("ORDER BY discountPrice DESC ");
            }
        }
        sb.append("LIMIT ?, ?");
        return sb.toString();
    }

    private String modifiedQueryByKey(String key, double[] range, String sort) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT id, name, categoryTypeId, originalPrice, discountPrice, discountPercent, keyword, quantity, soldQuantity, avgRate, numReviews ")
                .append("FROM products WHERE (name LIKE \"%")
                .append(key)
                .append("%\" OR keyword LIKE \"%")
                .append(key)
                .append("%\") AND status = 1 ");

        if (range != null) {
            sb.append(" AND (discountPrice BETWEEN ? AND ?) ");
        }

        if (!sort.equals("none")) {
            if (sort.equals("asc")) {
                sb.append("ORDER BY discountPrice ASC ");
            } else if (sort.equals("desc")) {
                sb.append("ORDER BY discountPrice DESC ");
            }
        }
        sb.append("LIMIT ?, ?");
        return sb.toString();
    }

    public void updateQuantity(int quantity, int id) {
        String sql = "UPDATE products SET quantity = ? WHERE id = ?";
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = openConnectionUtil.openConnection();
            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement(sql);

            SetParameterUtil.setParameter(preparedStatement, quantity, id);
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            logger.error("Error updating quantity for product ID: {}", id, e);
            try {
                if (connection != null) {
                    connection.rollback();
                }
            } catch (SQLException ex) {
                logger.error("Error rolling back transaction", ex);
                throw new RuntimeException(ex);
            }
        } finally {
            CloseResourceUtil.closeNotUseRS(preparedStatement, connection);
        }
    }

    public List<String> getSuggestTitle(String key) {
        String sql = "SELECT name FROM products WHERE name LIKE ?";
        List<String> suggestKeys = new ArrayList<>();

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = openConnectionUtil.openConnection();
            preparedStatement = connection.prepareStatement(sql);
            String keyQuery = "%" + key + "%";
            SetParameterUtil.setParameter(preparedStatement, keyQuery);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String productName = resultSet.getString("name");
                suggestKeys.add(productName);
            }
        } catch (SQLException e) {
            logger.error("Error getting suggest titles for key: {}", key, e);
        } finally {
            CloseResourceUtil.closeResource(resultSet, preparedStatement, connection);
        }
        return suggestKeys;
    }

    public int getTotalItems() {
        String sql = "SELECT COUNT(id) AS tongsanpham FROM products";

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = openConnectionUtil.openConnection();
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("tongsanpham");
            }
        } catch (SQLException e) {
            logger.error("Error getting total items", e);
        } finally {
            CloseResourceUtil.closeResource(resultSet, preparedStatement, connection);
        }
        return -1;
    }

    public ProductDTO findProductByName(String name) {
        ProductDTO product = null;
        String sql = "SELECT id, name, description, categoryTypeId, originalPrice, discountPrice, " +
                "discountPercent, quantity, soldQuantity, avgRate, numReviews, size, otherSpec, status, keyword, " +
                "createdDate, createdBy, modifiedDate, modifiedBy " +
                "FROM products " +
                "WHERE name = ? AND status <> 0";

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = openConnectionUtil.openConnection();
            preparedStatement = connection.prepareStatement(sql);
            SetParameterUtil.setParameter(preparedStatement, name);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                product = new ProductDTO();
                product.setId(resultSet.getInt("id"));
                product.setName(resultSet.getString("name"));
                product.setDescription(resultSet.getString("description"));
                product.setCategoryTypeId(resultSet.getInt("categoryTypeId"));
                product.setOriginalPrice(resultSet.getDouble("originalPrice"));
                product.setDiscountPrice(resultSet.getDouble("discountPrice"));
                product.setDiscountPercent(resultSet.getDouble("discountPercent"));
                product.setQuantity(resultSet.getInt("quantity"));
                product.setSoldQuantity(resultSet.getInt("soldQuantity"));
                product.setAvgRate(resultSet.getDouble("avgRate"));
                product.setNumReviews(resultSet.getInt("numReviews"));
                product.setSize(resultSet.getString("size"));
                product.setOtherSpec(resultSet.getString("otherSpec"));
                product.setStatus(resultSet.getInt("status"));
                product.setKeyword(resultSet.getString("keyword"));
                product.setCreatedDate(resultSet.getTimestamp("createdDate"));
                product.setCreatedBy(resultSet.getString("createdBy"));
                product.setModifiedDate(resultSet.getTimestamp("modifiedDate"));
                product.setModifiedBy(resultSet.getString("modifiedBy"));
            }
        } catch (SQLException e) {
            logger.error("Error finding product by name: {}", name, e);
        } finally {
            CloseResourceUtil.closeResource(resultSet, preparedStatement, connection);
        }

        return product;
    }

    public boolean isExistProductName(String name) {
        String sql = "SELECT id FROM products WHERE name = ? AND status <> 0";

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = openConnectionUtil.openConnection();
            preparedStatement = connection.prepareStatement(sql);
            SetParameterUtil.setParameter(preparedStatement, name);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return true;
            }
        } catch (SQLException e) {
            logger.error("Error checking if product name exists: {}", name, e);
            throw new RuntimeException(e);
        } finally {
            CloseResourceUtil.closeResource(resultSet, preparedStatement, connection);
        }
        return false;
    }

    public List<ProductDTO> findSixProductsForSuggest(int productId, int categoryTypeId, int offset) {
        String sql = "SELECT id, name, description, categoryTypeId, originalPrice, discountPrice, " +
                "discountPercent, quantity, soldQuantity, avgRate, numReviews, size, otherSpec, keyword, status, " +
                "createdDate, createdBy, modifiedDate, modifiedBy " +
                "FROM products " +
                "WHERE (id <> ? AND categoryTypeId = ?) " +
                "ORDER BY soldQuantity DESC " +
                "LIMIT 6 OFFSET ?";

        List<ProductDTO> productList = new ArrayList<>();

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = openConnectionUtil.openConnection();
            preparedStatement = connection.prepareStatement(sql);
            SetParameterUtil.setParameter(preparedStatement, productId, categoryTypeId, offset);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                ProductDTO productDTO = new ProductDTO();
                productDTO.setId(resultSet.getInt("id"));
                productDTO.setName(resultSet.getString("name"));
                productDTO.setDescription(resultSet.getString("description"));
                productDTO.setCategoryTypeId(resultSet.getInt("categoryTypeId"));
                productDTO.setOriginalPrice(resultSet.getDouble("originalPrice"));
                productDTO.setDiscountPrice(resultSet.getDouble("discountPrice"));
                productDTO.setDiscountPercent(resultSet.getDouble("discountPercent"));
                productDTO.setQuantity(resultSet.getInt("quantity"));
                productDTO.setSoldQuantity(resultSet.getInt("soldQuantity"));
                productDTO.setAvgRate(resultSet.getDouble("avgRate"));
                productDTO.setNumReviews(resultSet.getInt("numReviews"));
                productDTO.setSize(resultSet.getString("size"));
                productDTO.setOtherSpec(resultSet.getString("otherSpec"));
                productDTO.setKeyword(resultSet.getString("keyword"));
                productDTO.setStatus(resultSet.getInt("status"));
                productDTO.setCreatedDate(resultSet.getTimestamp("createdDate"));
                productDTO.setCreatedBy(resultSet.getString("createdBy"));
                productDTO.setModifiedDate(resultSet.getTimestamp("modifiedDate"));
                productDTO.setModifiedBy(resultSet.getString("modifiedBy"));

                productList.add(productDTO);
            }
        } catch (SQLException e) {
            logger.error("Error finding six products for suggest. ProductID: {}, CategoryTypeID: {}, Offset: {}",
                    productId, categoryTypeId, offset, e);
        } finally {
            CloseResourceUtil.closeResource(resultSet, preparedStatement, connection);
        }

        return productList;
    }

    public void updateRateTotal(int productId, double newRateTotal, int numReviews) {
        String sql = "UPDATE products SET avgRate = ? , numReviews = ? WHERE id = ?";

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = openConnectionUtil.openConnection();
            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement(sql);
            SetParameterUtil.setParameter(preparedStatement, newRateTotal, numReviews, productId);
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            logger.error("Error updating rate total for product ID: {}", productId, e);
            try {
                if (connection != null) {
                    connection.rollback();
                }
            } catch (SQLException ex) {
                logger.error("Error rolling back transaction", ex);
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        } finally {
            CloseResourceUtil.closeNotUseRS(preparedStatement, connection);
        }
    }

    public List<ProductDTO> getProductsDatatable(int start, int length, String columnOrder, String orderDir, String searchValue) {
        String sql = "SELECT id, name, description, categoryTypeId, originalPrice, discountPrice, " +
                "discountPercent, quantity, soldQuantity, avgRate, numReviews, size, otherSpec, keyword, status, " +
                "createdDate, createdBy, modifiedDate, modifiedBy " +
                "FROM products WHERE (status <> 0) ";

        List<ProductDTO> productList = new ArrayList<>();
        int index = 1;

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = openConnectionUtil.openConnection();

            if (searchValue != null && !searchValue.isEmpty()) {
                sql += " AND (id LIKE ? OR name LIKE ? OR description LIKE ? OR categoryTypeId LIKE ? OR originalPrice LIKE ? " +
                        "OR discountPrice LIKE ? OR discountPercent LIKE ? OR quantity LIKE ? OR soldQuantity LIKE ? OR avgRate LIKE ? " +
                        "OR numReviews LIKE ? OR size LIKE ? OR otherSpec LIKE ? OR keyword LIKE ? OR status LIKE ? OR createdDate LIKE ? " +
                        "OR createdBy LIKE ? OR modifiedDate LIKE ? OR modifiedBy LIKE ?) ";
            }

            sql += " ORDER BY " + columnOrder + " " + orderDir + " ";
            sql += "LIMIT ?, ?";
            preparedStatement = connection.prepareStatement(sql);

            if (searchValue != null && !searchValue.isEmpty()) {
                String searchPattern = "%" + searchValue + "%";
                for (int i = 0; i < 19; i++) {
                    preparedStatement.setString(index++, searchPattern);
                }
            }

            preparedStatement.setInt(index++, start);
            preparedStatement.setInt(index, length);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                ProductDTO product = new ProductDTO();
                product.setId(resultSet.getInt("id"));
                product.setName(resultSet.getString("name"));
                product.setDescription(resultSet.getString("description"));
                product.setCategoryTypeId(resultSet.getInt("categoryTypeId"));
                product.setOriginalPrice(resultSet.getDouble("originalPrice"));
                product.setDiscountPrice(resultSet.getDouble("discountPrice"));
                product.setDiscountPercent(resultSet.getDouble("discountPercent"));
                product.setQuantity(resultSet.getInt("quantity"));
                product.setSoldQuantity(resultSet.getInt("soldQuantity"));
                product.setAvgRate(resultSet.getDouble("avgRate"));
                product.setNumReviews(resultSet.getInt("numReviews"));
                product.setSize(resultSet.getString("size"));
                product.setOtherSpec(resultSet.getString("otherSpec"));
                product.setKeyword(resultSet.getString("keyword"));
                product.setStatus(resultSet.getInt("status"));
                product.setCreatedDate(resultSet.getTimestamp("createdDate"));
                product.setCreatedBy(resultSet.getString("createdBy"));
                product.setModifiedDate(resultSet.getTimestamp("modifiedDate"));
                product.setModifiedBy(resultSet.getString("modifiedBy"));

                productList.add(product);
            }
        } catch (SQLException e) {
            logger.error("Error getting products datatable. Start: {}, Length: {}, Order: {}, Direction: {}, Search: {}",
                    start, length, columnOrder, orderDir, searchValue, e);
        } finally {
            CloseResourceUtil.closeResource(resultSet, preparedStatement, connection);
        }

        return productList;
    }

    public int getRecordsTotal() {
        int recordsTotal = -1;
        String sql = "SELECT COUNT(id) AS recordsTotal FROM products WHERE status <> 0";

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = openConnectionUtil.openConnection();
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                recordsTotal = resultSet.getInt("recordsTotal");
            }
        } catch (SQLException e) {
            logger.error("Error getting total records count", e);
        } finally {
            CloseResourceUtil.closeResource(resultSet, preparedStatement, connection);
        }

        return recordsTotal;
    }

    public int getRecordsFiltered(String searchValue) {
        int recordsFiltered = -1;
        String sql = "SELECT COUNT(id) AS recordsFiltered FROM products WHERE (status <> 0)";
        int index = 1;

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = openConnectionUtil.openConnection();
            if (searchValue != null && !searchValue.isEmpty()) {
                sql += " AND (id LIKE ? OR name LIKE ? OR description LIKE ? OR categoryTypeId LIKE ? OR originalPrice LIKE ? " +
                        "OR discountPrice LIKE ? OR discountPercent LIKE ? OR quantity LIKE ? OR soldQuantity LIKE ? OR avgRate LIKE ? " +
                        "OR numReviews LIKE ? OR size LIKE ? OR otherSpec LIKE ? OR keyword LIKE ? OR status LIKE ? OR createdDate LIKE ? " +
                        "OR createdBy LIKE ? OR modifiedDate LIKE ? OR modifiedBy LIKE ?) ";
            }

            preparedStatement = connection.prepareStatement(sql);
            if (searchValue != null && !searchValue.isEmpty()) {
                String searchPattern = "%" + searchValue + "%";
                for (int i = 0; i < 19; i++) {
                    preparedStatement.setString(index++, searchPattern);
                }
            }
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                recordsFiltered = resultSet.getInt("recordsFiltered");
            }
        } catch (SQLException e) {
            logger.error("Error getting filtered records count with search value: {}", searchValue, e);
        } finally {
            CloseResourceUtil.closeResource(resultSet, preparedStatement, connection);
        }
        return recordsFiltered;
    }

    public void updateQuantityProduct(int id, int quantity) {
        String sql = "UPDATE products SET quantity = ? WHERE id = ?";
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = openConnectionUtil.openConnection();
            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement(sql);
            SetParameterUtil.setParameter(preparedStatement, quantity, id);
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            logger.error("Error updating quantity for product ID: {}", id, e);
            try {
                if (connection != null) {
                    connection.rollback();
                }
            } catch (SQLException ex) {
                logger.error("Error rolling back transaction", ex);
            }
        } finally {
            CloseResourceUtil.closeNotUseRS(preparedStatement, connection);
        }
    }

    public int disableProduct(int id) {
        int affectedRows = -1;
        String sql = "UPDATE products SET status = 0 WHERE id = ?";
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = openConnectionUtil.openConnection();
            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement(sql);
            SetParameterUtil.setParameter(preparedStatement, id);
            affectedRows = preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            logger.error("Error disabling product with ID: {}", id, e);
            try {
                if (connection != null) {
                    connection.rollback();
                }
            } catch (SQLException ex) {
                logger.error("Error rolling back transaction", ex);
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        } finally {
            CloseResourceUtil.closeNotUseRS(preparedStatement, connection);
        }
        return affectedRows;
    }

    public Integer countTotalProducts() {
        TypedQuery<Long> query = entityManager.createQuery(
                "SELECT COUNT(p) FROM ProductEntity p", Long.class);
        return query.getSingleResult().intValue();
    }

    public int countProduct() {
        int count = 0;
        String sql = "SELECT COUNT(*) FROM products WHERE status <> 0";

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = openConnectionUtil.openConnection();
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                count = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            logger.error("Error counting products", e);
        } finally {
            CloseResourceUtil.closeResource(resultSet, preparedStatement, connection);
        }
        return count;
    }
    public List<ProductDTO> findProductsByCategoryTypeId(int categoryTypeId) {
        List<ProductDTO> products = new ArrayList<>();
        String sql = "SELECT id, name, categoryTypeId, originalPrice, discountPrice, discountPercent, " +
                "quantity, soldQuantity, description, size, otherSpec, keyword, status " +
                "FROM products WHERE categoryTypeId = ? AND status = 1 " +
                "ORDER BY modifiedDate DESC";

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = openConnectionUtil.openConnection();
            preparedStatement = connection.prepareStatement(sql);
            SetParameterUtil.setParameter(preparedStatement, categoryTypeId);

            // Log để debug
            logger.info("Executing query for categoryTypeId: {}", categoryTypeId);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                ProductDTO productDTO = new ProductDTO();
                productDTO.setId(resultSet.getInt("id"));
                productDTO.setName(resultSet.getString("name"));
                productDTO.setCategoryTypeId(resultSet.getInt("categoryTypeId"));
                productDTO.setOriginalPrice(resultSet.getDouble("originalPrice"));
                productDTO.setDiscountPrice(resultSet.getDouble("discountPrice"));
                productDTO.setDiscountPercent(resultSet.getDouble("discountPercent"));
                productDTO.setQuantity(resultSet.getInt("quantity"));
                productDTO.setSoldQuantity(resultSet.getInt("soldQuantity"));
                productDTO.setDescription(resultSet.getString("description"));
                productDTO.setSize(resultSet.getString("size"));
                productDTO.setOtherSpec(resultSet.getString("otherSpec"));
                productDTO.setKeyword(resultSet.getString("keyword"));
                productDTO.setStatus(resultSet.getInt("status"));

                // Đặt giá trị mặc định cho avgRate và numReviews
                productDTO.setAvgRate(0.0);
                productDTO.setNumReviews(0);

                products.add(productDTO);
            }

            // Log số lượng sản phẩm tìm thấy
            logger.info("Found {} products for categoryTypeId: {}", products.size(), categoryTypeId);

        } catch (SQLException e) {
            logger.error("Error finding products by category type ID: {}", categoryTypeId, e);
        } finally {
            CloseResourceUtil.closeResource(resultSet, preparedStatement, connection);
        }

        return products;
    }



    public boolean testDatabaseConnection() {
        Connection connection = null;
        try {
            connection = openConnectionUtil.openConnection();
            if (connection != null && !connection.isClosed()) {
                logger.info("Database connection test successful");
                return true;
            } else {
                logger.error("Database connection test failed - connection is null or closed");
                return false;
            }
        } catch (SQLException e) {
            logger.error("Database connection test failed with error: {}", e.getMessage(), e);
            return false;
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    logger.error("Error closing test connection", e);
                }
            }
        }
    }
    public void checkProductsTableStructure() {
        Connection connection = null;
        ResultSet resultSet = null;

        try {
            connection = openConnectionUtil.openConnection();
            DatabaseMetaData metaData = connection.getMetaData();
            resultSet = metaData.getColumns(null, null, "products", null);

            logger.info("Products table structure:");
            while (resultSet.next()) {
                String columnName = resultSet.getString("COLUMN_NAME");
                String dataType = resultSet.getString("TYPE_NAME");
                logger.info("Column: {}, Type: {}", columnName, dataType);
            }
        } catch (SQLException e) {
            logger.error("Error checking products table structure: {}", e.getMessage(), e);
        } finally {
            CloseResourceUtil.closeResource(resultSet, null, connection);
        }
    }

    public int countByKeyAndLimit(String key, double[] range) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) AS total FROM products WHERE status = 1");

        // Thêm điều kiện tìm kiếm theo key nếu có
        if (key != null && !key.trim().isEmpty()) {
            sql.append(" AND (name LIKE ? OR keyword LIKE ?)");
        }

        // Thêm điều kiện lọc theo khoảng giá nếu có
        if (range != null && range.length == 2) {
            sql.append(" AND discountPrice BETWEEN ? AND ?");
        }

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        int total = 0;

        try {
            connection = openConnectionUtil.openConnection();
            preparedStatement = connection.prepareStatement(sql.toString());

            int index = 1;

            if (key != null && !key.trim().isEmpty()) {
                String likeKey = "%" + key.trim() + "%";
                preparedStatement.setString(index++, likeKey);
                preparedStatement.setString(index++, likeKey);
            }

            if (range != null && range.length == 2) {
                preparedStatement.setDouble(index++, range[0]);
                preparedStatement.setDouble(index, range[1]);
            }

            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                total = resultSet.getInt("total");
            }
        } catch (SQLException e) {
            logger.error("Error counting products by key and range", e);
        } finally {
            CloseResourceUtil.closeResource(resultSet, preparedStatement, connection);
        }

        return total;
    }


}
