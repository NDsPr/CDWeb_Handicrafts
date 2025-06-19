package com.handicrafts.repository;

import com.handicrafts.dto.WarehouseDetailDTO;
import com.handicrafts.util.CloseResourceUtil;
import com.handicrafts.util.OpenConnectionUtil;
import com.handicrafts.util.SetParameterUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class WarehouseDetailRepository {
    private static final Logger logger = LoggerFactory.getLogger(WarehouseDetailRepository.class);

    private final OpenConnectionUtil openConnectionUtil;

    @Autowired
    public WarehouseDetailRepository(OpenConnectionUtil openConnectionUtil) {
        this.openConnectionUtil = openConnectionUtil;
    }

    public List<WarehouseDetailDTO> findWarehouseDetailByWarehouseId(int warehouseId) {
        String sql = "SELECT id, warehouseId, productId, quantity FROM warehouse_details WHERE warehouseId = ?";
        List<WarehouseDetailDTO> detailList = new ArrayList<>();

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = openConnectionUtil.openConnection();
            stmt = conn.prepareStatement(sql);
            SetParameterUtil.setParameter(stmt, warehouseId);
            rs = stmt.executeQuery();

            while (rs.next()) {
                WarehouseDetailDTO dto = new WarehouseDetailDTO();
                dto.setId(rs.getInt("id"));
                dto.setWarehouseId(rs.getInt("warehouseId"));
                dto.setProductId(rs.getInt("productId"));
                dto.setQuantity(rs.getInt("quantity"));
                detailList.add(dto);
            }
        } catch (SQLException e) {
            logger.error("Error finding warehouse details for warehouseId: {}", warehouseId, e);
            throw new RuntimeException(e);
        } finally {
            CloseResourceUtil.closeResource(rs, stmt, conn);
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

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = openConnectionUtil.openConnection();
            stmt = conn.prepareStatement(sql);

            int index = 1;
            stmt.setInt(index++, warehouseId);
            if (searchValue != null && !searchValue.isEmpty()) {
                String searchPattern = "%" + searchValue + "%";
                for (int i = 0; i < 4; i++) {
                    stmt.setString(index++, searchPattern);
                }
            }
            stmt.setInt(index++, start);
            stmt.setInt(index, length);

            rs = stmt.executeQuery();
            while (rs.next()) {
                WarehouseDetailDTO dto = new WarehouseDetailDTO();
                dto.setId(rs.getInt("id"));
                dto.setWarehouseId(rs.getInt("warehouseId"));
                dto.setProductId(rs.getInt("productId"));
                dto.setQuantity(rs.getInt("quantity"));
                detailList.add(dto);
            }
        } catch (SQLException e) {
            logger.error("Error getting warehouse details datatable. WarehouseId: {}, Start: {}, Length: {}, Order: {}, Direction: {}, Search: {}",
                    warehouseId, start, length, columnOrder, orderDir, searchValue, e);
            throw new RuntimeException(e);
        } finally {
            CloseResourceUtil.closeResource(rs, stmt, conn);
        }
        return detailList;
    }

    public int getRecordsTotal() {
        String sql = "SELECT COUNT(id) AS count FROM warehouse_details";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = openConnectionUtil.openConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("count");
            }
        } catch (SQLException e) {
            logger.error("Error getting total records count", e);
            throw new RuntimeException(e);
        } finally {
            CloseResourceUtil.closeResource(rs, stmt, conn);
        }
        return -1;
    }

    public int getRecordsFiltered(int warehouseId, String searchValue) {
        String sql = "SELECT COUNT(id) AS count FROM warehouse_details WHERE warehouseId = ?";

        if (searchValue != null && !searchValue.isEmpty()) {
            sql += " AND (id LIKE ? OR warehouseId LIKE ? OR productId LIKE ? OR quantity LIKE ?)";
        }

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = openConnectionUtil.openConnection();
            stmt = conn.prepareStatement(sql);

            int index = 1;
            stmt.setInt(index++, warehouseId);
            if (searchValue != null && !searchValue.isEmpty()) {
                String searchPattern = "%" + searchValue + "%";
                for (int i = 0; i < 4; i++) {
                    stmt.setString(index++, searchPattern);
                }
            }

            rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("count");
            }
        } catch (SQLException e) {
            logger.error("Error getting filtered records count. WarehouseId: {}, Search: {}", warehouseId, searchValue, e);
            throw new RuntimeException(e);
        } finally {
            CloseResourceUtil.closeResource(rs, stmt, conn);
        }
        return -1;
    }

    public WarehouseDetailDTO findById(int id) {
        String sql = "SELECT id, warehouseId, productId, quantity FROM warehouse_details WHERE id = ?";
        WarehouseDetailDTO dto = null;

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = openConnectionUtil.openConnection();
            stmt = conn.prepareStatement(sql);
            SetParameterUtil.setParameter(stmt, id);
            rs = stmt.executeQuery();

            if (rs.next()) {
                dto = new WarehouseDetailDTO();
                dto.setId(rs.getInt("id"));
                dto.setWarehouseId(rs.getInt("warehouseId"));
                dto.setProductId(rs.getInt("productId"));
                dto.setQuantity(rs.getInt("quantity"));
            }
        } catch (SQLException e) {
            logger.error("Error finding warehouse detail by ID: {}", id, e);
            throw new RuntimeException(e);
        } finally {
            CloseResourceUtil.closeResource(rs, stmt, conn);
        }
        return dto;
    }

    public int create(WarehouseDetailDTO dto) {
        String sql = "INSERT INTO warehouse_details (warehouseId, productId, quantity) VALUES (?, ?, ?)";
        int generatedId = -1;

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = openConnectionUtil.openConnection();
            conn.setAutoCommit(false);
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            SetParameterUtil.setParameter(stmt,
                    dto.getWarehouseId(),
                    dto.getProductId(),
                    dto.getQuantity());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    generatedId = rs.getInt(1);
                }
            }
            conn.commit();
        } catch (SQLException e) {
            logger.error("Error creating warehouse detail: {}", dto, e);
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ex) {
                logger.error("Error rolling back transaction", ex);
            }
            throw new RuntimeException(e);
        } finally {
            CloseResourceUtil.closeResource(rs, stmt, conn);
        }
        return generatedId;
    }

    public int update(WarehouseDetailDTO dto) {
        String sql = "UPDATE warehouse_details SET warehouseId = ?, productId = ?, quantity = ? WHERE id = ?";
        int affectedRows = -1;

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = openConnectionUtil.openConnection();
            conn.setAutoCommit(false);
            stmt = conn.prepareStatement(sql);

            SetParameterUtil.setParameter(stmt,
                    dto.getWarehouseId(),
                    dto.getProductId(),
                    dto.getQuantity(),
                    dto.getId());

            affectedRows = stmt.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            logger.error("Error updating warehouse detail with ID: {}", dto.getId(), e);
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ex) {
                logger.error("Error rolling back transaction", ex);
            }
            throw new RuntimeException(e);
        } finally {
            CloseResourceUtil.closeNotUseRS(stmt, conn);
        }
        return affectedRows;
    }

    public int delete(int id) {
        String sql = "DELETE FROM warehouse_details WHERE id = ?";
        int affectedRows = -1;

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = openConnectionUtil.openConnection();
            conn.setAutoCommit(false);
            stmt = conn.prepareStatement(sql);

            SetParameterUtil.setParameter(stmt, id);
            affectedRows = stmt.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            logger.error("Error deleting warehouse detail with ID: {}", id, e);
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ex) {
                logger.error("Error rolling back transaction", ex);
            }
            throw new RuntimeException(e);
        } finally {
            CloseResourceUtil.closeNotUseRS(stmt, conn);
        }
        return affectedRows;
    }

    public WarehouseDetailDTO findByWarehouseIdAndProductId(int warehouseId, int productId) {
        String sql = "SELECT id, warehouseId, productId, quantity FROM warehouse_details WHERE warehouseId = ? AND productId = ?";
        WarehouseDetailDTO dto = null;

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = openConnectionUtil.openConnection();
            stmt = conn.prepareStatement(sql);
            SetParameterUtil.setParameter(stmt, warehouseId, productId);
            rs = stmt.executeQuery();

            if (rs.next()) {
                dto = new WarehouseDetailDTO();
                dto.setId(rs.getInt("id"));
                dto.setWarehouseId(rs.getInt("warehouseId"));
                dto.setProductId(rs.getInt("productId"));
                dto.setQuantity(rs.getInt("quantity"));
            }
        } catch (SQLException e) {
            logger.error("Error finding warehouse detail by warehouseId: {} and productId: {}", warehouseId, productId, e);
            throw new RuntimeException(e);
        } finally {
            CloseResourceUtil.closeResource(rs, stmt, conn);
        }
        return dto;
    }

    public int updateQuantity(int warehouseId, int productId, int quantity) {
        String sql = "UPDATE warehouse_details SET quantity = ? WHERE warehouseId = ? AND productId = ?";
        int affectedRows = -1;

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = openConnectionUtil.openConnection();
            conn.setAutoCommit(false);
            stmt = conn.prepareStatement(sql);

            SetParameterUtil.setParameter(stmt, quantity, warehouseId, productId);
            affectedRows = stmt.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            logger.error("Error updating quantity for warehouseId: {} and productId: {}", warehouseId, productId, e);
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ex) {
                logger.error("Error rolling back transaction", ex);
            }
            throw new RuntimeException(e);
        } finally {
            CloseResourceUtil.closeNotUseRS(stmt, conn);
        }
        return affectedRows;
    }
}
