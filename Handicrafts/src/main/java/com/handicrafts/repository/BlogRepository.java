package com.handicrafts.repository;

import com.ltw.bean.BlogBean;
import com.ltw.util.CloseResourceUtil;
import com.ltw.util.OpenConnectionUtil;
import com.ltw.util.SetParameterUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BlogRepository {

    public BlogBean findBlogById(int id) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT id, author, title, profilePic, content, categoryId, createdDate ")
                .append("FROM blogs ")
                .append("WHERE id = ?");

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        BlogBean blogBean = null;
        try {
            connection = OpenConnectionUtil.openConnection();
            preparedStatement = connection.prepareStatement(sql.toString());
            SetParameterUtil.setParameter(preparedStatement, id);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                blogBean = new BlogBean();
                blogBean.setId(resultSet.getInt("id"));
                blogBean.setAuthor(resultSet.getString("author"));
                blogBean.setTitle(resultSet.getString("title"));
                blogBean.setProfilePic(resultSet.getString("profilePic"));
                blogBean.setContent(resultSet.getString("content"));
                blogBean.setCategoryId(resultSet.getInt("categoryId"));
                blogBean.setCreatedDate(resultSet.getTimestamp("createdDate"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            CloseResourceUtil.closeResource(resultSet, preparedStatement, connection);
        }
        return blogBean;
    }

    // TODO: Thay đổi lại câu SQL khi thêm status trong bảng Category
    public List<BlogBean> findThreeBlogs() {
        List<BlogBean> result = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT id, title, author, categoryId, createdDate ")
                .append("FROM blogs ")
                .append("WHERE status = 1 ")
                .append("LIMIT 3");

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = OpenConnectionUtil.openConnection();
            preparedStatement = connection.prepareStatement(sql.toString());
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                BlogBean blogBean = new BlogBean();
                blogBean.setId(resultSet.getInt("id"));
                blogBean.setTitle(resultSet.getString("title"));
                blogBean.setAuthor(resultSet.getString("author"));
                blogBean.setCategoryId(resultSet.getInt("categoryId"));
                blogBean.setCreatedDate(resultSet.getTimestamp("createdDate"));

                result.add(blogBean);
            }
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            CloseResourceUtil.closeResource(resultSet, preparedStatement, connection);
        }
    }

    public List<BlogBean> findAllBlogs() {
        List<BlogBean> result = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT id, title, author, categoryId, createdDate ")
                .append("FROM blogs ")
                .append("WHERE status = 1 ");

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = OpenConnectionUtil.openConnection();
            preparedStatement = connection.prepareStatement(sql.toString());
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                BlogBean blogBean = new BlogBean();
                blogBean.setId(resultSet.getInt("id"));
                blogBean.setTitle(resultSet.getString("title"));
                blogBean.setAuthor(resultSet.getString("author"));
                blogBean.setCategoryId(resultSet.getInt("categoryId"));
                blogBean.setCreatedDate(resultSet.getTimestamp("createdDate"));

                result.add(blogBean);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            CloseResourceUtil.closeResource(resultSet, preparedStatement, connection);
        }
        return result;
    }

    public void createBlog(BlogBean blogBean) {
        String sql = "INSERT INTO blogs(title,author, description, content, categoryID, status,createdDate, createdBy) " +
                "VALUES (?,?, ?, ?, ?, ?,?,?,)";

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = OpenConnectionUtil.openConnection();
            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement(sql);
            SetParameterUtil.setParameter(preparedStatement, blogBean.getTitle(),blogBean.getAuthor(), blogBean.getDescription(),
                    blogBean.getContent(), blogBean.getCategoryId(), blogBean.getStatus());
            preparedStatement.executeUpdate();
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
    }


    public List<BlogBean> getBlogsDatatable(int start, int length, String columnOrder, String orderDir, String searchValue) {
        List<BlogBean> blogs = new ArrayList<>();
        String sql = "SELECT id, title, author, description, content, categoryId, status, profilePic, createdDate, createdBy, modifiedDate,modifiedBy FROM blogs";
        int index = 1;

        Connection conn = null;
        PreparedStatement preStat = null;
        ResultSet rs = null;

        try {
            conn = OpenConnectionUtil.openConnection();
            if (searchValue != null && !searchValue.isEmpty()) {
                sql += " WHERE (id LIKE ? OR title LIKE ? OR author LIKE ? OR description LIKE ? OR content LIKE ? OR categoryId LIKE ? " +
                        "OR status LIKE ? OR profilePic LIKE ? OR createdDate LIKE ? OR createdBy LIKE ? OR modifiedDate LIKE ? OR modifiedBy LIKE ?)";
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
                preStat.setString(index++, "%" + searchValue + "%");
                preStat.setString(index++, "%" + searchValue + "%");
            }
            preStat.setInt(index++, start);
            preStat.setInt(index, length);

            rs = preStat.executeQuery();
            while (rs.next()) {
                BlogBean blog = new BlogBean();
                blog.setId(rs.getInt("id"));
                blog.setTitle(rs.getString("title"));
                blog.setAuthor(rs.getString("author"));
                blog.setDescription(rs.getString("description"));
                blog.setContent(rs.getString("content"));
                blog.setCategoryId(rs.getInt("categoryId"));
                blog.setStatus(rs.getInt("status"));
                blog.setProfilePic(rs.getString("profilePic"));
                blog.setCreatedDate(rs.getTimestamp("createdDate"));
                blog.setCreatedBy(rs.getString("createdBy"));
                blog.setModifiedDate(rs.getTimestamp("modifiedDate"));
                blog.setModifiedBy(rs.getString("modifiedBy"));

                blogs.add(blog);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            CloseResourceUtil.closeResource(rs, preStat, conn);
        }
        return blogs;
    }

    public int getRecordsTotal() {
        int recordsTotal = -1;
        String sql = "SELECT COUNT(id) FROM blogs";

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
    String sql = "SELECT COUNT(id) FROM blogs";

    Connection conn = null;
    PreparedStatement preStat = null;
    ResultSet rs = null;

        try {
        conn = OpenConnectionUtil.openConnection();
        if (searchValue != null && !searchValue.isEmpty()) {
            sql += " WHERE (id LIKE ? OR title LIKE ? OR author LIKE ? OR description LIKE ? OR content LIKE ? OR categoryId LIKE ? " +
                    "OR status LIKE ? OR profilePic LIKE ? OR createDate LIKE ? OR createBy LIKE ? OR modifiedDate LIKE ? OR modifiedBy LIKE ?)";
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
    public int deleteBlog(int id) {
        int affectRows;
        String sql = "DELETE FROM blogs WHERE id = ?";

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
    public int updateBlog(BlogBean blog) {
        int affectedRows = -1;
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE blogs ")
                .append("SET title = ?, author = ?, description = ?, ")
                .append("content = ?, profilePic = ? ")
                .append("WHERE id = ?");

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = OpenConnectionUtil.openConnection();
            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement(sql.toString());
            SetParameterUtil.setParameter(preparedStatement, blog.getTitle(), blog.getAuthor(),
                    blog.getDescription(), blog.getContent(), blog.getProfilePic(), blog.getId());
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

}