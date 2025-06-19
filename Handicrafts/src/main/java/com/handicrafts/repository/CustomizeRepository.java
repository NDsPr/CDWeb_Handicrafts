package com.handicrafts.repository;

import com.handicrafts.dto.CustomizeDTO;
import com.handicrafts.entity.CustomizeEntity;
import com.handicrafts.util.CloseResourceUtil;
import com.handicrafts.util.OpenConnectionUtil;
import com.handicrafts.util.SetParameterUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.*;

@Repository
public class CustomizeRepository {

    // Sử dụng JdbcTemplate của Spring thay vì quản lý connection thủ công
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public CustomizeRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    // Nếu vẫn muốn giữ cách quản lý connection thủ công, giữ nguyên code dưới đây

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

    @Transactional
    public int updateCustomize(CustomizeDTO customizeBean) {
        int affectRows = -1;
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

            affectRows = preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            try {
                if (connection != null) {
                    connection.rollback();
                }
                return -1;
            } catch (SQLException ex) {
                ex.printStackTrace();
                return -1;
            }
        } finally {
            CloseResourceUtil.closeNotUseRS(preparedStatement, connection);
        }
        return affectRows;
    }

    @Transactional
    public int createCustomize(CustomizeDTO customizeBean) {
        int affectRows = -1;
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
                    customizeBean.getPrTitle1(), customizeBean.getPrDes1(), customizeBean.getPrIcon1(), customizeBean.getPrContentTitle1(),
                    customizeBean.getPrContentDes1(), customizeBean.getPrLink1(), customizeBean.getPrLink1InStorage(),
                    customizeBean.getPrTitle2(), customizeBean.getPrDes2(), customizeBean.getPrContent2(), customizeBean.getPrLink2(), customizeBean.getPrLink2InStorage(),
                    customizeBean.getBackground(), customizeBean.getBackgroundInStorage(), customizeBean.getFooterLeft(), customizeBean.getFooterMiddle(), customizeBean.getFacebookLink(),
                    customizeBean.getTwitterLink(), customizeBean.getInstagramLink(), customizeBean.getLinkedinLink());

            affectRows = preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            try {
                if (connection != null) {
                    connection.rollback();
                }
                return -1;
            } catch (SQLException ex) {
                ex.printStackTrace();
                return -1;
            }
        } finally {
            CloseResourceUtil.closeNotUseRS(preparedStatement, connection);
        }
        return affectRows;
    }

    // Tìm link của ảnh 1
    public String findOldImage1Link() {
        String link = "";
        String sql = "SELECT prLink1 FROM customize_pages WHERE id = 1";

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = OpenConnectionUtil.openConnection();
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
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
        String sql = "SELECT prLink2 FROM customize_pages WHERE id = 1";

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = OpenConnectionUtil.openConnection();
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
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
        String sql = "SELECT background FROM customize_pages WHERE id = 1";

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = OpenConnectionUtil.openConnection();
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                link = resultSet.getString("background");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            CloseResourceUtil.closeResource(resultSet, preparedStatement, connection);
        }
        return link;
    }
    /**
     * Phương thức save để tương thích với JpaRepository
     * Phương thức này sẽ gọi updateCustomize nếu entity đã tồn tại, ngược lại sẽ gọi createCustomize
     * @param entity Entity cần lưu
     * @return Entity đã được lưu
     */
    public CustomizeEntity save(CustomizeEntity entity) {
        // Chuyển đổi từ Entity sang DTO
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

        // Kiểm tra nếu entity đã tồn tại (id = 1) thì update, ngược lại thì create
        int result;
        if (entity.getId() != null && entity.getId() == 1) {
            result = updateCustomize(dto);
        } else {
            result = createCustomize(dto);
        }

        // Nếu thao tác thành công, trả về entity đã cập nhật
        if (result > 0) {
            return entity;
        }

        // Nếu thao tác thất bại, throw exception
        throw new RuntimeException("Failed to save customize entity");
    }

    /**
     * Phương thức findById để tương thích với JpaRepository
     * @param id ID của entity cần tìm
     * @return Optional chứa entity nếu tìm thấy, empty nếu không tìm thấy
     */
    public java.util.Optional<CustomizeEntity> findById(Integer id) {
        if (id != 1) {
            return java.util.Optional.empty(); // Chỉ có id = 1 trong bảng này
        }

        CustomizeDTO dto = getCustomizeInfo();
        if (dto == null) {
            return java.util.Optional.empty();
        }

        // Chuyển đổi từ DTO sang Entity
        CustomizeEntity entity = new CustomizeEntity();
        entity.setId(dto.getId());
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

        return java.util.Optional.of(entity);
    }

    // Phiên bản sử dụng JdbcTemplate (khuyến nghị sử dụng)
    /*
    public CustomizeDTO getCustomizeInfoWithJdbcTemplate() {
        String sql = "SELECT id, welcomeTitle, welcomeDes, productTitle, productDes, " +
                "prTitle1, prDes1, prContentTitle1, prContentDes1, prIcon1, prLink1, prLink1InStorage, " +
                "prTitle2, prDes2, prContent2, prLink2, prLink2InStorage, " +
                "background, backgroundInStorage, footerLeft, footerMiddle, facebookLink, " +
                "twitterLink, instagramLink, linkedinLink " +
                "FROM customize_pages WHERE id = 1";

        try {
            return jdbcTemplate.queryForObject(sql, new CustomizeDTORowMapper());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static class CustomizeDTORowMapper implements RowMapper<CustomizeDTO> {
        @Override
        public CustomizeDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            CustomizeDTO dto = new CustomizeDTO();
            dto.setId(rs.getInt("id"));
            dto.setWelcomeTitle(rs.getString("welcomeTitle"));
            dto.setWelcomeDes(rs.getString("welcomeDes"));
            dto.setProductTitle(rs.getString("productTitle"));
            dto.setProductDes(rs.getString("productDes"));
            dto.setPrTitle1(rs.getString("prTitle1"));
            dto.setPrContentTitle1(rs.getString("prContentTitle1"));
            dto.setPrContentDes1(rs.getString("prContentDes1"));
            dto.setPrDes1(rs.getString("prDes1"));
            dto.setPrIcon1(rs.getString("prIcon1"));
            dto.setPrLink1(rs.getString("prLink1"));
            dto.setPrLink1InStorage(rs.getString("prLink1InStorage"));
            dto.setPrTitle2(rs.getString("prTitle2"));
            dto.setPrDes2(rs.getString("prDes2"));
            dto.setPrContent2(rs.getString("prContent2"));
            dto.setPrLink2(rs.getString("prLink2"));
            dto.setPrLink2InStorage(rs.getString("prLink2InStorage"));
            dto.setBackground(rs.getString("background"));
            dto.setBackgroundInStorage(rs.getString("backgroundInStorage"));
            dto.setFooterLeft(rs.getString("footerLeft"));
            dto.setFooterMiddle(rs.getString("footerMiddle"));
            dto.setFacebookLink(rs.getString("facebookLink"));
            dto.setTwitterLink(rs.getString("twitterLink"));
            dto.setInstagramLink(rs.getString("instagramLink"));
            dto.setLinkedinLink(rs.getString("linkedinLink"));
            return dto;
        }
    }
    */

}
