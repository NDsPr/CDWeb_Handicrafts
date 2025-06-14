package com.handicrafts.repository;

import com.ltw.bean.CategoryTypeBean;
import com.ltw.util.CloseResourceUtil;
import com.ltw.util.OpenConnectionUtil;
import com.ltw.util.SetParameterUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoryTypeRepository {
    public List<CategoryTypeBean> findCategoryTypeByCategoryId(int categoryId) {
        List<CategoryTypeBean> categoryTypes = new ArrayList<>();
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
                CategoryTypeBean categoryType = new CategoryTypeBean();
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

    public CategoryTypeBean findTypeById(int id) {
        CategoryTypeBean categoryTypeBean = new CategoryTypeBean();
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
                categoryTypeBean.setId(resultSet.getInt("id"));
                categoryTypeBean.setName(resultSet.getString("name"));
                categoryTypeBean.setDescription(resultSet.getString("description"));
                categoryTypeBean.setCategoryId(resultSet.getInt("categoryId"));
                categoryTypeBean.setIdOnBrowser(resultSet.getString("idOnBrowser"));
                categoryTypeBean.setStatus(resultSet.getInt("status"));
                categoryTypeBean.setCreatedDate(resultSet.getTimestamp("createdDate"));
                categoryTypeBean.setCreatedBy(resultSet.getString("createdBy"));
                categoryTypeBean.setModifiedDate(resultSet.getTimestamp("modifiedDate"));
                categoryTypeBean.setModifiedBy(resultSet.getString("modifiedBy"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            CloseResourceUtil.closeResource(resultSet, preparedStatement, connection);
        }
        return categoryTypeBean;
    }
}
