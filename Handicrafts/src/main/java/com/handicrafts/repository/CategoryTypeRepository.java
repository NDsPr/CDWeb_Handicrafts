package com.handicrafts.repository;

import com.handicrafts.dto.CategoryTypeDTO;
import com.handicrafts.util.CloseResourceUtil;
import com.handicrafts.util.OpenConnectionUtil;
import com.handicrafts.util.SetParameterUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CategoryTypeRepository {
    private static final Logger logger = LoggerFactory.getLogger(CategoryTypeRepository.class);

    private final OpenConnectionUtil openConnectionUtil;

    @Autowired
    public CategoryTypeRepository(OpenConnectionUtil openConnectionUtil) {
        this.openConnectionUtil = openConnectionUtil;
    }

    public List<CategoryTypeDTO> findCategoryTypeByCategoryId(int categoryId) {
        List<CategoryTypeDTO> categoryTypes = new ArrayList<>();
        String sql = "SELECT id, name, categoryId, idOnBrowser FROM category_types WHERE categoryId = ? AND status = 1";

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = openConnectionUtil.openConnection();
            preparedStatement = connection.prepareStatement(sql);
            SetParameterUtil.setParameter(preparedStatement, categoryId);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                CategoryTypeDTO categoryType = new CategoryTypeDTO();
                categoryType.setId(resultSet.getInt("id"));
                categoryType.setName((resultSet.getString("name")));
                categoryType.setCategoryId(resultSet.getInt("categoryId"));
                categoryType.setIdOnBrowser(resultSet.getString("idOnBrowser"));

                categoryTypes.add(categoryType);
            }
        } catch (SQLException e) {
            logger.error("Error finding category types by categoryId: {}", categoryId, e);
        } finally {
            CloseResourceUtil.closeResource(resultSet, preparedStatement, connection);
        }
        return categoryTypes;
    }

    public CategoryTypeDTO findTypeById(int id) {
        CategoryTypeDTO categoryTypeDTO = new CategoryTypeDTO();
        String sql = "SELECT id, name, description, categoryId, idOnBrowser, status, createdDate, createdBy, modifiedDate, modifiedBy " +
                "FROM category_types " +
                "WHERE id = ? AND status = 1";

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = openConnectionUtil.openConnection();
            preparedStatement = connection.prepareStatement(sql);
            SetParameterUtil.setParameter(preparedStatement, id);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                categoryTypeDTO.setId(resultSet.getInt("id"));
                categoryTypeDTO.setName(resultSet.getString("name"));
                categoryTypeDTO.setDescription(resultSet.getString("description"));
                categoryTypeDTO.setCategoryId(resultSet.getInt("categoryId"));
                categoryTypeDTO.setIdOnBrowser(resultSet.getString("idOnBrowser"));
                categoryTypeDTO.setStatus(resultSet.getInt("status"));
                categoryTypeDTO.setCreatedDate(resultSet.getTimestamp("createdDate"));
                categoryTypeDTO.setCreatedBy(resultSet.getString("createdBy"));
                categoryTypeDTO.setModifiedDate(resultSet.getTimestamp("modifiedDate"));
                categoryTypeDTO.setModifiedBy(resultSet.getString("modifiedBy"));
            } else {
                // Return null if no record found instead of empty object
                return null;
            }
        } catch (SQLException e) {
            logger.error("Error finding category type by id: {}", id, e);
        } finally {
            CloseResourceUtil.closeResource(resultSet, preparedStatement, connection);
        }
        return categoryTypeDTO;
    }

    public List<CategoryTypeDTO> findAll() {
        List<CategoryTypeDTO> categoryTypes = new ArrayList<>();
        String sql = "SELECT id, name, description, categoryId, idOnBrowser, status, createdDate, createdBy, modifiedDate, modifiedBy " +
                "FROM category_types WHERE status = 1";

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = openConnectionUtil.openConnection();
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                CategoryTypeDTO categoryType = new CategoryTypeDTO();
                categoryType.setId(resultSet.getInt("id"));
                categoryType.setName(resultSet.getString("name"));
                categoryType.setDescription(resultSet.getString("description"));
                categoryType.setCategoryId(resultSet.getInt("categoryId"));
                categoryType.setIdOnBrowser(resultSet.getString("idOnBrowser"));
                categoryType.setStatus(resultSet.getInt("status"));
                categoryType.setCreatedDate(resultSet.getTimestamp("createdDate"));
                categoryType.setCreatedBy(resultSet.getString("createdBy"));
                categoryType.setModifiedDate(resultSet.getTimestamp("modifiedDate"));
                categoryType.setModifiedBy(resultSet.getString("modifiedBy"));

                categoryTypes.add(categoryType);
            }
        } catch (SQLException e) {
            logger.error("Error finding all category types", e);
        } finally {
            CloseResourceUtil.closeResource(resultSet, preparedStatement, connection);
        }
        return categoryTypes;
    }

    public int create(CategoryTypeDTO categoryType) {
        String sql = "INSERT INTO category_types (name, description, categoryId, idOnBrowser, status, createdDate, createdBy) " +
                "VALUES (?, ?, ?, ?, ?, NOW(), ?)";
        int affectedRows = 0;

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = openConnectionUtil.openConnection();
            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement(sql);

            SetParameterUtil.setParameter(preparedStatement,
                    categoryType.getName(),
                    categoryType.getDescription(),
                    categoryType.getCategoryId(),
                    categoryType.getIdOnBrowser(),
                    categoryType.getStatus(),
                    categoryType.getCreatedBy());

            affectedRows = preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            logger.error("Error creating category type: {}", categoryType, e);
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
        return affectedRows;
    }

    public int update(CategoryTypeDTO categoryType) {
        String sql = "UPDATE category_types SET name = ?, description = ?, categoryId = ?, idOnBrowser = ?, " +
                "status = ?, modifiedDate = NOW(), modifiedBy = ? WHERE id = ?";
        int affectedRows = 0;

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = openConnectionUtil.openConnection();
            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement(sql);

            SetParameterUtil.setParameter(preparedStatement,
                    categoryType.getName(),
                    categoryType.getDescription(),
                    categoryType.getCategoryId(),
                    categoryType.getIdOnBrowser(),
                    categoryType.getStatus(),
                    categoryType.getModifiedBy(),
                    categoryType.getId());

            affectedRows = preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            logger.error("Error updating category type with ID: {}", categoryType.getId(), e);
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
        return affectedRows;
    }

    public int delete(int id) {
        // Soft delete - update status to 0
        String sql = "UPDATE category_types SET status = 0, modifiedDate = NOW() WHERE id = ?";
        int affectedRows = 0;

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
            logger.error("Error deleting category type with ID: {}", id, e);
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
        return affectedRows;
    }

    public List<CategoryTypeDTO> getCategoryTypesDatatable(int start, int length, String columnOrder, String orderDir, String searchValue) {
        List<CategoryTypeDTO> categoryTypes = new ArrayList<>();
        String sql = "SELECT ct.id, ct.name, ct.description, ct.categoryId, c.name as categoryName, " +
                "ct.idOnBrowser, ct.status, ct.createdDate, ct.createdBy, ct.modifiedDate, ct.modifiedBy " +
                "FROM category_types ct " +
                "LEFT JOIN categories c ON ct.categoryId = c.id " +
                "WHERE ct.status = 1";

        if (searchValue != null && !searchValue.isEmpty()) {
            sql += " AND (ct.id LIKE ? OR ct.name LIKE ? OR ct.description LIKE ? OR c.name LIKE ? OR ct.idOnBrowser LIKE ?)";
        }
        sql += " ORDER BY " + columnOrder + " " + orderDir + " LIMIT ?, ?";

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = openConnectionUtil.openConnection();
            preparedStatement = connection.prepareStatement(sql);

            int index = 1;
            if (searchValue != null && !searchValue.isEmpty()) {
                String searchPattern = "%" + searchValue + "%";
                for (int i = 0; i < 5; i++) {
                    preparedStatement.setString(index++, searchPattern);
                }
            }
            preparedStatement.setInt(index++, start);
            preparedStatement.setInt(index, length);

            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                CategoryTypeDTO categoryType = new CategoryTypeDTO();
                categoryType.setId(resultSet.getInt("id"));
                categoryType.setName(resultSet.getString("name"));
                categoryType.setDescription(resultSet.getString("description"));
                categoryType.setCategoryId(resultSet.getInt("categoryId"));
                categoryType.setName(resultSet.getString("categoryName"));
                categoryType.setIdOnBrowser(resultSet.getString("idOnBrowser"));
                categoryType.setStatus(resultSet.getInt("status"));
                categoryType.setCreatedDate(resultSet.getTimestamp("createdDate"));
                categoryType.setCreatedBy(resultSet.getString("createdBy"));
                categoryType.setModifiedDate(resultSet.getTimestamp("modifiedDate"));
                categoryType.setModifiedBy(resultSet.getString("modifiedBy"));

                categoryTypes.add(categoryType);
            }
        } catch (SQLException e) {
            logger.error("Error getting category types datatable. Start: {}, Length: {}, Order: {}, Direction: {}, Search: {}",
                    start, length, columnOrder, orderDir, searchValue, e);
        } finally {
            CloseResourceUtil.closeResource(resultSet, preparedStatement, connection);
        }
        return categoryTypes;
    }

    public int getRecordsTotal() {
        int recordsTotal = 0;
        String sql = "SELECT COUNT(id) AS count FROM category_types WHERE status = 1";

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = openConnectionUtil.openConnection();
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                recordsTotal = resultSet.getInt("count");
            }
        } catch (SQLException e) {
            logger.error("Error getting total records count", e);
        } finally {
            CloseResourceUtil.closeResource(resultSet, preparedStatement, connection);
        }
        return recordsTotal;
    }

    public int getRecordsFiltered(String searchValue) {
        int recordsFiltered = 0;
        String sql = "SELECT COUNT(ct.id) AS count " +
                "FROM category_types ct " +
                "LEFT JOIN categories c ON ct.categoryId = c.id " +
                "WHERE ct.status = 1";

        if (searchValue != null && !searchValue.isEmpty()) {
            sql += " AND (ct.id LIKE ? OR ct.name LIKE ? OR ct.description LIKE ? OR c.name LIKE ? OR ct.idOnBrowser LIKE ?)";
        }

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = openConnectionUtil.openConnection();
            preparedStatement = connection.prepareStatement(sql);

            if (searchValue != null && !searchValue.isEmpty()) {
                String searchPattern = "%" + searchValue + "%";
                for (int i = 0; i < 5; i++) {
                    preparedStatement.setString(i + 1, searchPattern);
                }
            }

            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                recordsFiltered = resultSet.getInt("count");
            }
        } catch (SQLException e) {
            logger.error("Error getting filtered records count with search value: {}", searchValue, e);
        } finally {
            CloseResourceUtil.closeResource(resultSet, preparedStatement, connection);
        }
        return recordsFiltered;
    }

    public CategoryTypeDTO findByIdOnBrowser(String idOnBrowser) {
        CategoryTypeDTO categoryTypeDTO = null;
        String sql = "SELECT id, name, description, categoryId, idOnBrowser, status, createdDate, createdBy, modifiedDate, modifiedBy " +
                "FROM category_types " +
                "WHERE idOnBrowser = ? AND status = 1";

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = openConnectionUtil.openConnection();
            preparedStatement = connection.prepareStatement(sql);
            SetParameterUtil.setParameter(preparedStatement, idOnBrowser);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                categoryTypeDTO = new CategoryTypeDTO();
                categoryTypeDTO.setId(resultSet.getInt("id"));
                categoryTypeDTO.setName(resultSet.getString("name"));
                categoryTypeDTO.setDescription(resultSet.getString("description"));
                categoryTypeDTO.setCategoryId(resultSet.getInt("categoryId"));
                categoryTypeDTO.setIdOnBrowser(resultSet.getString("idOnBrowser"));
                categoryTypeDTO.setStatus(resultSet.getInt("status"));
                categoryTypeDTO.setCreatedDate(resultSet.getTimestamp("createdDate"));
                categoryTypeDTO.setCreatedBy(resultSet.getString("createdBy"));
                categoryTypeDTO.setModifiedDate(resultSet.getTimestamp("modifiedDate"));
                categoryTypeDTO.setModifiedBy(resultSet.getString("modifiedBy"));
            }
        } catch (SQLException e) {
            logger.error("Error finding category type by idOnBrowser: {}", idOnBrowser, e);
        } finally {
            CloseResourceUtil.closeResource(resultSet, preparedStatement, connection);
        }
        return categoryTypeDTO;
    }
}
