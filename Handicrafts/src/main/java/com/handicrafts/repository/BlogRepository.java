package com.handicrafts.repository;

import com.handicrafts.dto.BlogDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class BlogRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public BlogRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<BlogDTO> blogRowMapper = (ResultSet rs, int rowNum) -> {
        BlogDTO blog = new BlogDTO();
        blog.setId(rs.getInt("id"));
        blog.setAuthor(rs.getString("author"));
        blog.setTitle(rs.getString("title"));
        blog.setProfilePic(rs.getString("profilePic"));
        blog.setContent(rs.getString("content"));
        blog.setCategoryId(rs.getInt("categoryId"));
        blog.setCreatedDate(rs.getTimestamp("createdDate"));
        blog.setDescription(rs.getString("description"));
        blog.setStatus(rs.getInt("status"));
        blog.setCreatedBy(rs.getString("createdBy"));
        blog.setModifiedDate(rs.getTimestamp("modifiedDate"));
        blog.setModifiedBy(rs.getString("modifiedBy"));
        return blog;
    };

    public BlogDTO findBlogById(int id) {
        String sql = "SELECT id, author, title, profilePic, content, categoryId, createdDate, " +
                "description, status, createdBy, modifiedDate, modifiedBy " +
                "FROM blogs WHERE id = ?";

        List<BlogDTO> blogs = jdbcTemplate.query(sql, blogRowMapper, id);
        return blogs.isEmpty() ? null : blogs.get(0);
    }

    public List<BlogDTO> findThreeBlogs() {
        String sql = "SELECT id, author, title, profilePic, content, categoryId, createdDate, " +
                "description, status, createdBy, modifiedDate, modifiedBy " +
                "FROM blogs ORDER BY createdDate DESC LIMIT 3";

        return jdbcTemplate.query(sql, blogRowMapper);
    }

    public List<BlogDTO> findAllBlogs() {
        String sql = "SELECT id, author, title, profilePic, content, categoryId, createdDate, " +
                "description, status, createdBy, modifiedDate, modifiedBy " +
                "FROM blogs ORDER BY createdDate DESC";

        return jdbcTemplate.query(sql, blogRowMapper);
    }

    public List<BlogDTO> getBlogsDatatable(int start, int length, String columnOrder, String orderDir, String searchValue) {
        StringBuilder sqlBuilder = new StringBuilder(
                "SELECT b.id, b.title, b.author, b.description, b.status, b.createdDate, b.createdBy, " +
                        "b.modifiedDate, b.modifiedBy FROM blogs b ");

        if (searchValue != null && !searchValue.isEmpty()) {
            sqlBuilder.append("WHERE b.title LIKE ? OR b.author LIKE ? OR b.description LIKE ? ");
        }

        if (columnOrder != null && !columnOrder.isEmpty()) {
            sqlBuilder.append("ORDER BY b.").append(columnOrder).append(" ").append(orderDir);
        } else {
            sqlBuilder.append("ORDER BY b.id DESC");
        }

        sqlBuilder.append(" LIMIT ? OFFSET ?");
        String sql = sqlBuilder.toString();

        if (searchValue != null && !searchValue.isEmpty()) {
            String likePattern = "%" + searchValue + "%";
            return jdbcTemplate.query(sql,
                    (rs, rowNum) -> {
                        BlogDTO blog = new BlogDTO();
                        blog.setId(rs.getInt("id"));
                        blog.setTitle(rs.getString("title"));
                        blog.setAuthor(rs.getString("author"));
                        blog.setDescription(rs.getString("description"));
                        blog.setStatus(rs.getInt("status"));
                        blog.setCreatedDate(rs.getTimestamp("createdDate"));
                        blog.setCreatedBy(rs.getString("createdBy"));
                        blog.setModifiedDate(rs.getTimestamp("modifiedDate"));
                        blog.setModifiedBy(rs.getString("modifiedBy"));
                        return blog;
                    },
                    likePattern, likePattern, likePattern, length, start);
        } else {
            return jdbcTemplate.query(sql,
                    (rs, rowNum) -> {
                        BlogDTO blog = new BlogDTO();
                        blog.setId(rs.getInt("id"));
                        blog.setTitle(rs.getString("title"));
                        blog.setAuthor(rs.getString("author"));
                        blog.setDescription(rs.getString("description"));
                        blog.setStatus(rs.getInt("status"));
                        blog.setCreatedDate(rs.getTimestamp("createdDate"));
                        blog.setCreatedBy(rs.getString("createdBy"));
                        blog.setModifiedDate(rs.getTimestamp("modifiedDate"));
                        blog.setModifiedBy(rs.getString("modifiedBy"));
                        return blog;
                    },
                    length, start);
        }
    }

    public int getRecordsTotal() {
        String sql = "SELECT COUNT(*) FROM blogs";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    public int getRecordsFiltered(String searchValue) {
        if (searchValue != null && !searchValue.isEmpty()) {
            String sql = "SELECT COUNT(*) FROM blogs WHERE title LIKE ? OR author LIKE ? OR description LIKE ?";
            String likePattern = "%" + searchValue + "%";
            return jdbcTemplate.queryForObject(sql, Integer.class, likePattern, likePattern, likePattern);
        } else {
            return getRecordsTotal();
        }
    }

    public int deleteBlog(int id) {
        String sql = "DELETE FROM blogs WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }

    public int createBlog(BlogDTO blog) {
        String sql = "INSERT INTO blogs (author, title, profilePic, content, categoryId, description, status, createdBy, createdDate) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, NOW())";

        return jdbcTemplate.update(sql,
                blog.getAuthor(),
                blog.getTitle(),
                blog.getProfilePic(),
                blog.getContent(),
                blog.getCategoryId(),
                blog.getDescription(),
                blog.getStatus(),
                blog.getCreatedBy()
        );
    }

    public int updateBlog(BlogDTO blog) {
        String sql = "UPDATE blogs SET author = ?, title = ?, profilePic = ?, content = ?, " +
                "categoryId = ?, description = ?, status = ?, modifiedBy = ?, modifiedDate = NOW() " +
                "WHERE id = ?";

        return jdbcTemplate.update(sql,
                blog.getAuthor(),
                blog.getTitle(),
                blog.getProfilePic(),
                blog.getContent(),
                blog.getCategoryId(),
                blog.getDescription(),
                blog.getStatus(),
                blog.getModifiedBy(),
                blog.getId()
        );
    }

    public List<BlogDTO> findBlogsByCategoryId(int categoryId, int start, int size) {
        String sql = "SELECT id, author, title, profilePic, content, categoryId, createdDate, " +
                "description, status, createdBy, modifiedDate, modifiedBy " +
                "FROM blogs WHERE categoryId = ? ORDER BY createdDate DESC LIMIT ? OFFSET ?";

        return jdbcTemplate.query(sql, blogRowMapper, categoryId, size, start);
    }

    public int countBlogsByCategoryId(int categoryId) {
        String sql = "SELECT COUNT(*) FROM blogs WHERE categoryId = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, categoryId);
    }

    public List<BlogDTO> findRelatedBlogs(int blogId, int categoryId, int limit) {
        String sql = "SELECT id, author, title, profilePic, content, categoryId, createdDate, " +
                "description, status, createdBy, modifiedDate, modifiedBy " +
                "FROM blogs WHERE categoryId = ? AND id != ? ORDER BY createdDate DESC LIMIT ?";

        return jdbcTemplate.query(sql, blogRowMapper, categoryId, blogId, limit);
    }

    public List<BlogDTO> findLatestBlogs(int limit) {
        String sql = "SELECT id, author, title, profilePic, content, categoryId, createdDate, " +
                "description, status, createdBy, modifiedDate, modifiedBy " +
                "FROM blogs ORDER BY createdDate DESC LIMIT ?";

        return jdbcTemplate.query(sql, blogRowMapper, limit);
    }

    public List<BlogDTO> findPopularBlogs(int limit) {
        // Giả sử có cột viewCount để đánh giá mức độ phổ biến
        String sql = "SELECT id, author, title, profilePic, content, categoryId, createdDate, " +
                "description, status, createdBy, modifiedDate, modifiedBy " +
                "FROM blogs ORDER BY viewCount DESC LIMIT ?";

        return jdbcTemplate.query(sql, blogRowMapper, limit);
    }

    public void incrementViewCount(int blogId) {
        // Giả sử có cột viewCount để tăng lượt xem
        String sql = "UPDATE blogs SET viewCount = viewCount + 1 WHERE id = ?";
        jdbcTemplate.update(sql, blogId);
    }

    public List<BlogDTO> searchBlogs(String keyword, int start, int size) {
        String sql = "SELECT id, author, title, profilePic, content, categoryId, createdDate, " +
                "description, status, createdBy, modifiedDate, modifiedBy " +
                "FROM blogs WHERE title LIKE ? OR content LIKE ? OR description LIKE ? " +
                "ORDER BY createdDate DESC LIMIT ? OFFSET ?";

        String likePattern = "%" + keyword + "%";
        return jdbcTemplate.query(sql, blogRowMapper, likePattern, likePattern, likePattern, size, start);
    }

    public int countSearchResults(String keyword) {
        String sql = "SELECT COUNT(*) FROM blogs WHERE title LIKE ? OR content LIKE ? OR description LIKE ?";
        String likePattern = "%" + keyword + "%";
        return jdbcTemplate.queryForObject(sql, Integer.class, likePattern, likePattern, likePattern);
    }
}
