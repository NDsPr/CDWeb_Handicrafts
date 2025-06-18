package com.handicrafts.service;

import com.handicrafts.dto.BlogDTO;

import java.util.List;

public interface IBlogService {
    BlogDTO findBlogById(int id);
    List<BlogDTO> findThreeBlogs();
    List<BlogDTO> findAllBlogs();
    void createBlog(BlogDTO blog);
    int updateBlog(BlogDTO blog);
    int deleteBlog(int id);
    List<BlogDTO> getBlogsDatatable(int start, int length, String columnOrder, String orderDir, String searchValue);
    int getRecordsTotal();
    int getRecordsFiltered(String searchValue);
}
