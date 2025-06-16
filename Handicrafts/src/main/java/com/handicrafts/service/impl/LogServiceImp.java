package com.handicrafts.service.impl;

import com.handicrafts.dto.LogDTO;
import com.handicrafts.repository.LogRepository;
import com.handicrafts.service.ILogService;
import com.handicrafts.util.TransferDataUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class LogServiceImp<T> implements ILogService<T> {

    @Autowired
    private LogRepository logRepository;

    @Override
    public void log(HttpServletRequest request, String function, String state, int level, T previousInfo, T currentInfo) {
        String ip = request.getRemoteAddr();
        String address = new TransferDataUtil<String>().toJson(function + "-" + state);
        String previousValue = previousInfo != null ? new TransferDataUtil<T>().toJson(previousInfo) : null;
        String currentValue = currentInfo != null ? new TransferDataUtil<T>().toJson(currentInfo) : null;

        LogDTO logDTO = new LogDTO();
        logDTO.setIp(ip);
        logDTO.setLevel(level);
        logDTO.setAddress(address);
        logDTO.setPreviousValue(previousValue);
        logDTO.setCurrentValue(currentValue);
        logDTO.setCreatedDate(new Timestamp(System.currentTimeMillis()));

        save(logDTO);
    }

    @Override
    public int save(LogDTO dto) {
        return logRepository.save(dto);
    }

    @Override
    public LogDTO findById(int id) {
        return logRepository.findById(id);
    }

    @Override
    public List<LogDTO> findAll() {
        return logRepository.findAll();
    }

    @Override
    public LogDTO findById(Integer id) {
        return null;
    }

    @Override
    public void deleteById(Integer id) {

    }

    @Override
    public Page<LogDTO> searchLogs(String keyword, int page, int size, String sortField, String sortDir) {
        return null;
    }

    @Override
    public void deleteById(int id) {
        logRepository.deleteById(id);
    }
}
