package com.handicrafts.service.impl;

import com.handicrafts.dto.LogDTO;
import com.handicrafts.dto.UserDTO;
import com.handicrafts.entity.LogEntity;
import com.handicrafts.repository.LogRepository;
import com.handicrafts.service.ILogService;
import com.handicrafts.util.SessionUtil;
import com.handicrafts.util.TransferDataUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

@Service
public class LogServiceImp<T> implements ILogService<T> {

    @Autowired
    private LogRepository logRepository;

    private final ResourceBundle bundle = ResourceBundle.getBundle("log-content");

    @Override
    public void log(HttpServletRequest request, String function, String state, int level, T previous, T current) {
        UserDTO user = (UserDTO) SessionUtil.getInstance().getValue(request, "user");

        String ip = request.getRemoteAddr();
        String national = ""; // nếu cần lấy quốc gia từ IP
        String createdBy = (user != null) ? user.getEmail() : "anonymous";
        int userId = (user != null) ? user.getId() : -1;

        String key = function + "-" + state;
        String content = bundle.containsKey(key) ? bundle.getString(key) : key;
        String address = content + " | userId=" + userId;

        String previousValue = (previous != null) ? new TransferDataUtil<T>().toJson(previous) : null;
        String currentValue = (current != null) ? new TransferDataUtil<T>().toJson(current) : null;

        LogEntity entity = new LogEntity();
        entity.setIp(ip);
        entity.setNational(national);
        entity.setLevel(level);
        entity.setAddress(address);
        entity.setPreviousValue(previousValue);
        entity.setCurrentValue(currentValue);
        entity.setCreatedDate(new Timestamp(System.currentTimeMillis()));
        entity.setCreatedBy(createdBy);

        logRepository.save(entity);
    }

    @Override
    public int save(LogDTO dto) {
        LogEntity entity = toEntity(dto);
        logRepository.save(entity);
        return 1;
    }

    @Override
    public LogDTO findById(Integer id) {
        Optional<LogEntity> entity = logRepository.findById(id);
        return entity.map(this::toDTO).orElse(null);
    }

    @Override
    public List<LogDTO> findAll() {
        return logRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Integer id) {
        logRepository.deleteById(id);
    }

    @Override
    public Page<LogDTO> searchLogs(String keyword, int page, int size, String sortField, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<LogEntity> pageResult = logRepository.searchLogs(keyword.toLowerCase(), pageable);
        List<LogDTO> dtos = pageResult.getContent().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());

        return new PageImpl<>(dtos, pageable, pageResult.getTotalElements());
    }

    // ========== Mapping ==========
    private LogDTO toDTO(LogEntity entity) {
        LogDTO dto = new LogDTO();
        dto.setId(entity.getId());
        dto.setIp(entity.getIp());
        dto.setNational(entity.getNational());
        dto.setLevel(entity.getLevel());
        dto.setAddress(entity.getAddress());
        dto.setPreviousValue(entity.getPreviousValue());
        dto.setCurrentValue(entity.getCurrentValue());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setCreatedBy(entity.getCreatedBy());
        return dto;
    }

    private LogEntity toEntity(LogDTO dto) {
        LogEntity entity = new LogEntity();
        entity.setId(dto.getId());
        entity.setIp(dto.getIp());
        entity.setNational(dto.getNational());
        entity.setLevel(dto.getLevel());
        entity.setAddress(dto.getAddress());
        entity.setPreviousValue(dto.getPreviousValue());
        entity.setCurrentValue(dto.getCurrentValue());
        entity.setCreatedDate(dto.getCreatedDate());
        entity.setCreatedBy(dto.getCreatedBy());
        return entity;
    }
}
