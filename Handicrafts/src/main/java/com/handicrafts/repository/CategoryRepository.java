package com.handicrafts.repository;

import com.handicrafts.dto.CategoryDTO;
import com.handicrafts.util.CloseResourceUtil;
import com.handicrafts.util.OpenConnectionUtil;
import org.springframework.stereotype.Repository;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CategoryRepository {
    public List<CategoryDTO> findAllCategories() {
        String sql = "SELECT id, name, profilePic FROM categories";
        List<CategoryDTO> result = new ArrayList<>();

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = OpenConnectionUtil.openConnection();
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                CategoryDTO categoryDTO = new CategoryDTO();
                categoryDTO.setId(resultSet.getInt("id"));
                categoryDTO.setName(resultSet.getString("name"));
                categoryDTO.setProfilePic(resultSet.getString("profilePic"));

                result.add(categoryDTO);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            CloseResourceUtil.closeResource(resultSet, preparedStatement, connection);
        }
        return result;
    }
}
