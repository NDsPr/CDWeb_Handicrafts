package com.handicrafts.repository;

import com.handicrafts.dto.ReviewDTO;
import com.handicrafts.util.CloseResourceUtil;
import com.handicrafts.util.OpenConnectionUtil;
import com.handicrafts.util.SetParameterUtil;
import org.springframework.stereotype.Repository;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ReviewRepository {

    public List<ReviewDTO> findAllReviews() {
        String sql = "SELECT id, productId, productName, userId, username, " +
                "orderId, content, rating, status, " +  // thiếu dấu phẩy trong code gốc trước "createdDate"
                "createdDate, createdBy, modifiedDate, modifiedBy " +
                "FROM reviews";

        List<ReviewDTO> reviewList = new ArrayList<>();

        try (
                Connection connection = OpenConnectionUtil.openConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                ResultSet resultSet = preparedStatement.executeQuery()
        ) {
            while (resultSet.next()) {
                ReviewDTO reviewDTO = new ReviewDTO();
                reviewDTO.setId(resultSet.getInt("id"));
                reviewDTO.setProductId(resultSet.getInt("productId"));
                reviewDTO.setProductName(resultSet.getString("productName"));
                reviewDTO.setUserId(resultSet.getInt("userId"));
                reviewDTO.setUsername(resultSet.getString("username"));
                reviewDTO.setOrderId(resultSet.getInt("orderId"));
                reviewDTO.setContent(resultSet.getString("content"));
                reviewDTO.setRating(resultSet.getInt("rating"));
                reviewDTO.setStatus(resultSet.getInt("status"));
                reviewDTO.setCreatedDate(resultSet.getTimestamp("createdDate"));
                reviewDTO.setCreatedBy(resultSet.getString("createdBy"));
                reviewDTO.setModifiedDate(resultSet.getTimestamp("modifiedDate"));
                reviewDTO.setModifiedBy(resultSet.getString("modifiedBy"));

                reviewList.add(reviewDTO);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reviewList;
    }

    public int createReview(ReviewDTO reviewDTO) {
        int id = -1;
        String sql = "INSERT INTO reviews (productId, productName, userId, username, " +
                "orderId, content, rating, status, " +
                "createdDate, createdBy, modifiedDate, modifiedBy) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, 1, ?, ?, ?, ?)";

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = OpenConnectionUtil.openConnection();
            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            SetParameterUtil.setParameter(preparedStatement, reviewDTO.getProductId(), reviewDTO.getProductName(), reviewDTO.getUserId(),
                    reviewDTO.getUsername(), reviewDTO.getOrderId(), reviewDTO.getContent(), reviewDTO.getRating(),
                    reviewDTO.getCreatedDate(), reviewDTO.getCreatedBy(),
                    reviewDTO.getModifiedDate(), reviewDTO.getModifiedBy());
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                resultSet = preparedStatement.getGeneratedKeys();
                if (resultSet.next()) {
                    id = resultSet.getInt(1);
                }
            }
            connection.commit();
        } catch (SQLException e) {
            try {
                if (connection != null) connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        } finally {
            CloseResourceUtil.closeResource(resultSet, preparedStatement, connection);
        }
        return id;
    }

    public List<ReviewDTO> findReviewPaginationByProductId(int productId, int offset, int rating) {
        String sql = "SELECT id, productId, userId, username, " +
                "orderId, content, rating, status, " +
                "createdDate, createdBy, modifiedDate, modifiedBy " +
                "FROM reviews " +
                "WHERE productId = ? AND status = 1";

        if (rating > 0 && rating < 6) {
            sql += " AND rating = ?";
        }

        sql += " ORDER BY createdDate DESC LIMIT 5 OFFSET ?";

        List<ReviewDTO> reviews = new ArrayList<>();

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = OpenConnectionUtil.openConnection();
            preparedStatement = connection.prepareStatement(sql);
            if (rating > 0 && rating < 6) {
                SetParameterUtil.setParameter(preparedStatement, productId, rating, offset);
            } else {
                SetParameterUtil.setParameter(preparedStatement, productId, offset);
            }

            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                ReviewDTO reviewDTO = new ReviewDTO();
                reviewDTO.setId(resultSet.getInt("id"));
                reviewDTO.setProductId(resultSet.getInt("productId"));
                reviewDTO.setUserId(resultSet.getInt("userId"));
                reviewDTO.setUsername(resultSet.getString("username"));
                reviewDTO.setOrderId(resultSet.getInt("orderId"));
                reviewDTO.setContent(resultSet.getString("content"));
                reviewDTO.setRating(resultSet.getInt("rating"));
                reviewDTO.setStatus(resultSet.getInt("status"));
                reviewDTO.setCreatedDate(resultSet.getTimestamp("createdDate"));
                reviewDTO.setCreatedBy(resultSet.getString("createdBy"));
                reviewDTO.setModifiedDate(resultSet.getTimestamp("modifiedDate"));
                reviewDTO.setModifiedBy(resultSet.getString("modifiedBy"));
                reviews.add(reviewDTO);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            CloseResourceUtil.closeResource(resultSet, preparedStatement, connection);
        }
        return reviews;
    }

    public void disableReview(int id) {
        String sql = "UPDATE reviews SET status = 0 WHERE id = ?";

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = OpenConnectionUtil.openConnection();
            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement(sql);
            SetParameterUtil.setParameter(preparedStatement, id);
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            try {
                if (connection != null) connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        } finally {
            CloseResourceUtil.closeNotUseRS(preparedStatement, connection);
        }
    }

    public int getRateTotal(int productId) {
        int rateTotal = 0;
        String sql = "SELECT SUM(rating) FROM reviews WHERE productId = ?";

        try (
                Connection connection = OpenConnectionUtil.openConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
            SetParameterUtil.setParameter(preparedStatement, productId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    rateTotal = resultSet.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rateTotal;
    }

    public int getRelatedReview(int productId) {
        int relatedReview = 0;
        String sql = "SELECT COUNT(productId) FROM reviews WHERE productId = ?";

        try (
                Connection connection = OpenConnectionUtil.openConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
            SetParameterUtil.setParameter(preparedStatement, productId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    relatedReview = resultSet.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return relatedReview;
    }
}
