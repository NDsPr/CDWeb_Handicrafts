package com.handicrafts.service.impl;


//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import com.handicrafts.converter.CartConverter;
//import com.handicrafts.dto.HandicraftDTO;
//import com.handicrafts.dto.CartDTO;
//import com.handicrafts.dto.UserDTO;
//import com.handicrafts.entity.CartEntity;
//import com.handicrafts.repository.CartRepository;
//import com.handicrafts.service.IHandicraftService;
//import com.handicrafts.service.ICartService;
//import com.handicrafts.service.IUserService;
//
//import java.util.ArrayList;
//import java.util.List;
//
//
//@Service
public class CartServiceImp  {
    //implements ICartService
//    @Autowired
//    private CartRepository cartRepo;
//    @Autowired
//    private IUserService userService;
//    @Autowired
//    private IHandicraftService handicraftService;
//    @Autowired
//    private CartConverter cartConverter;
//
//    @Override
//    public CartDTO addProduct(String email, int handicraftId, int quantity) {
//        //tim user dang dang nhap
//        UserDTO currentUser = userService.findByEmailAndIsEnable(email);
//        //tim xem user co cart chua
//        CartEntity cart = cartRepo.findByUserUserIDAndHandicraftId(currentUser.getUserID(), handicraftId);
//        //neu chua thi tao moi cart, them sach vua bam vo
//        CartDTO newCart = new CartDTO();
//        HandicraftDTO handicraft = handicraftService.findById(handicraftId);
//        if (cart == null) {
//            newCart.setUser(currentUser);
//            newCart.setQuantity(1);
//            newCart.setHandicraft(handicraft);
//        } else {
//            //neu cart da ton tai thi lay id cart set lai cho cartdto
//            newCart.setCartID(cart.getCartID());
//            newCart.setUser(currentUser);
//            newCart.setHandicraft(handicraft);
//            newCart.setQuantity(cart.getQuantity() + quantity);
//        }
//        cartRepo.save(cartConverter.toEntity(newCart));
//        return newCart;
//    }
//
//    @Override
//    public List<CartDTO> getHandicrafts(String email) {
//        //tim tất cả cart có thuộc user
//        List<CartEntity> handicraftsEntity = cartRepo.findAllByUserUserID(userService.findByEmailAndIsEnable(email).getUserID());
//        List<CartDTO> result = new ArrayList<>();
//        for (CartEntity cartEntity : handicraftsEntity) {
//            result.add(cartConverter.toDTO(cartEntity));
//        }
//        return result;
//    }
//
//    @Override
//    public List<CartDTO> deleteHandicraft(String email, int handicraftId) {
//        //tim cart cua user, chua handicraft
//        CartEntity userCart = cartRepo.findByUserUserIDAndHandicraftId(userService.findByEmailAndIsEnable(email).getUserID(), handicraftId);
//        cartRepo.delete(userCart);
//        return getHandicrafts(email);
//
//    }
//
//    @Override
//    public List<CartDTO> updateQuantity(String email, int handicraftId, int quantity) {
//        CartEntity cartDb = cartRepo.findByUserUserIDAndHandicraftId(userService.findByEmailAndIsEnable(email).getUserID(), handicraftId);
//        cartDb.setQuantity(quantity);
//        cartRepo.save(cartDb);
//        return getHandicrafts(email);
//    }
//
//    @Override
//    public CartDTO getById(int id) {
//        return cartConverter.toDTO(cartRepo.getByCartID(id));
//    }
//
//    @Override
//    public void deleteCart(CartDTO cart) {
//        cartRepo.delete(cartConverter.toEntity(cart));
//    }
}
