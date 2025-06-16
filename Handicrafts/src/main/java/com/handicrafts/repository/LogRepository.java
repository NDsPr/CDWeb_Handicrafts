package com.handicrafts.repository;

import com.handicrafts.entity.LogEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LogRepository extends JpaRepository<LogEntity, Integer> {

    // Tìm kiếm toàn văn trên các trường
    @Query("SELECT l FROM LogEntity l WHERE " +
            "CAST(l.id AS string) LIKE %:keyword% OR " +
            "LOWER(l.ip) LIKE %:keyword% OR " +
            "LOWER(l.national) LIKE %:keyword% OR " +
            "CAST(l.level AS string) LIKE %:keyword% OR " +
            "LOWER(l.address) LIKE %:keyword% OR " +
            "LOWER(l.previousValue) LIKE %:keyword% OR " +
            "LOWER(l.currentValue) LIKE %:keyword% OR " +
            "LOWER(l.createdBy) LIKE %:keyword%")
    Page<LogEntity> searchLogs(String keyword, Pageable pageable);
}
