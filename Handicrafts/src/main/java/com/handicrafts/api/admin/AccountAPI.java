package com.handicrafts.api.admin;

import com.handicrafts.dto.DatatableDTO;
import com.handicrafts.dto.UserDTO;
import com.handicrafts.entity.UserEntity;
import com.handicrafts.repository.UserRepository;
import com.handicrafts.util.SendEmailUtil;
import com.handicrafts.util.SessionUtil;
import com.handicrafts.util.TransferDataUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin/account")
@RequiredArgsConstructor
public class AccountAPI {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @PostMapping
    public ResponseEntity<?> getAccounts(HttpServletRequest req) {
        int draw = Integer.parseInt(req.getParameter("draw"));
        int start = Integer.parseInt(req.getParameter("start"));
        int length = Integer.parseInt(req.getParameter("length"));
        String searchValue = req.getParameter("search[value]");
        String orderBy = req.getParameter("order[0][column]");
        String orderDir = req.getParameter("order[0][dir]");
        String columnOrder = req.getParameter("columns[" + orderBy + "][data]");

        Pageable pageable = PageRequest.of(start / length, length);
        List<UserEntity> entities = userRepository.getUsersDatatable(searchValue, columnOrder, orderDir, pageable);
        List<UserDTO> users = entities.stream()
                .map(user -> modelMapper.map(user, UserDTO.class))
                .collect(Collectors.toList());

        int recordsTotal = userRepository.getRecordsTotal();
        int recordsFiltered = userRepository.getRecordsFiltered(searchValue);
        draw++;

        DatatableDTO<UserDTO> userDatatableDTO = new DatatableDTO<>(users, recordsTotal, recordsFiltered, draw);
        return ResponseEntity.ok(userDatatableDTO);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteAccount(@RequestParam("id") Integer id, HttpServletRequest req) {
        Optional<UserEntity> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.badRequest().body("{\"status\": \"error\", \"notify\": \"Người dùng không tồn tại!\"}");
        }

        UserEntity prevUser = optionalUser.get();
        try {
            userRepository.deleteById(id);

            // Lấy user đang đăng nhập để gửi email
            UserDTO currentUser = (UserDTO) SessionUtil.getInstance().getValue(req, "user");
            SendEmailUtil.sendDeleteNotify(currentUser.getId(), currentUser.getEmail(), prevUser.getId(), "User");

            return ResponseEntity.ok("{\"status\": \"success\", \"notify\": \"Xóa người dùng thành công!\"}");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("{\"status\": \"error\", \"notify\": \"Có lỗi khi xóa người dùng!\"}");
        }
    }
}
