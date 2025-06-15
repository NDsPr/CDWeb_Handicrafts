package com.handicrafts.repository;

import com.handicrafts.bean.OrderBean;
import com.handicrafts.bean.OrderDetailBean;
import com.handicrafts.util.CloseResourceUtil;
import com.handicrafts.util.OpenConnectionUtil;
import com.handicrafts.util.SetParameterUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderRepository {
    public List<OrderDetailBean> findOrderDetailByOrderId(int orderId) {
        List<OrderDetailBean> result = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT order_details.orderId, order_details.productId, products.name, products.originalPrice, products.discountPrice, order_details.quantity ")
                .append("FROM order_details ")
                .append("INNER JOIN products ON order_details.productId = products.id ")
                .append("WHERE order_details.orderId = ?");
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = OpenConnectionUtil.openConnection();
            preparedStatement = connection.prepareStatement(sql.toString());
            SetParameterUtil.setParameter(preparedStatement, orderId);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                OrderDetailBean orderDetail = new OrderDetailBean();
                orderDetail.setOrderId(resultSet.getInt("orderId"));
                orderDetail.setProductId(resultSet.getInt("productId"));
                orderDetail.setProductName(resultSet.getString("name"));
                orderDetail.setOriginalPrice(resultSet.getDouble("originalPrice"));
                orderDetail.setDiscountPrice(resultSet.getDouble("discountPrice"));
                orderDetail.setQuantity(resultSet.getInt("quantity"));

                result.add(orderDetail);
            }
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;

        } finally {
            CloseResourceUtil.closeResource(resultSet, preparedStatement, connection);
        }
    }

    public List<OrderBean> findOrderByUserId(int userId) {
        List<OrderBean> orderList = new ArrayList<>();
        String sql = "SELECT id, createdDate, shipToDate, total, status FROM orders WHERE userId = ?";

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = OpenConnectionUtil.openConnection();
            preparedStatement = connection.prepareStatement(sql);
            SetParameterUtil.setParameter(preparedStatement, userId);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                OrderBean order = new OrderBean();
                order.setId(resultSet.getInt("id"));
                order.setCreatedDate(resultSet.getTimestamp("createdDate"));
                order.setShipToDate(resultSet.getTimestamp("shipToDate"));
                order.setTotal(resultSet.getDouble("total"));
                order.setStatus(resultSet.getInt("status"));

                orderList.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            CloseResourceUtil.closeResource(resultSet, preparedStatement, connection);
        }
        return orderList;
    }


    public List<OrderBean> findAllOrders() {
        String sql = "SELECT id, userId, total, paymentMethod, status, shipToDate, createdDate, createdBy, modifiedDate, modifiedBy " +
                "FROM orders";
        List<OrderBean> orderList = new ArrayList<>();

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = OpenConnectionUtil.openConnection();
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                OrderBean orderBean = new OrderBean();
                orderBean.setId(resultSet.getInt("id"));
                orderBean.setUserId(resultSet.getInt("userId"));
                orderBean.setTotal(resultSet.getDouble("total"));
                orderBean.setPaymentMethod(resultSet.getString("paymentMethod"));
                orderBean.setStatus(resultSet.getInt("status"));
                orderBean.setShipToDate(resultSet.getTimestamp("shipToDate"));
                orderBean.setCreatedDate(resultSet.getTimestamp("createdDate"));
                orderBean.setCreatedBy(resultSet.getString("createdBy"));
                orderBean.setModifiedDate(resultSet.getTimestamp("modifiedDate"));
                orderBean.setModifiedBy(resultSet.getString("modifiedBy"));

                orderList.add(orderBean);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            CloseResourceUtil.closeResource(resultSet, preparedStatement, connection);
        }
        return orderList;
    }

    public OrderBean findOrderById(int id) {
        OrderBean order = null;
        String sql = "SELECT id, userId, total, paymentMethod, status, shipToDate, createdDate, createdBy, modifiedDate, modifiedBy " +
                "FROM orders " +
                "WHERE id = ?";

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = OpenConnectionUtil.openConnection();
            preparedStatement = connection.prepareStatement(sql);
            SetParameterUtil.setParameter(preparedStatement, id);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                order = new OrderBean();
                order.setId(resultSet.getInt("id"));
                order.setUserId(resultSet.getInt("userId"));
                order.setTotal(resultSet.getDouble("total"));
                order.setPaymentMethod(resultSet.getString("paymentMethod"));
                order.setShipToDate(resultSet.getTimestamp("shipToDate"));
                order.setStatus(resultSet.getInt("status"));
                order.setCreatedDate(resultSet.getTimestamp("createdDate"));
                order.setCreatedBy(resultSet.getString("createdBy"));
                order.setModifiedDate(resultSet.getTimestamp("modifiedDate"));
                order.setModifiedBy(resultSet.getString("modifiedBy"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            CloseResourceUtil.closeResource(resultSet, preparedStatement, connection);
        }
        return order;
    }

    public int updateOrder(OrderBean orderBean) {
        int affectedRows = -1;
        String sql = "UPDATE orders " +
                "SET status = ? " +
                "WHERE id = ?";

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = OpenConnectionUtil.openConnection();
            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement(sql);
            SetParameterUtil.setParameter(preparedStatement, orderBean.getStatus(), orderBean.getId());
            affectedRows = preparedStatement.executeUpdate();
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
        return affectedRows;
    }

    public int createOrder(OrderBean orderBean) {
        int id = -1;
        String sql = "INSERT INTO orders (userId, createdDate, shipToDate, " +
                "total, paymentMethod, status, createdBy, modifiedDate, modifiedBy) " +
                "VALUES (?, ?, ?, ?, ?, 1, ?, ?, ?)";

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = OpenConnectionUtil.openConnection();
            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            SetParameterUtil.setParameter(preparedStatement, orderBean.getUserId(), orderBean.getCreatedDate(), orderBean.getShipToDate(),
                    orderBean.getTotal(), orderBean.getPaymentMethod(), orderBean.getCreatedBy(),
                    orderBean.getModifiedDate(), orderBean.getModifiedBy());
            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows > 0) {
                // Retrieve the generated keys
                resultSet = preparedStatement.getGeneratedKeys();

                if (resultSet.next()) {
                    // Get the generated ID
                    id = resultSet.getInt(1);
                }
            }
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
        return id;
    }

    public int cancelOrder(int orderId) {
        int affected = -1;
        String sql = "UPDATE orders " +
                "SET status = 0 " +
                "WHERE id = ? AND status NOT IN (0, 3, 4)";

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = OpenConnectionUtil.openConnection();
            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement(sql);
            SetParameterUtil.setParameter(preparedStatement, orderId);
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

    public int cancerOrderAdmin(int id) {
        int affectRows;
        String sql = "UPDATE orders SET status = 0 WHERE id = ?";

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = OpenConnectionUtil.openConnection();
            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement(sql);
            SetParameterUtil.setParameter(preparedStatement, id);
            affectRows = preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
                return -1;
            } catch (SQLException ex) {
                ex.printStackTrace();
                return -1;
            }
        } finally {
            CloseResourceUtil.closeNotUseRS(preparedStatement, connection);
        }
        return affectRows;
    }

    public List<OrderBean> getOrderDatatable(int start, int length, String columnOrder, String orderDir, String searchValue) {
        List<OrderBean> orders = new ArrayList<>();
    String sql = "SELECT id, userId, total,paymentMethod,status, shipToDate, createdDate, createdBy, modifiedDate,modifiedBy FROM orders";
    int index = 1;

    Connection conn = null;
    PreparedStatement preStat = null;
    ResultSet rs = null;

        try {
        conn = OpenConnectionUtil.openConnection();
        if (searchValue != null && !searchValue.isEmpty()) {
            sql += " WHERE (id LIKE ? OR userId LIKE ? OR total LIKE ? OR paymentMethod LIKE ? " +
                    "OR status LIKE ? OR shipToDate LIKE ? OR createdDate LIKE ? OR createdBy LIKE ? OR modifiedDate LIKE ? OR modifiedBy LIKE ?)";
        }
        sql += " ORDER BY " + columnOrder + " " + orderDir + " ";
        sql += "LIMIT ?, ?";

        preStat = conn.prepareStatement(sql);
        if (searchValue != null && !searchValue.isEmpty()) {
            preStat.setString(index++, "%" + searchValue + "%");
            preStat.setString(index++, "%" + searchValue + "%");
            preStat.setString(index++, "%" + searchValue + "%");
            preStat.setString(index++, "%" + searchValue + "%");
            preStat.setString(index++, "%" + searchValue + "%");
            preStat.setString(index++, "%" + searchValue + "%");
            preStat.setString(index++, "%" + searchValue + "%");
            preStat.setString(index++, "%" + searchValue + "%");
            preStat.setString(index++, "%" + searchValue + "%");
            preStat.setString(index++, "%" + searchValue + "%");
             }
        preStat.setInt(index++, start);
        preStat.setInt(index, length);

        rs = preStat.executeQuery();
        while (rs.next()) {
            OrderBean order = new OrderBean();
            order.setId(rs.getInt("id"));
            order.setUserId(rs.getInt("userId"));
            order.setTotal(rs.getDouble("total"));
            order.setPaymentMethod(rs.getString("paymentMethod"));
            order.setStatus(rs.getInt("status"));
            order.setShipToDate(rs.getTimestamp("shipToDate"));
            order.setCreatedDate(rs.getTimestamp("createdDate"));
            order.setCreatedBy(rs.getString("createdBy"));
            order.setModifiedDate(rs.getTimestamp("modifiedDate"));
            order.setModifiedBy(rs.getString("modifiedBy"));

            orders.add(order);
        }
    } catch (SQLException e) {
        throw new RuntimeException(e);
    } finally {
        CloseResourceUtil.closeResource(rs, preStat, conn);
    }
        return orders;
}

    public int getRecordsTotal() {
        int recordsTotal = -1;
        String sql = "SELECT COUNT(id) FROM orders";

        Connection conn = null;
        PreparedStatement preStat = null;
        ResultSet rs = null;

        try {
            conn = OpenConnectionUtil.openConnection();
            preStat = conn.prepareStatement(sql);
            rs = preStat.executeQuery();

            if (rs.next()) {
                recordsTotal = rs.getInt(1);

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            CloseResourceUtil.closeResource(rs, preStat, conn);
        }
        return recordsTotal;
    }

    public int getRecordsFiltered(String searchValue){
        int recordsFiltered = -1;
        String sql = "SELECT COUNT(id) FROM orders";

        Connection conn = null;
        PreparedStatement preStat = null;
        ResultSet rs = null;

        try {
            conn = OpenConnectionUtil.openConnection();
            if (searchValue != null && !searchValue.isEmpty()) {
                sql +=" WHERE (id LIKE ? OR userId LIKE ? OR total LIKE ? OR paymentMethod LIKE ? " +
                        "OR status LIKE ? OR shipToDate LIKE ? OR createdDate LIKE ? OR createdBy LIKE ? OR modifiedDate LIKE ? OR modifiedBy LIKE ?)";
            }
            preStat = conn.prepareStatement(sql);
            int index = 1;
            if (searchValue != null && !searchValue.isEmpty()) {
                preStat.setString(index++, "%" + searchValue + "%");
                preStat.setString(index++, "%" + searchValue + "%");
                preStat.setString(index++, "%" + searchValue + "%");
                preStat.setString(index++, "%" + searchValue + "%");
                preStat.setString(index++, "%" + searchValue + "%");
                preStat.setString(index++, "%" + searchValue + "%");
                preStat.setString(index++, "%" + searchValue + "%");
                preStat.setString(index++, "%" + searchValue + "%");
                preStat.setString(index++, "%" + searchValue + "%");
                preStat.setString(index++, "%" + searchValue + "%");
               }
            rs = preStat.executeQuery();

            if (rs.next()) {
                recordsFiltered = rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            CloseResourceUtil.closeResource(rs, preStat, conn);
        }
        return recordsFiltered;
    }
}