package com.handicrafts.dto;


import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class HandicraftDTO {
    private int id;

    private String title;

    private int year_made;

    private String material;

    private String brand;

    private String description;

    private int price;

    private double discountPercent;

    private int quantitySold;
    private boolean active;
    private boolean popular;
    private boolean new_arrival;

    private CategoryDTO category;
    private Category_typeDTO category_type;
    private List<HandicraftImageDTO> images = new ArrayList<>();
    private List<OrderlineDTO> orderlines = new ArrayList<>();

    //getter & setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getYear_made() {
        return year_made;
    }

    public void setYear_made(int year_made) {
        this.year_made = year_made;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public double getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(double discountPercent) {
        this.discountPercent = discountPercent;
    }

    public int getQuantitySold() {
        return quantitySold;
    }

    public void setQuantitySold(int quantitySold) {
        this.quantitySold = quantitySold;
    }

    public CategoryDTO getCategory() {
        return category;
    }

    public void setCategory(CategoryDTO category) {
        this.category = category;
    }

    public Category_typeDTO getCategory_type() {
        return category_type;
    }

    public void setCategory_tpye(Category_typeDTO category_type) {
        this.category_type = category_type;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isPopular() {
        return popular;
    }

    public void setPopular(boolean popular) {
        this.popular = popular;
    }

    public boolean isNew_arrival() {
        return new_arrival;
    }

    public void setNew_arrival(boolean new_arrival) {
        this.new_arrival = new_arrival;
    }

    public List<HandicraftImageDTO> getImages() {
        return images;
    }

    public void setImages(List<HandicraftImageDTO> images) {
        this.images = images;
    }

    public String getDiscountPrice() {
        double discountPrice = this.price * (1 - this.discountPercent / 100);
        DecimalFormat df = new DecimalFormat("###,###,###");
        return df.format(discountPrice) + " VNĐ";

    }

    public double getDiscount() {
        double percent = this.discountPercent / 100;
        double discountPrice = this.price * (1 - percent);
        return discountPrice;

    }

    public String formatPrice(int price) {
        DecimalFormat df = new DecimalFormat("###,###,###");
        return df.format(price) + " VNĐ";
    }

    public String getPriceFormat() {
        DecimalFormat df = new DecimalFormat("###,###,###");
        return df.format(this.price) + " VNĐ";
    }

    public String getPercentFormat() {
        return "-" + this.discountPercent + "%";
    }
}
