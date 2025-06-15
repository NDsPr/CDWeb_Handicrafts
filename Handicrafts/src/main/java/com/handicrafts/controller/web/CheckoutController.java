package com.handicrafts.controller.web;

import com.handicrafts.dto.*;
import com.handicrafts.constant.LogLevel;
import com.handicrafts.constant.LogState;
import com.handicrafts.dto.FullOrderDTO;
import com.handicrafts.repository.*;
import com.handicrafts.service.LogService;
import com.handicrafts.util.SessionUtil;
import com.handicrafts.util.ValidateParamUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    private LogService<UserBean> userLogService;

    @Autowired
    private LogService<FullOrderDTO> orderLogService;

    private ResourceBundle logBundle = ResourceBundle.getBundle("log-content");

    @GetMapping
    public String doGet(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        CustomizeBean customizeInfo = customizeRepository.getCustomizeInfo();
        // Kiểm tra só lượng trước khi cho phép checkout
        Cart cart = (Cart) SessionUtil.getInstance().getValue(req, "cart");
        List<Item> items = cart.getItems();

        for (Item item : items) {
            if (item.getQuantity() > item.getProduct().getQuantity()) {
                return "redirect:/cart-management?error=e&productId=" + item.getProduct().getId()
                        + "&quantity=" + item.getProduct().getQuantity();
            }
        }
        req.setAttribute("customizeInfo", customizeInfo);
        return "checkout";
    }

    @PostMapping
    public String doPost(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        req.setCharacterEncoding("UTF-8");
        String firstName = req.getParameter("firstName");
        String lastName = req.getParameter("lastName");
        String addressLine = req.getParameter("addressLine");
        String addressWard = req.getParameter("addressWard");
        String addressDistrict = req.getParameter("addressDistrict");
        String addressProvince = req.getParameter("addressProvince");
        String paymentMethod = req.getParameter("paymentMethod");

        String[] inputsForm = new String[]{firstName, lastName, addressLine, addressWard, addressDistrict, addressProvince};

        // Biến bắt lỗi
        boolean isValid = true;

        // Kiểm tra input rằng/null trong hàm checkEmptyParam
        List<String> errors = ValidateParamUtil.checkEmptyParam(inputsForm);

        // Nếu có lỗi (khác null) trả về isValid = false
        for (String error : errors) {
            if (error != null) {
                isValid = false;
                break;
            }
        }

        UserBean user = (UserBean) SessionUtil.getInstance().getValue(req, "user");
        Cart cart = (Cart) SessionUtil.getInstance().getValue(req, "cart");
        UserBean prevUser = userRepository.findById(user.getId()).orElse(null);

        if (isValid) {
            // Lưu thông tin của người dùng đã nhập
            UserBean userBean = new UserBean();
            userBean.setId(user.getId());
            userBean.setFirstName(firstName.trim());
            userBean.setLastName(lastName.trim());
            userBean.setAddressLine(addressLine.trim());
            userBean.setAddressWard(addressWard.trim());
            userBean.setAddressDistrict(addressDistrict.trim());
            userBean.setAddressProvince(addressProvince.trim());

            int affectedRows = userRepository.updateAccount(userBean);
            UserBean currentUser = userRepository.findById(user.getId()).orElse(null);
            if (affectedRows <= 0) {
                userLogService.log(req, "user-update-in-checkout", LogState.FAIL, LogLevel.ALERT, prevUser, currentUser);
            } else {
                userLogService.log(req, "user-update-in-checkout", LogState.SUCCESS, LogLevel.INFO, prevUser, currentUser);
            }

            // Lưu thông tin đơn hàng
            OrderBean orderBean = new OrderBean();
            orderBean.setUserId(user.getId());
            orderBean.setTotal(cart.getDiscountPriceTotal());
            if (paymentMethod.equals("paymentOnDelivery")) {
                orderBean.setPaymentMethod("Thanh toán khi nhận hàng");
            } else {
                orderBean.setPaymentMethod("Thanh toán chuyển khoản");
            }
            orderBean.setCreatedDate(new Timestamp(System.currentTimeMillis()));
            orderBean.setShipToDate(calcShipToDate());
            orderBean.setCreatedBy(user.getEmail());
            orderBean.setModifiedDate(new Timestamp(System.currentTimeMillis()));
            orderBean.setModifiedBy(user.getEmail());

            int orderId = orderRepository.createOrder(orderBean);
            if (orderId <= 0) {
                req.setAttribute("insertError", "ie");
                CustomizeBean customizeInfo = customizeRepository.getCustomizeInfo();
                req.setAttribute("customizeInfo", customizeInfo);
                return "checkout";
            } else {
                List<OrderDetailBean> orderDetails = new ArrayList<>();

                for (Item item : cart.getItems()) {
                    OrderDetailBean orderDetailBean = new OrderDetailBean();
                    orderDetailBean.setOrderId(orderId);
                    orderDetailBean.setProductId(item.getProduct().getId());
                    orderDetailBean.setQuantity(item.getQuantity());
                    // Đánh dấu là chưa review cho sản phẩm này
                    orderDetailBean.setReviewed(0);

                    orderDetails.add(orderDetailBean);
                    orderDetailRepository.save(orderDetailBean);

                    int quantityProductAfterOrder = productRepository.getTotalItems() - item.getQuantity();
                    // Set lại số lượng product
                    productRepository.updateQuantity(item.getProduct().getId(), quantityProductAfterOrder);
                }
                // Ghi log thành công
                FullOrderDTO fullOrder = new FullOrderDTO(orderRepository.findById(orderId).orElse(null), orderDetails);
                orderLogService.log(req, "order-in-checkout", LogState.SUCCESS, LogLevel.INFO, null, fullOrder);
                SessionUtil.getInstance().removeValue(req, "cart");

                return "redirect:/thankyou";
            }
        } else {
            req.setAttribute("errors", errors);
            // Ghi log thất bại
            orderLogService.log(req, "order-in-checkout", LogState.FAIL, LogLevel.ALERT, null, null);
            return "checkout";
        }
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
