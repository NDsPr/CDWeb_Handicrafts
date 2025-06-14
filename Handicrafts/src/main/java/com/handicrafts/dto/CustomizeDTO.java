package com.handicrafts.dto;

// TODO: Upload 1 ảnh cho mỗi description.

import lombok.Data;

@Data
public class CustomizeDTO {
    private int id;
    private String welcomeTitle, welcomeDes,
            productTitle, productDes,
            prTitle1, prContentTitle1, prContentDes1, prDes1, prIcon1, prLink1, prLink1InStorage,
            prTitle2, prDes2, prContent2, prLink2, prLink2InStorage,
            background, backgroundInStorage, footerLeft, footerMiddle, facebookLink,
            twitterLink, instagramLink, linkedinLink;

    public CustomizeDTO() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWelcomeTitle() {
        return welcomeTitle;
    }

    public void setWelcomeTitle(String welcomeTitle) {
        this.welcomeTitle = welcomeTitle;
    }

    public String getWelcomeDes() {
        return welcomeDes;
    }

    public void setWelcomeDes(String welcomeDes) {
        this.welcomeDes = welcomeDes;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public String getProductDes() {
        return productDes;
    }

    public void setProductDes(String productDes) {
        this.productDes = productDes;
    }

    public String getPrTitle1() {
        return prTitle1;
    }

    public void setPrTitle1(String prTitle1) {
        this.prTitle1 = prTitle1;
    }

    public String getPrDes1() {
        return prDes1;
    }

    public void setPrDes1(String prDes1) {
        this.prDes1 = prDes1;
    }

    public String getPrIcon1() {
        return prIcon1;
    }

    public void setPrIcon1(String prIcon1) {
        this.prIcon1 = prIcon1;
    }

    public String getPrLink1() {
        return prLink1;
    }

    public void setPrLink1(String prLink1) {
        this.prLink1 = prLink1;
    }

    public String getPrTitle2() {
        return prTitle2;
    }

    public void setPrTitle2(String prTitle2) {
        this.prTitle2 = prTitle2;
    }

    public String getPrDes2() {
        return prDes2;
    }

    public void setPrDes2(String prDes2) {
        this.prDes2 = prDes2;
    }

    public String getPrLink2() {
        return prLink2;
    }

    public void setPrLink2(String prLink2) {
        this.prLink2 = prLink2;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public String getFooterLeft() {
        return footerLeft;
    }

    public void setFooterLeft(String footerLeft) {
        this.footerLeft = footerLeft;
    }

    public String getFooterMiddle() {
        return footerMiddle;
    }

    public void setFooterMiddle(String footerMiddle) {
        this.footerMiddle = footerMiddle;
    }

    public String getFacebookLink() {
        return facebookLink;
    }

    public void setFacebookLink(String facebookLink) {
        this.facebookLink = facebookLink;
    }

    public String getTwitterLink() {
        return twitterLink;
    }

    public void setTwitterLink(String twitterLink) {
        this.twitterLink = twitterLink;
    }

    public String getInstagramLink() {
        return instagramLink;
    }

    public void setInstagramLink(String instagramLink) {
        this.instagramLink = instagramLink;
    }

    public String getLinkedinLink() {
        return linkedinLink;
    }

    public void setLinkedinLink(String linkedinLink) {
        this.linkedinLink = linkedinLink;
    }

    public String getPrContentTitle1() {
        return prContentTitle1;
    }

    public void setPrContentTitle1(String prContentTitle1) {
        this.prContentTitle1 = prContentTitle1;
    }

    public String getPrContentDes1() {
        return prContentDes1;
    }

    public void setPrContentDes1(String prContentDes1) {
        this.prContentDes1 = prContentDes1;
    }

    public String getPrContent2() {
        return prContent2;
    }

    public void setPrContent2(String prContent2) {
        this.prContent2 = prContent2;
    }

    public String getPrLink1InStorage() {
        return prLink1InStorage;
    }

    public void setPrLink1InStorage(String prLink1InStorage) {
        this.prLink1InStorage = prLink1InStorage;
    }

    public String getPrLink2InStorage() {
        return prLink2InStorage;
    }

    public void setPrLink2InStorage(String prLink2InStorage) {
        this.prLink2InStorage = prLink2InStorage;
    }

    public String getBackgroundInStorage() {
        return backgroundInStorage;
    }

    public void setBackgroundInStorage(String backgroundInStorage) {
        this.backgroundInStorage = backgroundInStorage;
    }
}
