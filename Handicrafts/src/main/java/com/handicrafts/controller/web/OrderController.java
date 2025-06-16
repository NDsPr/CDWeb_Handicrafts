package com.handicrafts.controller.web;

import com.handicrafts.repository.OrderRepository;
import com.handicrafts.repository.OrderDetailRepository;
import com.handicrafts.service.IHandicraftService;
import com.handicrafts.service.IUserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private IUserService userService;

    @Autowired
    private IHandicraftService handicraftService;

//    @GetMapping("/cart")
//    public ModelAndView showCart(Authentication authentication) {
//        ModelAndView mav = new ModelAndView("web/cart");
//
//        if (authentication != null && authentication.isAuthenticated()) {
//            int userId = getUserId(authentication);
//
//            // Find cart (order with status = 1)
//            List<OrderDTO> userOrders = orderRepository.findOrderByUserId(userId);
//            OrderDTO cart = userOrders.stream()
//                    .filter(order -> order.getStatus() == 1)
//                    .findFirst()
//                    .orElse(null);
//
//            if (cart != null) {
//                // Get cart items
//                List<OrderDetailDTO> cartItems = orderDetailRepository.findOrderDetailByOrderId(cart.getId());
//
//                // Convert to CartDTO objects with product details
//                List<CartDTO> cartDTOs = new ArrayList<>();
//                double total = 0;
//
//                for (OrderDetailDTO item : cartItems) {
//                    CartDTO cartDTO = new CartDTO();
//                    cartDTO.setOrderId(cart.getId());
//                    cartDTO.setProductId(item.getProductId());
//                    cartDTO.setQuantity(item.getQuantity());
//
//                    // Get product details from handicraft service
//                    cartDTO.setProduct(handicraftService.findById(item.getProductId()));
//
//                    // Calculate subtotal
//                    double price = cartDTO.getProduct().getPrice();
//                    double subtotal = price * item.getQuantity();
//                    cartDTO.setSubtotal(subtotal);
//
//                    total += subtotal;
//                    cartDTOs.add(cartDTO);
//                }
//
//                mav.addObject("cartItems", cartDTOs);
//                mav.addObject("total", total);
//                mav.addObject("cartId", cart.getId());
//            } else {
//                mav.addObject("cartItems", new ArrayList<>());
//                mav.addObject("total", 0);
//            }
//        } else {
//            mav.addObject("cartItems", new ArrayList<>());
//            mav.addObject("total", 0);
//        }
//
//        return mav;
//    }
//
//    @PostMapping("/checkout")
//    public ModelAndView checkout(@RequestParam(value = "paymentMethod", defaultValue = "COD") String paymentMethod,
//                                 Authentication authentication) {
//        ModelAndView mav = new ModelAndView("redirect:/order-success");
//
//        if (authentication != null && authentication.isAuthenticated()) {
//            int userId = getUserId(authentication);
//
//            // Find cart
//            List<OrderDTO> userOrders = orderRepository.findOrderByUserId(userId);
//            OrderDTO cart = userOrders.stream()
//                    .filter(order -> order.getStatus() == 1)
//                    .findFirst()
//                    .orElse(null);
//
//            if (cart != null) {
//                // Update cart to order
//                cart.setStatus(2); // 2 = ordered
//                cart.setPaymentMethod(paymentMethod);
//                cart.setShipToDate(Timestamp.valueOf(LocalDateTime.now().plusDays(5)));
//                cart.setModifiedDate(Timestamp.valueOf(LocalDateTime.now()));
//                cart.setModifiedBy(authentication.getName());
//
//                // Calculate total from order details
//                List<OrderDetailDTO> orderDetails = orderDetailRepository.findOrderDetailByOrderId(cart.getId());
//                double total = 0;
//
//                for (OrderDetailDTO detail : orderDetails) {
//                    double price = handicraftService.findById(detail.getProductId()).getPrice();
//                    total += price * detail.getQuantity();
//                }
//
//                cart.setTotal(total);
//
//                // Update the order
//                int affected = orderRepository.updateOrder(cart);
//
//                if (affected <= 0) {
//                    mav.setViewName("redirect:/cart?error=checkout");
//                }
//            } else {
//                mav.setViewName("redirect:/cart?error=nocart");
//            }
//        } else {
//            mav.setViewName("redirect:/login");
//        }
//
//        return mav;
//    }
//
//    @GetMapping("/order-success")
//    public ModelAndView orderSuccess() {
//        return new ModelAndView("web/order-success");
//    }
//
//    private int getUserId(Authentication authentication) {
//        if (authentication.getPrincipal() instanceof CustomOAuth2User) {
//            CustomOAuth2User oauthUser = (CustomOAuth2User) authentication.getPrincipal();
//            return userService.findByEmail(oauthUser.getEmail()).getId();
//        } else {
//            return userService.findByUsername(authentication.getName()).getId();
//        }
//    }
}
