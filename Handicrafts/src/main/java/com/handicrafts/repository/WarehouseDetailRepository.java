package com.handicrafts.repository;

import com.handicrafts.dto.WarehouseDetailDTO;
import com.handicrafts.util.CloseResourceUtil;
import com.handicrafts.util.OpenConnectionUtil;
import com.handicrafts.util.SetParameterUtil;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class WarehouseDetailRepository {

    public List<WarehouseDetailDTO> findWarehouseDetailByWarehouseId(int warehouseId) {
        String sql = "SELECT id, warehouseId, productId, quantity FROM warehouse_details WHERE warehouseId = ?";
        List<WarehouseDetailDTO> detailList = new ArrayList<>();

        try (Connection conn = OpenConnectionUtil.openConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            SetParameterUtil.setParameter(stmt, warehouseId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                WarehouseDetailDTO dto = new WarehouseDetailDTO();
                dto.setId(rs.getInt("id"));
                dto.setWarehouseId(rs.getInt("warehouseId"));
                dto.setProductId(rs.getInt("productId"));
                dto.setQuantity(rs.getInt("quantity"));
                detailList.add(dto);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return detailList;
    }

    public List<WarehouseDetailDTO> getWarehouseDetailsDatatable(int warehouseId, int start, int length, String columnOrder, String orderDir, String searchValue) {
        List<WarehouseDetailDTO> detailList = new ArrayList<>();
        String sql = "SELECT id, warehouseId, productId, quantity FROM warehouse_details WHERE warehouseId = ?";

        if (searchValue != null && !searchValue.isEmpty()) {
            sql += " AND (id LIKE ? OR warehouseId LIKE ? OR productId LIKE ? OR quantity LIKE ?)";
        }
        sql += " ORDER BY " + columnOrder + " " + orderDir + " LIMIT ?, ?";

        try (Connection conn = OpenConnectionUtil.openConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            int index = 1;
            stmt.setInt(index++, warehouseId);
            if (searchValue != null && !searchValue.isEmpty()) {
                for (int i = 0; i < 4; i++) {
                    stmt.setString(index++, "%" + searchValue + "%");
                }
            }
            stmt.setInt(index++, start);
            stmt.setInt(index, length);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                WarehouseDetailDTO dto = new WarehouseDetailDTO();
                dto.setId(rs.getInt("id"));
                dto.setWarehouseId(rs.getInt("warehouseId"));
                dto.setProductId(rs.getInt("productId"));
                dto.setQuantity(rs.getInt("quantity"));
                detailList.add(dto);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return detailList;
    }

    public int getRecordsTotal() {
        String sql = "SELECT COUNT(id) AS count FROM warehouse_details";
        try (Connection conn = OpenConnectionUtil.openConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt("count");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return -1;
    }

    public int getRecordsFiltered(int warehouseId, String searchValue) {
        String sql = "SELECT COUNT(id) AS count FROM warehouse_details WHERE warehouseId = ?";

        if (searchValue != null && !searchValue.isEmpty()) {
            sql += " AND (id LIKE ? OR warehouseId LIKE ? OR productId LIKE ? OR quantity LIKE ?)";
        }

        try (Connection conn = OpenConnectionUtil.openConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            int index = 1;
            stmt.setInt(index++, warehouseId);
            if (searchValue != null && !searchValue.isEmpty()) {
                for (int i = 0; i < 4; i++) {
                    stmt.setString(index++, "%" + searchValue + "%");
                }
            }

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("count");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return -1;
    }
}
