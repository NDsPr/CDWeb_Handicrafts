package com.handicrafts.repository;

import com.handicrafts.dto.LogDTO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ILogRepository extends IRepositoryNonUpdate<LogDTO> {

    /**
     * Lấy dữ liệu log cho datatable với phân trang và tìm kiếm
     *
     * @param start vị trí bắt đầu
     * @param length số lượng bản ghi
     * @param columnOrder cột để sắp xếp
     * @param orderDir hướng sắp xếp
     * @param searchValue giá trị tìm kiếm
     * @return danh sách LogDTO
     */
    List<LogDTO> getUsersDatatable(int start, int length, String columnOrder, String orderDir, String searchValue);

    /**
     * Lấy tổng số bản ghi logs
     *
     * @return tổng số bản ghi
     */
    int getRecordsTotal();

    /**
     * Lấy số bản ghi sau khi lọc
     *
     * @param searchValue giá trị tìm kiếm
     * @return số bản ghi sau khi lọc
     */
    int getRecordsFiltered(String searchValue);

    /**
     * Xóa log theo id
     *
     * @param id id của log cần xóa
     * @return số bản ghi bị ảnh hưởng
     */
    int deleteLog(int id);
}
