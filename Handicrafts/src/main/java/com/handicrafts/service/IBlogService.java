package com.handicrafts.service;

import com.handicrafts.dto.BlogDTO;

import java.util.List;

public interface IBlogService {
    /**
     * Tìm blog theo ID
     */
    BlogDTO findBlogById(int id);

    /**
     * Lấy ba blog mới nhất
     */
    List<BlogDTO> findThreeBlogs();

    /**
     * Lấy tất cả các blog
     */
    List<BlogDTO> findAllBlogs();

    /**
     * Tạo blog mới
     */
    void createBlog(BlogDTO blog);

    /**
     * Cập nhật thông tin blog
     * @return Số lượng bản ghi đã cập nhật
     */
    int updateBlog(BlogDTO blog);

    /**
     * Xóa blog theo ID
     * @return Số lượng bản ghi đã xóa
     */
    int deleteBlog(int id);

    /**
     * Lấy danh sách blog cho DataTable với phân trang và sắp xếp
     */
    List<BlogDTO> getBlogsDatatable(int start, int length, String columnOrder, String orderDir, String searchValue);

    /**
     * Đếm tổng số blog
     */
    int getRecordsTotal();

    /**
     * Đếm số blog phù hợp với điều kiện tìm kiếm
     */
    int getRecordsFiltered(String searchValue);

    /**
     * Tìm blog theo danh mục với phân trang
     */
    List<BlogDTO> findBlogsByCategoryId(int categoryId, int page, int size);

    /**
     * Đếm số blog trong một danh mục
     */
    int countBlogsByCategoryId(int categoryId);

    /**
     * Tìm các blog liên quan đến một blog cụ thể
     */
    List<BlogDTO> findRelatedBlogs(int blogId, int categoryId, int limit);

    /**
     * Tìm các blog mới nhất
     */
    List<BlogDTO> findLatestBlogs(int limit);

    /**
     * Tìm các blog phổ biến nhất (dựa trên lượt xem)
     */
    List<BlogDTO> findPopularBlogs(int limit);

    /**
     * Tăng số lượt xem của blog
     */
    void incrementViewCount(int blogId);

    /**
     * Tìm kiếm blog theo từ khóa với phân trang
     */
    List<BlogDTO> searchBlogs(String keyword, int page, int size);

    /**
     * Đếm số kết quả tìm kiếm
     */
    int countSearchResults(String keyword);
}
