package com.handicrafts.repository;

import com.handicrafts.bean.OrderDetailBean;
import com.handicrafts.util.CloseResourceUtil;
import com.handicrafts.util.OpenConnectionUtil;
import com.handicrafts.util.SetParameterUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderDetailRepository {

    public List<OrderDetailBean> findDetailByOrderId(String orderId) {
        String sql = "SELECT order_details.orderId, order_details.productId, products.name, products.originalPrice, " +
                "products.discountPrice, products.discountPercent, order_details.quantity " +
                "FROM order_details INNER JOIN products " +
                "ON order_details.productId = products.id " +
                "WHERE order_details.orderId = ?";
        List<OrderDetailBean> orderDetailList = new ArrayList<>();
        int orderIdConvert = Integer.parseInt(orderId);

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = OpenConnectionUtil.openConnection();
            preparedStatement = connection.prepareStatement(sql);
            SetParameterUtil.setParameter(preparedStatement, orderIdConvert);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                OrderDetailBean orderDetailBean = new OrderDetailBean();
                orderDetailBean.setOrderId(resultSet.getInt("orderId"));
                orderDetailBean.setProductId(resultSet.getInt("productId"));
                orderDetailBean.setProductName(resultSet.getString("name"));
                orderDetailBean.setOriginalPrice(resultSet.getDouble("originalPrice"));
                orderDetailBean.setDiscountPrice(resultSet.getDouble("discountPrice"));
                orderDetailBean.setDiscountPercent(resultSet.getDouble("discountPercent"));
                orderDetailBean.setQuantity(resultSet.getInt("quantity"));

                orderDetailList.add(orderDetailBean);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            CloseResourceUtil.closeResource(resultSet, preparedStatement, connection);
        }
        return orderDetailList;
    }

    public int createOrderDetail(OrderDetailBean orderDetailBean) {
        int affected = -1;
        String sql = "INSERT INTO order_details (orderId, productId, quantity) " +
                "VALUES (?, ?, ?)";

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = OpenConnectionUtil.openConnection();
            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement(sql);
            SetParameterUtil.setParameter(preparedStatement, orderDetailBean.getOrderId(), orderDetailBean.getProductId(), orderDetailBean.getQuantity());
            affected = preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        } finally {
            CloseResourceUtil.closeNotUseRS(preparedStatement, connection);
        }
        return affected;
    }

    public List<OrderDetailBean> getOrderDetailsDatatable(int orderId, int start, int length, String columnOrder, String orderDir, String searchValue) {
        List<OrderDetailBean> orderDetails = new ArrayList<>();
        String sql = "SELECT order_details.orderId, order_details.productId, products.name, products.originalPrice, " +
                "products.discountPrice, products.discountPercent, order_details.quantity " +
                "FROM order_details INNER JOIN products " +
                "ON order_details.productId = products.id " +
                "WHERE (order_details.orderId = ?)";

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = OpenConnectionUtil.openConnection();
            int index = 1;
            if (searchValue != null && !searchValue.isEmpty()) {
                sql += " AND (order_details.productId LIKE ? OR products.name LIKE ? " +
                        "OR products.originalPrice LIKE ? OR products.discountPrice LIKE ? " +
                        "OR products.discountPercent LIKE ? OR order_details.quantity LIKE ?)";
            }
            sql += " ORDER BY " + columnOrder + " " + orderDir + " ";
            sql += "LIMIT ?, ?";

            preparedStatement = connection.prepareStatement(sql);

            // Set param orderId trong tất cả trường hợp
            preparedStatement.setInt(index++, orderId);
            if (searchValue != null && !searchValue.isEmpty()) {
                preparedStatement.setString(index++, "%" + searchValue + "%");
                preparedStatement.setString(index++, "%" + searchValue + "%");
                preparedStatement.setString(index++, "%" + searchValue + "%");
                preparedStatement.setString(index++, "%" + searchValue + "%");
                preparedStatement.setString(index++, "%" + searchValue + "%");
                preparedStatement.setString(index++, "%" + searchValue + "%");
            }
            preparedStatement.setInt(index++, start);
            preparedStatement.setInt(index, length);

            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                OrderDetailBean orderDetailBean = new OrderDetailBean();
                orderDetailBean.setOrderId(resultSet.getInt("orderId"));
                orderDetailBean.setProductId(resultSet.getInt("productId"));
                orderDetailBean.setProductName(resultSet.getString("name"));
                orderDetailBean.setOriginalPrice(resultSet.getDouble("originalPrice"));
                orderDetailBean.setDiscountPrice(resultSet.getDouble("discountPrice"));
                orderDetailBean.setDiscountPercent(resultSet.getDouble("discountPercent"));
                orderDetailBean.setQuantity(resultSet.getInt("quantity"));

                orderDetails.add(orderDetailBean);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            CloseResourceUtil.closeResource(resultSet, preparedStatement, connection);
        }
        return orderDetails;
    }

    public int getRecordsTotal(int orderId) {
        int recordTotal = -1;
        String sql = "SELECT COUNT(orderId) FROM order_details WHERE orderId = ?";

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = OpenConnectionUtil.openConnection();
            preparedStatement = connection.prepareStatement(sql);
            SetParameterUtil.setParameter(preparedStatement, orderId);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                recordTotal = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            CloseResourceUtil.closeResource(resultSet, preparedStatement, connection);
        }
        return recordTotal;
    }

    public int getRecordsFiltered(int orderId, String searchValue) {
        int recordFiltered = -1;
        String sql = "SELECT COUNT(orderId) FROM order_details INNER JOIN products" +
                " ON order_details.productId = products.id" +
                " WHERE orderId = ?";

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = OpenConnectionUtil.openConnection();
            int index = 1;
            if (searchValue != null && !searchValue.isEmpty()) {
                sql += " AND (order_details.productId LIKE ? OR products.name LIKE ? " +
                        "OR products.originalPrice LIKE ? OR products.discountPrice LIKE ? " +
                        "OR products.discountPercent LIKE ? OR order_details.quantity LIKE ?)";
            }
            preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(index++, orderId);
            if (searchValue != null && !searchValue.isEmpty()) {
                preparedStatement.setString(index++, "%" + searchValue + "%");
                preparedStatement.setString(index++, "%" + searchValue + "%");
                preparedStatement.setString(index++, "%" + searchValue + "%");
                preparedStatement.setString(index++, "%" + searchValue + "%");
                preparedStatement.setString(index++, "%" + searchValue + "%");
                preparedStatement.setString(index, "%" + searchValue + "%");
            }
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                recordFiltered = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            CloseResourceUtil.closeResource(resultSet, preparedStatement, connection);
        }
        return recordFiltered;
    }

    public List<Integer> getUnreviewedProductByOrderId(int orderId) {
        List<Integer> productIds = new ArrayList<>();
        String sql = "SELECT productId FROM order_details WHERE (orderId = ? AND reviewed = 0)";

        Connection conn = null;
        PreparedStatement preStat = null;
        ResultSet rs = null;

        try {
            conn = OpenConnectionUtil.openConnection();
            preStat = conn.prepareStatement(sql);
            SetParameterUtil.setParameter(preStat, orderId);
            rs = preStat.executeQuery();

            while (rs.next()) {
                productIds.add(rs.getInt("productId"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            CloseResourceUtil.closeResource(rs, preStat, conn);
        }
        return productIds;
    }

    public void setSuccessReview(int productId, int orderId) {
        String sql = "UPDATE order_details SET reviewed = 1 WHERE (productId = ? AND orderId = ?)";
        Connection conn = null;
        PreparedStatement preStat = null;
        try {
            conn = OpenConnectionUtil.openConnection();
            conn.setAutoCommit(false);
            preStat = conn.prepareStatement(sql);
            SetParameterUtil.setParameter(preStat, productId, orderId);
            preStat.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        } finally {
            CloseResourceUtil.closeNotUseRS(preStat, conn);
        }
    }
}