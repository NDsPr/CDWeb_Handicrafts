package com.handicrafts.repository;

import com.handicrafts.util.CloseResourceUtil;
import com.handicrafts.util.OpenConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class AdminHomeRepository {

    private static final Logger logger = LoggerFactory.getLogger(AdminHomeRepository.class);
    private final OpenConnectionUtil openConnectionUtil;

    @Autowired
    public AdminHomeRepository(OpenConnectionUtil openConnectionUtil) {
        this.openConnectionUtil = openConnectionUtil;
    }

    public List<Integer> countUser() {
        List<Integer> count = new ArrayList<>();
        String sql = "SELECT " +
                " COUNT(id) AS total_accounts, " +
                " SUM(CASE WHEN status = 1 THEN 1 ELSE 0 END) AS active_accounts," +
                " SUM(CASE WHEN status = 0 THEN 1 ELSE 0 END) AS disable_accounts," +
                " SUM(CASE WHEN roleId = 1 THEN 1 ELSE 0 END) AS client_accounts," +
                " SUM(CASE WHEN roleId = 2 THEN 1 ELSE 0 END) AS admin_accounts" +
                " FROM users";

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = openConnectionUtil.openConnection();
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                count.add(resultSet.getInt(1));
                count.add(resultSet.getInt(2));
                count.add(resultSet.getInt(3));
                count.add(resultSet.getInt(4));
                count.add(resultSet.getInt(5));
            }
        } catch (SQLException e) {
            logger.error("Error counting users", e);
        } finally {
            CloseResourceUtil.closeResource(resultSet, preparedStatement, connection);
        }
        return count;
    }

    public List<Integer> countProduct() {
        List<Integer> count = new ArrayList<>();
        String sql = "SELECT COUNT(id) AS total_products, " +
                "SUM(CASE WHEN status = 1 THEN 1 ELSE 0 END) AS active_products, " +
                "SUM(CASE WHEN status = 2 THEN 1 ELSE 0 END) AS soldout_products, " +
                "SUM(CASE WHEN status = 0 THEN 1 ELSE 0 END) AS disable_products " +
                "FROM products";
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = openConnectionUtil.openConnection();
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                count.add(resultSet.getInt(1));
                count.add(resultSet.getInt(2));
                count.add(resultSet.getInt(3));
                count.add(resultSet.getInt(4));
            }
        } catch (SQLException e) {
            logger.error("Error counting products", e);
        } finally {
            CloseResourceUtil.closeResource(resultSet, preparedStatement, connection);
        }
        return count;
    }

    public List<Integer> countOrder() {
        List<Integer> count = new ArrayList<>();
        String sql = "SELECT COUNT(id) AS total_orders, " +
                "SUM(CASE WHEN status = 3 THEN 1 ELSE 0 END) AS transporting_orders, " +
                "SUM(CASE WHEN status = 4 THEN 1 ELSE 0 END) AS success_orders, " +
                "SUM(CASE WHEN status = 0 THEN 1 ELSE 0 END) AS cancel_orders " +
                "FROM orders";

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = openConnectionUtil.openConnection();
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                count.add(resultSet.getInt(1));
                count.add(resultSet.getInt(2));
                count.add(resultSet.getInt(3));
                count.add(resultSet.getInt(4));
            }
        } catch (SQLException e) {
            logger.error("Error counting orders", e);
        } finally {
            CloseResourceUtil.closeResource(resultSet, preparedStatement, connection);
        }
        return count;
    }
}
