package com.handicrafts.repository;

import com.handicrafts.dto.WarehouseDTO;
import com.handicrafts.util.CloseResourceUtil;
import com.handicrafts.util.OpenConnectionUtil;
import com.handicrafts.util.SetParameterUtil;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class WarehouseRepository {

    public List<WarehouseDTO> findAllWarehouses() {
        List<WarehouseDTO> warehouseList = new ArrayList<>();
        String sql = "SELECT id, shippingFrom, shippingStart, shippingDone, description, createdDate, createdBy FROM warehouses";

        try (Connection conn = OpenConnectionUtil.openConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                WarehouseDTO warehouse = new WarehouseDTO();
                warehouse.setId(rs.getInt("id"));
                warehouse.setShippingFrom(rs.getString("shippingFrom"));
                warehouse.setShippingStart(rs.getTimestamp("shippingStart"));
                warehouse.setShippingDone(rs.getTimestamp("shippingDone"));
                warehouse.setDescription(rs.getString("description"));
                warehouse.setCreatedDate(rs.getTimestamp("createdDate"));
                warehouse.setCreatedBy(rs.getString("createdBy"));

                warehouseList.add(warehouse);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return warehouseList;
    }

    public int createWarehouse(WarehouseDTO warehouse) {
        int id = -1;
        String sql = "INSERT INTO warehouses (shippingFrom, shippingStart, shippingDone, description, createdDate, createdBy) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = OpenConnectionUtil.openConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            conn.setAutoCommit(false);
            SetParameterUtil.setParameter(stmt,
                    warehouse.getShippingFrom(),
                    warehouse.getShippingStart(),
                    warehouse.getShippingDone(),
                    warehouse.getDescription(),
                    warehouse.getCreatedDate(),
                    warehouse.getCreatedBy());

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) id = rs.getInt(1);
                rs.close();
            }
            conn.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return id;
    }

    public WarehouseDTO findWarehouseById(int id) {
        String sql = "SELECT id, shippingFrom, shippingStart, shippingDone, description, createdDate, createdBy FROM warehouses WHERE id = ?";
        WarehouseDTO warehouse = null;

        try (Connection conn = OpenConnectionUtil.openConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            SetParameterUtil.setParameter(stmt, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                warehouse = new WarehouseDTO();
                warehouse.setId(rs.getInt("id"));
                warehouse.setShippingFrom(rs.getString("shippingFrom"));
                warehouse.setShippingStart(rs.getTimestamp("shippingStart"));
                warehouse.setShippingDone(rs.getTimestamp("shippingDone"));
                warehouse.setDescription(rs.getString("description"));
                warehouse.setCreatedDate(rs.getTimestamp("createdDate"));
                warehouse.setCreatedBy(rs.getString("createdBy"));
            }
            rs.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return warehouse;
    }

    public int updateWarehouse(WarehouseDTO warehouse) {
        int result = -1;
        String sql = "UPDATE warehouses SET shippingFrom = ?, shippingStart = ?, shippingDone = ?, description = ?, createdDate = ?, createdBy = ? WHERE id = ?";

        try (Connection conn = OpenConnectionUtil.openConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            conn.setAutoCommit(false);
            SetParameterUtil.setParameter(stmt,
                    warehouse.getShippingFrom(),
                    warehouse.getShippingStart(),
                    warehouse.getShippingDone(),
                    warehouse.getDescription(),
                    warehouse.getCreatedDate(),
                    warehouse.getCreatedBy(),
                    warehouse.getId());

            result = stmt.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    public int deleteWarehouse(int id) {
        String sql = "DELETE FROM warehouses WHERE id = ?";

        try (Connection conn = OpenConnectionUtil.openConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            conn.setAutoCommit(false);
            SetParameterUtil.setParameter(stmt, id);
            int rows = stmt.executeUpdate();
            conn.commit();
            return rows;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<WarehouseDTO> getWarehousesDatatable(int start, int length, String columnOrder, String orderDir, String searchValue) {
        List<WarehouseDTO> list = new ArrayList<>();
        String sql = "SELECT id, shippingFrom, shippingStart, shippingDone, description, createdDate, createdBy FROM warehouses";
        if (searchValue != null && !searchValue.isEmpty()) {
            sql += " WHERE (id LIKE ? OR shippingFrom LIKE ? OR shippingStart LIKE ? OR shippingDone LIKE ? OR description LIKE ? OR createdDate LIKE ? OR createdBy LIKE ?)";
        }
        sql += " ORDER BY " + columnOrder + " " + orderDir + " LIMIT ?, ?";

        try (Connection conn = OpenConnectionUtil.openConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            int index = 1;
            if (searchValue != null && !searchValue.isEmpty()) {
                for (int i = 0; i < 7; i++) {
                    stmt.setString(index++, "%" + searchValue + "%");
                }
            }
            stmt.setInt(index++, start);
            stmt.setInt(index, length);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                WarehouseDTO dto = new WarehouseDTO();
                dto.setId(rs.getInt("id"));
                dto.setShippingFrom(rs.getString("shippingFrom"));
                dto.setShippingStart(rs.getTimestamp("shippingStart"));
                dto.setShippingDone(rs.getTimestamp("shippingDone"));
                dto.setDescription(rs.getString("description"));
                dto.setCreatedDate(rs.getTimestamp("createdDate"));
                dto.setCreatedBy(rs.getString("createdBy"));
                list.add(dto);
            }
            rs.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return list;
    }

    public int getRecordsTotal() {
        String sql = "SELECT COUNT(*) FROM warehouses";
        try (Connection conn = OpenConnectionUtil.openConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int getRecordsFiltered(String searchValue) {
        String sql = "SELECT COUNT(*) FROM warehouses";
        if (searchValue != null && !searchValue.isEmpty()) {
            sql += " WHERE (id LIKE ? OR shippingFrom LIKE ? OR shippingStart LIKE ? OR shippingDone LIKE ? OR description LIKE ? OR createdDate LIKE ? OR createdBy LIKE ?)";
        }

        try (Connection conn = OpenConnectionUtil.openConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            if (searchValue != null && !searchValue.isEmpty()) {
                for (int i = 1; i <= 7; i++) {
                    stmt.setString(i, "%" + searchValue + "%");
                }
            }

            ResultSet rs = stmt.executeQuery();
            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
