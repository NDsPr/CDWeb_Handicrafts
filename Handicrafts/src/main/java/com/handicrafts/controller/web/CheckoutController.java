package com.handicrafts.controller.web;

import com.handicrafts.dto.*;
import com.handicrafts.constant.LogLevel;
import com.handicrafts.constant.LogState;
import com.handicrafts.dto.FullOrderDTO;
import com.handicrafts.entity.OrderEntity;
import com.handicrafts.entity.UserEntity;
import com.handicrafts.repository.*;
import com.handicrafts.service.ILogService;
import com.handicrafts.util.ValidateParamUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.util.*;

@Controller
@RequestMapping("/checkout")
public class CheckoutController {

    @Autowired
    private CustomizeRepository customizeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ILogService<UserDTO> userLogService;

    @Autowired
    private ILogService<FullOrderDTO> orderLogService;

    private ResourceBundle logBundle = ResourceBundle.getBundle("log-content");

    @GetMapping
    public String doGet(HttpServletRequest request, Model model) {
        CustomizeDTO customizeInfo = customizeRepository.getCustomizeInfo();
        // Kiểm tra số lượng trước khi cho phép checkout
        HttpSession session = request.getSession();
        CartDTO cart = (CartDTO) session.getAttribute("cart");

        if (cart == null || cart.getItems() == null || cart.getItems().isEmpty()) {
            return "redirect:/cart-management";
        }

        List<ItemDTO> items = cart.getItems();

        for (ItemDTO item : items) {
            if (item.getQuantity() > item.getProduct().getQuantity()) {
                return "redirect:/cart-management?error=e&productId=" + item.getProduct().getId()
                        + "&quantity=" + item.getProduct().getQuantity();
            }
        }
        model.addAttribute("customizeInfo", customizeInfo);
        return "checkout";
    }
    @PostMapping
    public String doPost(HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) throws Exception {
        request.setCharacterEncoding("UTF-8");
        String fullName = request.getParameter("fullName");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");
        String paymentMethod = request.getParameter("paymentMethod");

        String[] inputsForm = new String[]{fullName, phone, address};

        // Biến bắt lỗi
        boolean isValid = true;

        // Kiểm tra input rỗng/null trong hàm checkEmptyParam
        List<String> errors = ValidateParamUtil.checkEmptyParam(inputsForm);

        // Nếu có lỗi (khác null) trả về isValid = false
        for (String error : errors) {
            if (error != null) {
                isValid = false;
                break;
            }
        }

        HttpSession session = request.getSession();
        UserDTO user = (UserDTO) session.getAttribute("user");
        CartDTO cart = (CartDTO) session.getAttribute("cart");
        UserEntity prevUser = userRepository.findById(user.getId()).orElse(null);

        if (isValid) {
            // Lưu thông tin của người dùng đã nhập
            UserDTO userDTO = new UserDTO();
            userDTO.setId(user.getId());
            userDTO.setFullName(fullName.trim());
            userDTO.setPhone(phone.trim());
            // Các thông tin khác giữ nguyên từ user hiện tại
            userDTO.setEmail(user.getEmail());
            userDTO.setUsername(user.getUsername());
            userDTO.setBirthDate(user.getBirthDate());
            userDTO.setGender(user.getGender());
            userDTO.setStatus(user.getStatus());
            userDTO.setIsEnable(user.getIsEnable());
            userDTO.setProvider(user.getProvider());
            userDTO.setRoles(user.getRoles());

            // Sửa lại phần gọi updateAccount dựa trên phương thức trong repository
            String[] nameParts = splitFullName(fullName.trim());
            String firstName = nameParts[0];
            String lastName = nameParts[1];

            int affectedRows = userRepository.updateAccount(
                    firstName,
                    lastName,
                    address.trim(), // addressLine
                    "", // addressWard - cần bổ sung hoặc để trống
                    "", // addressDistrict - cần bổ sung hoặc để trống
                    "", // addressProvince - cần bổ sung hoặc để trống
                    user.getId()
            );

            UserEntity currentUser = userRepository.findById(user.getId()).orElse(null);
            if (affectedRows <= 0) {
                userLogService.log(request, "user-update-in-checkout", LogState.FAIL, LogLevel.ALERT, prevUser, currentUser);
            } else {
                userLogService.log(request, "user-update-in-checkout", LogState.SUCCESS, LogLevel.INFO, prevUser, currentUser);
                // Cập nhật lại thông tin user trong session
                session.setAttribute("user", currentUser);
            }

            // Lưu thông tin đơn hàng
            OrderEntity orderEntity = new OrderEntity();
            orderEntity.setUserId(user.getId());
            orderEntity.setTotal(cart.getDiscountPriceTotal());
            if (paymentMethod.equals("paymentOnDelivery")) {
                orderEntity.setPaymentMethod("Thanh toán khi nhận hàng");
            } else {
                orderEntity.setPaymentMethod("Thanh toán chuyển khoản");
            }
            orderEntity.setCreatedDate(new Timestamp(System.currentTimeMillis()));
            orderEntity.setShipToDate(calcShipToDate());
            orderEntity.setStatus(1); // Giả sử 1 là trạng thái mới đặt hàng
            orderEntity.setCreatedBy(user.getEmail());
            orderEntity.setModifiedDate(new Timestamp(System.currentTimeMillis()));
            orderEntity.setModifiedBy(user.getEmail());

            // Sử dụng phương thức save trong OrderRepository
            OrderEntity savedOrder = orderRepository.save(orderEntity);

            if (savedOrder.getId() == null) {
                model.addAttribute("insertError", "ie");
                CustomizeDTO customizeInfo = customizeRepository.getCustomizeInfo();
                model.addAttribute("customizeInfo", customizeInfo);
                return "checkout";
            } else {
                List<OrderDetailDTO> orderDetails = new ArrayList<>();

                for (ItemDTO item : cart.getItems()) {
                    OrderDetailDTO orderDetailDTO = new OrderDetailDTO();
                    orderDetailDTO.setOrderId(savedOrder.getId());
                    orderDetailDTO.setProductId(item.getProduct().getId());
                    orderDetailDTO.setQuantity(item.getQuantity());
                    // Đánh dấu là chưa review cho sản phẩm này
                    orderDetailDTO.setReviewed(0);

                    orderDetails.add(orderDetailDTO);
                    orderDetailRepository.save(orderDetailDTO);

                    int quantityProductAfterOrder = item.getProduct().getQuantity() - item.getQuantity();
                    // Set lại số lượng product
                    productRepository.updateQuantity(item.getProduct().getId(), quantityProductAfterOrder);
                }
                // Ghi log thành công
                FullOrderDTO fullOrder = new FullOrderDTO(orderRepository.findById(savedOrder.getId()).orElse(null), orderDetails);
                orderLogService.log(request, "order-in-checkout", LogState.SUCCESS, LogLevel.INFO, null, fullOrder);
                session.removeAttribute("cart");

                return "redirect:/thankyou";
            }
        } else {
            model.addAttribute("errors", errors);
            // Ghi log thất bại
            orderLogService.log(request, "order-in-checkout", LogState.FAIL, LogLevel.ALERT, null, null);
            CustomizeDTO customizeInfo = customizeRepository.getCustomizeInfo();
            model.addAttribute("customizeInfo", customizeInfo);
            return "checkout";
        }
    }

    // Phương thức hỗ trợ tách họ và tên
    private String[] splitFullName(String fullName) {
        String[] result = new String[2];
        if (fullName == null || fullName.trim().isEmpty()) {
            result[0] = "";
            result[1] = "";
            return result;
        }

        String[] parts = fullName.trim().split("\\s+");
        if (parts.length == 1) {
            result[0] = parts[0];
            result[1] = "";
        } else {
            result[1] = parts[parts.length - 1]; // Tên
            StringBuilder firstName = new StringBuilder();
            for (int i = 0; i < parts.length - 1; i++) {
                firstName.append(parts[i]);
                if (i < parts.length - 2) {
                    firstName.append(" ");
                }
            }
            result[0] = firstName.toString(); // Họ
        }
        return result;
    }

    private Timestamp calcShipToDate() {
        Date currentDate = new Date();

        // Tạo một đối tượng Calendar và thiết lập thời gian hiện tại
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);

        // Thêm 7 ngày
        calendar.add(Calendar.DAY_OF_MONTH, 7);

        // Lấy thời gian sau khi thêm 7 ngày
        Date sevenDaysLater = calendar.getTime();
        return new Timestamp(sevenDaysLater.getTime());
    }

}
