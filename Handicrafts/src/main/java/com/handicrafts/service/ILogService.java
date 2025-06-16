package com.handicrafts.service;

import com.handicrafts.dto.LogDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ILogService<T> {
    void log(HttpServletRequest request, String function, String state, int level, T previous, T current);

    int save(LogDTO dto);

    LogDTO findById(int id);

    List<LogDTO> findAll();

    LogDTO findById(Integer id);

    void deleteById(Integer id);

    Page<LogDTO> searchLogs(String keyword, int page, int size, String sortField, String sortDir);

    void deleteById(int id);
}
