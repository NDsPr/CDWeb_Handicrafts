package com.handicrafts.service.impl;

import com.handicrafts.dto.BlogDTO;
import com.handicrafts.repository.BlogRepository;
import com.handicrafts.service.IBlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlogServiceImpl implements IBlogService {

    @Autowired
    private BlogRepository blogRepository;

    @Override
    public BlogDTO findBlogById(int id) {
        return blogRepository.findBlogById(id);
    }

    @Override
    public List<BlogDTO> findThreeBlogs() {
        return blogRepository.findThreeBlogs();
    }

    @Override
    public List<BlogDTO> findAllBlogs() {
        return blogRepository.findAllBlogs();
    }

    @Override
    public void createBlog(BlogDTO blog) {
        blogRepository.createBlog(blog);
    }

    @Override
    public List<BlogDTO> getBlogsDatatable(int start, int length, String columnOrder, String orderDir, String searchValue) {
        return blogRepository.getBlogsDatatable(start, length, columnOrder, orderDir, searchValue);
    }

    @Override
    public int getRecordsTotal() {
        return blogRepository.getRecordsTotal();
    }

    @Override
    public int getRecordsFiltered(String searchValue) {
        return blogRepository.getRecordsFiltered(searchValue);
    }

    @Override
    public int deleteBlog(int id) {
        return blogRepository.deleteBlog(id);
    }

    @Override
    public int updateBlog(BlogDTO blog) {
        return blogRepository.updateBlog(blog);
    }
}
