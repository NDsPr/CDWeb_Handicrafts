package com.handicrafts.repository;

import com.ltw.bean.WarehouseDetailBean;
import com.ltw.util.CloseResourceUtil;
import com.ltw.util.OpenConnectionUtil;
import com.ltw.util.SetParameterUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;



public class WarehouseDetailRepository {

    public List<WarehouseDetailBean> findWarehouseDetailByWarehouseId(int warehouseId) {
        String sql = "SELECT id, warehouseId, productId, quantity FROM warehouse_details WHERE warehouseId = ?";
        List<WarehouseDetailBean> warehouseDetailList = new ArrayList<>();

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = OpenConnectionUtil.openConnection();
            preparedStatement = connection.prepareStatement(sql);
            SetParameterUtil.setParameter(preparedStatement, warehouseId);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                WarehouseDetailBean warehouseDetailBean = new WarehouseDetailBean();
                warehouseDetailBean.setId(resultSet.getInt("id"));
                warehouseDetailBean.setWarehouseId(resultSet.getInt("warehouseId"));
                warehouseDetailBean.setProductId(resultSet.getInt("productId"));
                warehouseDetailBean.setQuantity(resultSet.getInt("quantity"));

                warehouseDetailList.add(warehouseDetailBean);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            CloseResourceUtil.closeResource(resultSet, preparedStatement, connection);
        }
        return warehouseDetailList;
    }

    public List<WarehouseDetailBean> getWarehouseDetailsDatatable(int warehouseId, int start, int length, String columnOrder, String orderDir, String searchValue) {
        List<WarehouseDetailBean> warehouseDetailList = new ArrayList<>();
        String sql = "SELECT id, warehouseId, productId, quantity FROM warehouse_details WHERE (warehouseId = ?)";
        int index = 1;

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = OpenConnectionUtil.openConnection();

            if (searchValue != null && !searchValue.isEmpty()) {
                sql += " AND (id LIKE ? OR warehouseId LIKE ? OR productId LIKE ? OR quantity LIKE ?)";
            }
            sql += " ORDER BY " + columnOrder + " " + orderDir;
            sql += " LIMIT ?, ?";
            preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(index++, warehouseId);
            if (searchValue != null && !searchValue.isEmpty()) {
                preparedStatement.setString(index++, "%" + searchValue + "%");
                preparedStatement.setString(index++, "%" + searchValue + "%");
                preparedStatement.setString(index++, "%" + searchValue + "%");
                preparedStatement.setString(index++, "%" + searchValue + "%");
            }
            preparedStatement.setInt(index++, start);
            preparedStatement.setInt(index, length);

            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                WarehouseDetailBean warehouseDetailBean = new WarehouseDetailBean();
                warehouseDetailBean.setId(resultSet.getInt("id"));
                warehouseDetailBean.setWarehouseId(resultSet.getInt("warehouseId"));
                warehouseDetailBean.setProductId(resultSet.getInt("productId"));
                warehouseDetailBean.setQuantity(resultSet.getInt("quantity"));

                warehouseDetailList.add(warehouseDetailBean);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            CloseResourceUtil.closeResource(resultSet, preparedStatement, connection);
        }
        return warehouseDetailList;
    }

    public int getRecordsTotal() {
        String sql = "SELECT COUNT(id) AS count FROM warehouse_details";
        int count = -1;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = OpenConnectionUtil.openConnection();
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                count = resultSet.getInt("count");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            CloseResourceUtil.closeResource(resultSet, preparedStatement, connection);
        }
        return count;
    }

    public int getRecordsFiltered(int warehouseId, String searchValue) {
        int recordsFiltered = -1;
        String sql = "SELECT COUNT(id) AS count FROM warehouse_details WHERE (warehouseId = ?)";
        int index = 1;

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = OpenConnectionUtil.openConnection();

            if (searchValue != null && !searchValue.isEmpty()) {
                sql += " AND (id LIKE ? OR warehouseId LIKE ? OR productId LIKE ? OR quantity LIKE ?)";
            }
            preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(index, warehouseId);
            if (searchValue != null && !searchValue.isEmpty()) {
                preparedStatement.setString(index++, "%" + searchValue + "%");
                preparedStatement.setString(index++, "%" + searchValue + "%");
                preparedStatement.setString(index++, "%" + searchValue + "%");
                preparedStatement.setString(index, "%" + searchValue + "%");
            }

            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                recordsFiltered = resultSet.getInt("count");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            CloseResourceUtil.closeResource(resultSet, preparedStatement, connection);
        }
        return recordsFiltered;
    }
}