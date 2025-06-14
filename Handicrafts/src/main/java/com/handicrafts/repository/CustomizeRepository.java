package com.handicrafts.repository;

import com.handicrafts.dto.CustomizeDTO;
import com.handicrafts.util.CloseResourceUtil;
import com.handicrafts.util.OpenConnectionUtil;
import com.handicrafts.util.SetParameterUtil;
import org.springframework.stereotype.Repository;


import java.sql.*;
@Repository
public class CustomizeRepository {
    public CustomizeDTO getCustomizeInfo() {
        CustomizeDTO customizeBean = null;
        String sql = "SELECT id, welcomeTitle, welcomeDes, productTitle, productDes, " +
                "prTitle1, prDes1, prContentTitle1, prContentDes1, prIcon1, prLink1, prLink1InStorage, " +
                "prTitle2, prDes2, prContent2, prLink2, prLink2InStorage, " +
                "background, backgroundInStorage, footerLeft, footerMiddle, facebookLink, " +
                "twitterLink, instagramLink, linkedinLink " +
                "FROM customize_pages WHERE id = 1";

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = OpenConnectionUtil.openConnection();
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                customizeBean = new CustomizeDTO();
                customizeBean.setId(resultSet.getInt("id"));
                customizeBean.setWelcomeTitle(resultSet.getString("welcomeTitle"));
                customizeBean.setWelcomeDes(resultSet.getString("welcomeDes"));
                customizeBean.setProductTitle(resultSet.getString("productTitle"));
                customizeBean.setProductDes(resultSet.getString("productDes"));
                customizeBean.setPrTitle1(resultSet.getString("prTitle1"));
                customizeBean.setPrContentTitle1(resultSet.getString("prContentTitle1"));
                customizeBean.setPrContentDes1(resultSet.getString("prContentDes1"));
                customizeBean.setPrDes1(resultSet.getString("prDes1"));
                customizeBean.setPrIcon1(resultSet.getString("prIcon1"));
                customizeBean.setPrLink1(resultSet.getString("prLink1"));
                customizeBean.setPrLink1InStorage(resultSet.getString("prLink1InStorage"));
                customizeBean.setPrTitle2(resultSet.getString("prTitle2"));
                customizeBean.setPrDes2(resultSet.getString("prDes2"));
                customizeBean.setPrContent2(resultSet.getString("prContent2"));
                customizeBean.setPrLink2(resultSet.getString("prLink2"));
                customizeBean.setPrLink2InStorage(resultSet.getString("prLink2InStorage"));
                customizeBean.setBackground(resultSet.getString("background"));
                customizeBean.setBackgroundInStorage(resultSet.getString("backgroundInStorage"));
                customizeBean.setFooterLeft(resultSet.getString("footerLeft"));
                customizeBean.setFooterMiddle(resultSet.getString("footerMiddle"));
                customizeBean.setFacebookLink(resultSet.getString("facebookLink"));
                customizeBean.setTwitterLink(resultSet.getString("twitterLink"));
                customizeBean.setInstagramLink(resultSet.getString("instagramLink"));
                customizeBean.setLinkedinLink(resultSet.getString("linkedinLink"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            CloseResourceUtil.closeResource(resultSet, preparedStatement, connection);
        }
        return customizeBean;
    }

    public int updateCustomize(CustomizeDTO customizeBean) {
        int afffectRows = -1;
        String sql = "UPDATE customize_pages SET welcomeTitle = ?, welcomeDes = ?, " +
                "productTitle = ?, productDes = ?, " +
                "prTitle1 = ?, prDes1 = ?, prIcon1 = ?,  prContentTitle1 = ?, prContentDes1 = ?, prLink1 = ?, prLink1InStorage = ?, " +
                "prTitle2 = ?, prDes2 = ?, prContent2 = ?, prLink2 = ?, prLink2InStorage = ?, " +
                "background = ?, backgroundInStorage = ?, footerLeft = ?, footerMiddle = ?, facebookLink = ?, " +
                "twitterLink = ?, instagramLink = ?, linkedinLink = ? " +
                "WHERE id = 1";

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = OpenConnectionUtil.openConnection();
            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement(sql);

            SetParameterUtil.setParameter(preparedStatement, customizeBean.getWelcomeTitle(), customizeBean.getWelcomeDes(),
                    customizeBean.getProductTitle(), customizeBean.getProductDes(),
                    customizeBean.getPrTitle1(), customizeBean.getPrDes1(), customizeBean.getPrIcon1(), customizeBean.getPrContentTitle1(),
                    customizeBean.getPrContentDes1(), customizeBean.getPrLink1(), customizeBean.getPrLink1InStorage(),
                    customizeBean.getPrTitle2(), customizeBean.getPrDes2(), customizeBean.getPrContent2(), customizeBean.getPrLink2(), customizeBean.getPrLink2InStorage(),
                    customizeBean.getBackground(), customizeBean.getBackgroundInStorage(), customizeBean.getFooterLeft(), customizeBean.getFooterMiddle(), customizeBean.getFacebookLink(),
                    customizeBean.getTwitterLink(), customizeBean.getInstagramLink(), customizeBean.getLinkedinLink());

            afffectRows = preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
                return -1;
            } catch (SQLException ex) {
                e.printStackTrace();
                return -1;
            }
        } finally {
            CloseResourceUtil.closeNotUseRS(preparedStatement, connection);
        }
        return afffectRows;
    }

    public int createCustomize(CustomizeDTO customizeBean) {
        int afffectRows = -1;
        String sql = "INSERT INTO customize_pages (welcomeTitle, welcomeDes, productTitle, productDes, " +
                "prTitle1, prDes1, prIcon1, prContentTitle1, prContentDes1, prLink1, prLink1InStorage, prTitle2, prDes2, prContent2, prLink2, " +
                "prLink2InStorage, background, backgroundInStorage, footerLeft, footerMiddle, facebookLink, twitterLink, instagramLink, linkedinLink) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";


        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = OpenConnectionUtil.openConnection();
            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            SetParameterUtil.setParameter(preparedStatement, customizeBean.getWelcomeTitle(), customizeBean.getWelcomeDes(),
                    customizeBean.getProductTitle(), customizeBean.getProductDes(),
                    customizeBean.getPrTitle1(), customizeBean.getPrContentTitle1(), customizeBean.getPrContentDes1(), customizeBean.getPrDes1(), customizeBean.getPrIcon1(), customizeBean.getPrLink1(),
                    customizeBean.getPrLink1InStorage(), customizeBean.getPrTitle2(), customizeBean.getPrDes2(), customizeBean.getPrContent2(), customizeBean.getPrLink2(), customizeBean.getPrLink2InStorage(),
                    customizeBean.getBackground(), customizeBean.getBackgroundInStorage(), customizeBean.getFooterLeft(), customizeBean.getFooterMiddle(), customizeBean.getFacebookLink(),
                    customizeBean.getTwitterLink(), customizeBean.getInstagramLink(), customizeBean.getLinkedinLink());

            afffectRows = preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
                return -1;
            } catch (SQLException ex) {
                e.printStackTrace();
                return -1;
            }
        } finally {
            CloseResourceUtil.closeNotUseRS(preparedStatement, connection);
        }
        return afffectRows;
    }

    // Tìm link của ảnh 1
    public String findOldImage1Link() {
        String link = "";
        String sql = "SELECT prLink1 FROM customize_pages";

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = OpenConnectionUtil.openConnection();
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                link = resultSet.getString("prLink1");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            CloseResourceUtil.closeResource(resultSet, preparedStatement, connection);
        }
        return link;
    }

    // Tìm link của ảnh 2
    public String findOldImage2Link() {
        String link = "";
        String sql = "SELECT prLink2 FROM customize_pages";

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = OpenConnectionUtil.openConnection();
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                link = resultSet.getString("prLink2");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            CloseResourceUtil.closeResource(resultSet, preparedStatement, connection);
        }
        return link;
    }

    // Tìm link của background
    public String findOldBackgroundLink() {
        String link = "";
        String sql = "SELECT background FROM customize_pages";

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = OpenConnectionUtil.openConnection();
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                link = resultSet.getString("background");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            CloseResourceUtil.closeResource(resultSet, preparedStatement, connection);
        }
        return link;
    }
}