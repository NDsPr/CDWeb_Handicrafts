package com.handicrafts.repository;

import com.ltw.bean.WarehouseBean;
import com.ltw.util.CloseResourceUtil;
import com.ltw.util.OpenConnectionUtil;
import com.ltw.util.SetParameterUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class WarehouseRepository {
    public static List<WarehouseBean> findAllWarehouses() {
        String sql = "SELECT id, shippingFrom, shippingStart, shippingDone, " +
                "description, createdDate, createdBy"+
                "FROM warehouses";

        List<WarehouseBean> warehouseList = new ArrayList<>();

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = OpenConnectionUtil.openConnection();
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                WarehouseBean warehouseBean = new WarehouseBean();
                warehouseBean.setId(resultSet.getInt("id"));
                warehouseBean.setShippingFrom(resultSet.getString("shippingFrom"));
                warehouseBean.setShippingStart(resultSet.getTimestamp("shippingStart"));
                warehouseBean.setShippingDone(resultSet.getTimestamp("shippingDone"));
                warehouseBean.setDescription(resultSet.getString("description"));
                warehouseBean.setCreatedDate(resultSet.getTimestamp("createdDate"));
                warehouseBean.setCreatedBy(resultSet.getString("createdBy"));

                warehouseList.add(warehouseBean);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            CloseResourceUtil.closeResource(resultSet, preparedStatement, connection);
        }
        return warehouseList;
    }

    public int createWarehouse(WarehouseBean warehouseBean) {
        int id = -1;
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO warehouses ")
                .append("(shippingFrom, shippingStart, shippingDone, description, createdDate, createdBy) ")
                .append("VALUES (?, ?, ?, ?, ?, ?)");

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = OpenConnectionUtil.openConnection();
            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement(sql.toString(), PreparedStatement.RETURN_GENERATED_KEYS);

            SetParameterUtil.setParameter(preparedStatement, warehouseBean.getShippingFrom(), warehouseBean.getShippingStart(),
                    warehouseBean.getShippingDone(), warehouseBean.getDescription(), warehouseBean.getCreatedDate(),
                    warehouseBean.getCreatedBy());

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
                connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        } finally {
            CloseResourceUtil.closeResource(resultSet, preparedStatement, connection);
        }
        return id;
    }

    public WarehouseBean findWarehouseById(int id) {
        WarehouseBean warehouse = null;
        String sql = "SELECT id, shippingFrom, shippingStart, shippingDone, description, createdDate, createdBy " +
                "FROM warehouses WHERE id = ?";

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = OpenConnectionUtil.openConnection();
            preparedStatement = connection.prepareStatement(sql);
            SetParameterUtil.setParameter(preparedStatement, id);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                warehouse = new WarehouseBean();
                warehouse.setId(resultSet.getInt("id"));
                warehouse.setShippingFrom(resultSet.getString("shippingFrom"));
                warehouse.setShippingStart(resultSet.getTimestamp("shippingStart"));
                warehouse.setShippingDone(resultSet.getTimestamp("shippingDone"));
                warehouse.setDescription(resultSet.getString("description"));
                warehouse.setCreatedDate(resultSet.getTimestamp("createdDate"));
                warehouse.setCreatedBy(resultSet.getString("createdBy"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            CloseResourceUtil.closeResource(resultSet, preparedStatement, connection);
        }
        return warehouse;
    }

    public int updateWarehouse(WarehouseBean warehouse) {
        int affectedRows = -1;
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE warehouses SET shippingFrom = ?, shippingStart = ?, shippingDone = ?, description = ?, createdDate = ?, createdBy = ? WHERE id = ?");

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = OpenConnectionUtil.openConnection();
            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement(sql.toString());
            SetParameterUtil.setParameter(preparedStatement, warehouse.getShippingFrom(), warehouse.getShippingStart(),
                    warehouse.getShippingDone(), warehouse.getDescription(), warehouse.getCreatedDate(),
                    warehouse.getCreatedBy(), warehouse.getId());
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

    public int deleteWarehouse(int id) {
        int affectRows;
        String sql = "DELETE FROM warehouses WHERE id = ?";

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

    public List<WarehouseBean> getWarehousesDatatable(int start, int length, String columnOrder, String orderDir, String searchValue) {
        List<WarehouseBean> warehouses = new ArrayList<>();
        String sql = "SELECT id, shippingFrom, shippingStart, shippingDone, description, createdDate, createdBy FROM warehouses";
        int index = 1;

        Connection conn = null;
        PreparedStatement preStat = null;
        ResultSet rs = null;

        try {
            conn = OpenConnectionUtil.openConnection();
            if (searchValue != null && !searchValue.isEmpty()) {
                sql += " WHERE (id LIKE ? OR shippingFrom LIKE ? OR shippingStart LIKE ? OR shippingDone LIKE ? OR description LIKE ? OR createdDate LIKE ? OR createdBy LIKE ?)";
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
            }
            preStat.setInt(index++, start);
            preStat.setInt(index, length);

            rs = preStat.executeQuery();
            while (rs.next()) {
                WarehouseBean warehouse = new WarehouseBean();
                warehouse.setId(rs.getInt("id"));
                warehouse.setShippingFrom(rs.getString("shippingFrom"));
                warehouse.setShippingStart(rs.getTimestamp("shippingStart"));
                warehouse.setShippingDone(rs.getTimestamp("shippingDone"));
                warehouse.setDescription(rs.getString("description"));
                warehouse.setCreatedDate(rs.getTimestamp("createdDate"));
                warehouse.setCreatedBy(rs.getString("createdBy"));

                warehouses.add(warehouse);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            CloseResourceUtil.closeResource(rs, preStat, conn);
        }
        return warehouses;
    }

    public int getRecordsTotal() {
        int recordsTotal = -1;
        String sql = "SELECT COUNT(id) FROM warehouses";

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

    public int getRecordsFiltered(String searchValue) {
        int recordsFiltered = -1;
        String sql = "SELECT COUNT(id) FROM warehouses";

        Connection conn = null;
        PreparedStatement preStat = null;
        ResultSet rs = null;

        try {
            conn = OpenConnectionUtil.openConnection();
            if (searchValue != null && !searchValue.isEmpty()) {
                sql += " WHERE (id LIKE ? OR shippingFrom LIKE ? OR shippingStart LIKE ? OR shippingDone LIKE ? OR description LIKE ? OR createdDate LIKE ? OR createdBy LIKE ?)";
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
                preStat.setString(index, "%" + searchValue + "%");
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
