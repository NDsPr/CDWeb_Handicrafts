package com.handicrafts.dto;

import java.util.ArrayList;
import java.util.List;

public class CartDTO {
    private List<ItemDTO> items;
    private int totalItem;
    private double originalPriceTotal;
    private double discountPriceTotal;

    public CartDTO() {
        items = new ArrayList<>();
    }

    public void addItem(ItemDTO item) {
        // Nếu trong cart đã có item => quantity + 1
        int itemProductId = item.getProduct().getId();
        if (getItemByProductId(itemProductId) != null) {
            ItemDTO itemInCart = getItemByProductId(itemProductId);
            itemInCart.setQuantity(itemInCart.getQuantity() + 1);
            itemInCart.setTotal(item.getProduct().getOriginalPrice() * itemInCart.getQuantity());
            itemInCart.setTotalWithDiscount(item.getProduct().getDiscountPrice() * itemInCart.getQuantity());
        } else {
            // Chưa có thì thêm item vào và set quantity = 1
            item.setQuantity(1);
            item.setTotal(item.getProduct().getOriginalPrice());
            item.setTotalWithDiscount(item.getProduct().getDiscountPrice());
            items.add(item);
        }
        this.totalItem = items.size();

        // Set tổng vào cart
        setTotalPurcharseAndDiscount();
    }

    public void updateItem(int productId, int quantity) {
        ItemDTO item = getItemByProductId(productId);
        if (item != null) {
            // Nếu số lượng nhỏ hơn hoặc bằng 0 thì xóa khỏi cart
            if (quantity <= 0) {
                deleteItemZeroQuantity(productId);
            } else {
                item.setQuantity(quantity);
                double originalTotal = item.getProduct().getOriginalPrice() * quantity;
                double discountTotal = item.getProduct().getDiscountPrice() * quantity;
                item.setTotal(originalTotal);
                item.setTotalWithDiscount(discountTotal);
            }
        }

        // Set tổng vào cart
        setTotalPurcharseAndDiscount();
    }

    public int deleteItem(int productId) {
        ItemDTO item = getItemByProductId(productId);
        if (item != null) {
            items.remove(item);
            // Set tổng vào cart
            setTotalPurcharseAndDiscount();
            return 1;
        }
        return -1;
    }

    // Không set tổng vào cart khi xóa
    public void deleteItemZeroQuantity(int productId) {
        ItemDTO item = getItemByProductId(productId);
        if (item != null) {
            items.remove(item);
        }
    }

    public ItemDTO getItemByProductId(int productId) {
        for (ItemDTO item : items) {
            if (item.getProduct().getId() == productId) {
                return item;
            }
        }
        return null;
    }

    public List<ItemDTO> getItems() {
        return items;
    }

    public void setItems(List<ItemDTO> items) {
        this.items = items;
    }

    public void setTotalItem(int totalItem) {
        this.totalItem = totalItem;
    }

    public double getOriginalPriceTotal() {
        return originalPriceTotal;
    }

    public void setOriginalPriceTotal(double originalPriceTotal) {
        this.originalPriceTotal = originalPriceTotal;
    }

    public double getDiscountPriceTotal() {
        return discountPriceTotal;
    }

    public void setDiscountPriceTotal(double discountPriceTotal) {
        this.discountPriceTotal = discountPriceTotal;
    }

    public int getTotalItem() {
        this.totalItem = items.size();
        return totalItem;
    }

    private void setTotalPurcharseAndDiscount() {
        // Trả về 0 trước mỗi lần set
        this.originalPriceTotal = 0;
        this.discountPriceTotal = 0;
        // Set tổng vào cart
        for (ItemDTO i : items) {
            this.originalPriceTotal += i.getProduct().getOriginalPrice() * i.getQuantity();
            this.discountPriceTotal += i.getProduct().getDiscountPrice() * i.getQuantity();
        }
    }
}
