package com.handicrafts.controller.web;


import com.handicrafts.service.IHandicraftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import com.handicrafts.api.input.OrderInput;
import com.handicrafts.api.output.CartOutput;
import com.handicrafts.dto.CartDTO;
import com.handicrafts.dto.OrderlineDTO;
import com.handicrafts.oauth2.CustomOAuth2User;
import com.handicrafts.service.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
public class OrderController {
    @Autowired
    private ICartService cartService;
    @Autowired
    private IUserService userService;
    @Autowired
    private IOrderService orderService;
    @Autowired
    private IOrderlineService orderlineService;
    @Autowired
    private IHandicraftService handicraftService;

    @PostMapping("/thanh-toan")
    //lay list cart item, total dem qua trang thanh toan
    public ModelAndView getCartItemToCheckout(@ModelAttribute("order") OrderInput input, Authentication authentication) {
        if (authentication == null) {
            return new ModelAndView("web/signin.html");
        }

        String userEmail = "";
        if (authentication.getPrincipal() instanceof CustomOAuth2User) {
            CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
            userEmail = oAuth2User.getAttribute("email");
        } else userEmail = authentication.getName();

        CartOutput output = new CartOutput();
        double total = 0;
        //ds id cac sp
        for (Integer c : input.getCarts()) {
            CartDTO item = cartService.getById(c);
            //tong tien cua moi sp
            total += item.getTotalAmount();
            output.getHandicraftsList().add(item);
        }
        //set lai tong tien cho ouput
        output.setTotal(total);
        ModelAndView mav = new ModelAndView("web/checkout.html");
        mav.addObject("items", output);
        mav.addObject("user", userService.findByEmailAndIsEnable(userEmail));
        return mav;
    }

    @PostMapping("/dat-hang")
    public ModelAndView checkOut(@ModelAttribute("order") OrderInput order, Authentication authentication) {
        ModelAndView mav = new ModelAndView("web/cart.html");
        List<OrderlineDTO> orderLineList = new ArrayList<>();

        //kiem tra principal co phai the hien cua oauth hay khong, neu co thi ep kieu de lay email
        String userEmail = "";
        if (authentication.getPrincipal() instanceof CustomOAuth2User) {
            CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
            userEmail = oAuth2User.getAttribute("email");
        } else userEmail = authentication.getName();

        double total = 0;
        //ds id cac sp
        for (Integer c : order.getCarts()) {
            CartDTO cart = cartService.getById(c);
            //tong tien cua moi sp
            total += cart.getTotalAmount();
            //tao orderline
            OrderlineDTO line = new OrderlineDTO();
            line.setBook(cart.getBook());
            line.setQuantity(cart.getQuantity());
            line.setTotalPrice(cart.getTotalAmount());

            //thêm line vào list (lát set cho order)
            //xóa luôn sp đã đặt trong cart
            orderLineList.add(line);
            cartService.deleteCart(cart);

            //tăng quantity cho book
            HandicraftDTO handicraftDTO = handicraftService.findById(cart.getHandicraft().getId());
            //cart.quantity là số lượng sách mà người dùng đặt mua
            handicraftService.updateQuantity(handicraft.getQuantitySold() + cart.getQuantity(), cart.getHandicraft().getId());
        }
        //tạo order mới
        OrderDTO newOrder = new OrderDTO();
        newOrder.setCreatedAt(LocalDate.now());
        //dl lay tu model attribute
        newOrder.setAddress(order.getAddress());
        newOrder.setName(order.getName());
        newOrder.setPhone(order.getPhone());
        newOrder.setNote(order.getNote());
        //set list orderline cho order
        newOrder.setOrderlines(orderLineList);
        newOrder.setUser(userService.findByEmailAndIsEnable(userEmail));
        newOrder.setStatus("Đang xử lý..");
        newOrder.setTotalPrice(total + 15000);
        orderService.save(newOrder);

        //chạy list orderline de set lai orderid
        for (OrderlineDTO l : orderLineList) {
            l.setOrder(orderService.findLastSave());
            orderlineService.save(l);
        }
        mav.addObject("message", "Đặt hàng thành công. Vui lòng truy cập trang đơn hàng để theo dõi.");
        return mav;
    }

    @GetMapping("/don-hang")
    public ModelAndView checkOrder(Authentication authentication) {
        ModelAndView mav = new ModelAndView("web/checkOrder.html");
        if (authentication == null) return new ModelAndView("web/signin.html");
        if (authentication.getPrincipal() instanceof CustomOAuth2User) {
            CustomOAuth2User oauthUser = (CustomOAuth2User) authentication.getPrincipal();
            mav.addObject("orders", orderService.findAllByUserId(userService.findByEmailAndIsEnable(oauthUser.getAttribute("email")).getUserID()));
        } else
            mav.addObject("orders", orderService.findAllByUserId(userService.findByEmailAndIsEnable(authentication.getName()).getUserID()));
        return mav;
    }
}
