package com.handicrafts.repository;

import com.handicrafts.dto.LogDTO;
import com.handicrafts.util.OpenConnectionUtil;
import com.handicrafts.util.SetParameterUtil;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class LogRepository {

    public int save(LogDTO log) {
        String sql = "INSERT INTO logs (ip, national, level, address, previousValue, currentValue, createdDate, createdBy) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = OpenConnectionUtil.openConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            conn.setAutoCommit(false);
            SetParameterUtil.setParameter(stmt,
                    log.getIp(),
                    log.getNational(),
                    log.getLevel(),
                    log.getAddress(),
                    log.getPreviousValue(),
                    log.getCurrentValue(),
                    log.getCreatedDate(),
                    log.getCreatedBy());

            int rows = stmt.executeUpdate();
            conn.commit();
            if (rows > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public LogDTO findById(int id) {
        String sql = "SELECT * FROM logs WHERE id = ?";
        try (Connection conn = OpenConnectionUtil.openConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            SetParameterUtil.setParameter(stmt, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return mapToDTO(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<LogDTO> findAll() {
        List<LogDTO> logs = new ArrayList<>();
        String sql = "SELECT * FROM logs";
        try (Connection conn = OpenConnectionUtil.openConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                logs.add(mapToDTO(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return logs;
    }

    public void deleteById(int id) {
        String sql = "DELETE FROM logs WHERE id = ?";
        try (Connection conn = OpenConnectionUtil.openConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            SetParameterUtil.setParameter(stmt, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private LogDTO mapToDTO(ResultSet rs) throws SQLException {
        LogDTO dto = new LogDTO();
        dto.setId(rs.getInt("id"));
        dto.setIp(rs.getString("ip"));
        dto.setNational(rs.getString("national"));
        dto.setLevel(rs.getInt("level"));
        dto.setAddress(rs.getString("address"));
        dto.setPreviousValue(rs.getString("previousValue"));
        dto.setCurrentValue(rs.getString("currentValue"));
        dto.setCreatedDate(rs.getTimestamp("createdDate"));
        dto.setCreatedBy(rs.getString("createdBy"));
        return dto;
    }
}
