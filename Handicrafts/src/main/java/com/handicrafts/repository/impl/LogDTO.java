package com.handicrafts.repository.impl;

import com.ltw.bean.LogBean;
import com.ltw.dao.intf.IDaoNonUpdate;
import com.ltw.util.CloseResourceUtil;
import com.ltw.util.OpenConnectionUtil;
import com.ltw.util.SetParameterUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LogDTO implements IDaoNonUpdate<LogBean> {
    @Override
    public List<LogBean> getAll() {
        return null;
    }

    @Override
    public int create(LogBean logBean) {
        int record = -1;
        String sql = "INSERT INTO logs (ip, national, level, address, " +
                "previousValue, currentValue, createdDate) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = OpenConnectionUtil.openConnection();
            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement(sql);
            SetParameterUtil.setParameter(preparedStatement, logBean.getIp(), logBean.getNational(), logBean.getLevel(),
                    logBean.getAddress(), logBean.getPreviousValue(), logBean.getCurrentValue(), logBean.getCreatedDate());
            record = preparedStatement.executeUpdate();
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
        return record;
    }

    @Override
    public int delete(int[] ids) {
        return 0;
    }

    public List<LogBean> getUsersDatatable(int start, int length, String columnOrder, String orderDir, String searchValue) {
        List<LogBean> logs = new ArrayList<>();
        String sql = "SELECT id, ip, national, level, address, previousValue, currentValue, createdDate, createdBy " +
                "FROM logs ";

        Connection conn = null;
        PreparedStatement preStat = null;
        ResultSet rs = null;

        try {
            conn = OpenConnectionUtil.openConnection();
            if (searchValue != null && !searchValue.isEmpty()) {
                sql += " WHERE (id LIKE ? OR ip LIKE ? OR national LIKE ? OR level LIKE ? OR address LIKE ? " +
                        "OR previousValue LIKE ? OR currentValue LIKE ? OR createdDate LIKE ? OR createdBy LIKE ?)";
            }
            sql += " ORDER BY " + columnOrder + " " + orderDir + " ";
            sql += "LIMIT ?, ?";

            int index = 1;

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
            }
            preStat.setInt(index++, start);
            preStat.setInt(index, length);

            rs = preStat.executeQuery();
            while (rs.next()) {
                LogBean log = new LogBean();
                log.setId(rs.getInt("id"));
                log.setIp(rs.getString("ip"));
                log.setNational(rs.getString("national"));
                log.setLevel(rs.getInt("level"));
                log.setAddress(rs.getString("address"));
                log.setPreviousValue(rs.getString("previousValue"));
                log.setCurrentValue(rs.getString("currentValue"));
                log.setCreatedDate(rs.getTimestamp("createdDate"));
                log.setCreatedBy(rs.getString("createdBy"));
                logs.add(log);
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        } finally {
            CloseResourceUtil.closeResource(rs, preStat, conn);
        }
        return logs;
    }

    public int getRecordsTotal() {
        int recordsTotal = -1;
        String sql = "SELECT COUNT(id) FROM logs";

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
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        } finally {
            CloseResourceUtil.closeResource(rs, preStat, conn);
        }
        return recordsTotal;
    }

    public int getRecordsFiltered(String searchValue) {
        int recordsFiltered = -1;
        String sql = "SELECT COUNT(id) FROM logs";

        Connection conn = null;
        PreparedStatement preStat = null;
        ResultSet rs = null;

        try {
            conn = OpenConnectionUtil.openConnection();
            if (searchValue != null && !searchValue.isEmpty()) {
                sql += " WHERE (id LIKE ? OR ip LIKE ? OR national LIKE ? OR level LIKE ? OR address LIKE ? " +
                        "OR previousValue LIKE ? OR currentValue LIKE ? OR createdDate LIKE ? OR createdBy LIKE ?)";
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
                preStat.setString(index, "%" + searchValue + "%");
            }
            rs = preStat.executeQuery();
            if (rs.next()) {
                recordsFiltered = rs.getInt(1);
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        } finally {
            CloseResourceUtil.closeResource(rs, preStat, conn);
        }
        return recordsFiltered;
    }

    public int deleteLog(int id) {
        String sql = "DELETE FROM logs WHERE id = ?";
        int result = -1;

        Connection conn = null;
        PreparedStatement preStat = null;

        try {
            conn = OpenConnectionUtil.openConnection();
            conn.setAutoCommit(false);
            preStat = conn.prepareStatement(sql);
            SetParameterUtil.setParameter(preStat, id);
            result = preStat.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            try {
                conn.rollback();
                throw new RuntimeException(e);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        } finally {
            CloseResourceUtil.closeNotUseRS(preStat, conn);
        }
        return result;
    }
}
