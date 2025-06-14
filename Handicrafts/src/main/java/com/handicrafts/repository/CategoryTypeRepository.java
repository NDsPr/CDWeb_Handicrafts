package com.handicrafts.repository;

import com.handicrafts.dto.CategoryTypeDTO;
import com.handicrafts.util.CloseResourceUtil;
import com.handicrafts.util.OpenConnectionUtil;
import com.handicrafts.util.SetParameterUtil;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoryTypeRepository {
    public List<CategoryTypeDTO> findCategoryTypeByCategoryId(int categoryId) {
        List<CategoryTypeDTO> categoryTypes = new ArrayList<>();
        String sql = "SELECT id, name, categoryId, idOnBrowser FROM category_types WHERE categoryId = ? AND status = 1";

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = OpenConnectionUtil.openConnection();
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
            e.printStackTrace();
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
            connection = OpenConnectionUtil.openConnection();
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
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            CloseResourceUtil.closeResource(resultSet, preparedStatement, connection);
        }
        return categoryTypeDTO;
    }
}