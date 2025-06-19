package com.handicrafts.service.impl;

import com.handicrafts.dto.BlogDTO;
import com.handicrafts.repository.BlogRepository;
import com.handicrafts.service.IBlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class BlogServiceImpl implements IBlogService {

    private final BlogRepository blogRepository;

    @Autowired
    public BlogServiceImpl(BlogRepository blogRepository) {
        this.blogRepository = blogRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public BlogDTO findBlogById(int id) {
        return blogRepository.findBlogById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BlogDTO> findThreeBlogs() {
        return blogRepository.findThreeBlogs();
    }

    @Override
    @Transactional(readOnly = true)
    public List<BlogDTO> findAllBlogs() {
        return blogRepository.findAllBlogs();
    }

    @Override
    public void createBlog(BlogDTO blog) {
        // Thiết lập các giá trị mặc định nếu cần
        if (blog.getCreatedDate() == null) {
            blog.setCreatedDate(new Timestamp(System.currentTimeMillis()));
        }
        // Kiểm tra status bằng cách khác, không dùng null check
        // Giả sử 0 là giá trị mặc định chưa được thiết lập
        if (blog.getStatus() == 0) {
            blog.setStatus(1); // Giả sử 1 là trạng thái hoạt động
        }

        blogRepository.createBlog(blog);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BlogDTO> getBlogsDatatable(int start, int length, String columnOrder, String orderDir, String searchValue) {
        return blogRepository.getBlogsDatatable(start, length, columnOrder, orderDir, searchValue);
    }

    @Override
    @Transactional(readOnly = true)
    public int getRecordsTotal() {
        return blogRepository.getRecordsTotal();
    }

    @Override
    @Transactional(readOnly = true)
    public int getRecordsFiltered(String searchValue) {
        return blogRepository.getRecordsFiltered(searchValue);
    }

    @Override
    public int deleteBlog(int id) {
        return blogRepository.deleteBlog(id);
    }

    @Override
    public int updateBlog(BlogDTO blog) {
        // Thiết lập thời gian chỉnh sửa
        blog.setModifiedDate(new Timestamp(System.currentTimeMillis()));
        return blogRepository.updateBlog(blog);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BlogDTO> findBlogsByCategoryId(int categoryId, int page, int size) {
        int start = page * size;
        return blogRepository.findBlogsByCategoryId(categoryId, start, size);
    }

    @Override
    @Transactional(readOnly = true)
    public int countBlogsByCategoryId(int categoryId) {
        return blogRepository.countBlogsByCategoryId(categoryId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BlogDTO> findRelatedBlogs(int blogId, int categoryId, int limit) {
        return blogRepository.findRelatedBlogs(blogId, categoryId, limit);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BlogDTO> findLatestBlogs(int limit) {
        return blogRepository.findLatestBlogs(limit);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BlogDTO> findPopularBlogs(int limit) {
        return blogRepository.findPopularBlogs(limit);
    }

    @Override
    public void incrementViewCount(int blogId) {
        blogRepository.incrementViewCount(blogId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BlogDTO> searchBlogs(String keyword, int page, int size) {
        int start = page * size;
        return blogRepository.searchBlogs(keyword, start, size);
    }

    @Override
    @Transactional(readOnly = true)
    public int countSearchResults(String keyword) {
        return blogRepository.countSearchResults(keyword);
    }
}
