package com.handicrafts.service.impl;

import com.handicrafts.dto.CustomizeDTO;
import com.handicrafts.entity.CustomizeEntity;
import com.handicrafts.repository.CustomizeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomizeService {

    @Autowired
    private CustomizeRepository customizeRepository;

    public CustomizeDTO getCustomizeInfo() {
        CustomizeEntity entity = customizeRepository.findById(1).orElse(new CustomizeEntity());
        return convertToDTO(entity);
    }

    @Transactional
    public boolean updateCustomize(CustomizeDTO customizeDTO) {
        try {
            CustomizeEntity entity = convertToEntity(customizeDTO);
            customizeRepository.save(entity);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public String findOldImage1Link() {
        return customizeRepository.findOldImage1Link();
    }

    public String findOldImage2Link() {
        return customizeRepository.findOldImage2Link();
    }

    public String findOldBackgroundLink() {
        return customizeRepository.findOldBackgroundLink();
    }

    private CustomizeDTO convertToDTO(CustomizeEntity entity) {
        CustomizeDTO dto = new CustomizeDTO();
        dto.setId(entity.getId());
        dto.setWelcomeTitle(entity.getWelcomeTitle());
        dto.setWelcomeDes(entity.getWelcomeDes());
        dto.setProductTitle(entity.getProductTitle());
        dto.setProductDes(entity.getProductDes());
        dto.setPrTitle1(entity.getPrTitle1());
        dto.setPrDes1(entity.getPrDes1());
        dto.setPrIcon1(entity.getPrIcon1());
        dto.setPrContentTitle1(entity.getPrContentTitle1());
        dto.setPrContentDes1(entity.getPrContentDes1());
        dto.setPrLink1(entity.getPrLink1());
        dto.setPrLink1InStorage(entity.getPrLink1InStorage());
        dto.setPrTitle2(entity.getPrTitle2());
        dto.setPrDes2(entity.getPrDes2());
        dto.setPrContent2(entity.getPrContent2());
        dto.setPrLink2(entity.getPrLink2());
        dto.setPrLink2InStorage(entity.getPrLink2InStorage());
        dto.setBackground(entity.getBackground());
        dto.setBackgroundInStorage(entity.getBackgroundInStorage());
        dto.setFooterLeft(entity.getFooterLeft());
        dto.setFooterMiddle(entity.getFooterMiddle());
        dto.setFacebookLink(entity.getFacebookLink());
        dto.setTwitterLink(entity.getTwitterLink());
        dto.setInstagramLink(entity.getInstagramLink());
        dto.setLinkedinLink(entity.getLinkedinLink());
        return dto;
    }

    private CustomizeEntity convertToEntity(CustomizeDTO dto) {
        CustomizeEntity entity = customizeRepository.findById(dto.getId() != null ? dto.getId() : 1)
                .orElse(new CustomizeEntity());

        entity.setWelcomeTitle(dto.getWelcomeTitle());
        entity.setWelcomeDes(dto.getWelcomeDes());
        entity.setProductTitle(dto.getProductTitle());
        entity.setProductDes(dto.getProductDes());
        entity.setPrTitle1(dto.getPrTitle1());
        entity.setPrDes1(dto.getPrDes1());
        entity.setPrIcon1(dto.getPrIcon1());
        entity.setPrContentTitle1(dto.getPrContentTitle1());
        entity.setPrContentDes1(dto.getPrContentDes1());
        entity.setPrLink1(dto.getPrLink1());
        entity.setPrLink1InStorage(dto.getPrLink1InStorage());
        entity.setPrTitle2(dto.getPrTitle2());
        entity.setPrDes2(dto.getPrDes2());
        entity.setPrContent2(dto.getPrContent2());
        entity.setPrLink2(dto.getPrLink2());
        entity.setPrLink2InStorage(dto.getPrLink2InStorage());
        entity.setBackground(dto.getBackground());
        entity.setBackgroundInStorage(dto.getBackgroundInStorage());
        entity.setFooterLeft(dto.getFooterLeft());
        entity.setFooterMiddle(dto.getFooterMiddle());
        entity.setFacebookLink(dto.getFacebookLink());
        entity.setTwitterLink(dto.getTwitterLink());
        entity.setInstagramLink(dto.getInstagramLink());
        entity.setLinkedinLink(dto.getLinkedinLink());

        return entity;
    }
}
