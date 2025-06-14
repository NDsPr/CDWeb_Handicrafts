package com.handicrafts.repository;

import com.handicrafts.dto.ProductImageDTO;
import com.handicrafts.util.CloseResourceUtil;
import com.handicrafts.util.OpenConnectionUtil;
import com.handicrafts.util.SetParameterUtil;
import org.springframework.stereotype.Repository;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ImageRepository {
    // TODO: Cần thêm status cho ảnh (Sẽ làm sau)
    public ProductImageDTO findImageById(int id) {
        ProductImageDTO imageDTO = null;
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT id, name, link, productId ")
                .append("FROM images WHERE id = ?");

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = OpenConnectionUtil.openConnection();
            preparedStatement = connection.prepareStatement(sql.toString());
            SetParameterUtil.setParameter(preparedStatement, id);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                imageDTO = new ProductImageDTO();
                imageDTO.setId(resultSet.getInt("id"));
                imageDTO.setName(resultSet.getString("name"));
                imageDTO.setLink(resultSet.getString("link"));
                imageDTO.setProductId(resultSet.getInt("productId"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            CloseResourceUtil.closeResource(resultSet, preparedStatement, connection);
        }
        return imageDTO;
    }

    public int insertProductImage(ProductImageDTO image) {
        int id = -1;
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO images ")
                .append("(name, link, productId, createdBy, modifiedBy )")
                .append(" VALUES ")
                .append("(?, ?, ?, ?, ?)");

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = OpenConnectionUtil.openConnection();
            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);

            SetParameterUtil.setParameter(preparedStatement, image.getName(), image.getLink(), image.getProductId(),
                    image.getCreatedBy(), image.getModifiedBy());
            id = preparedStatement.executeUpdate();

            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        } finally {
            CloseResourceUtil.closeNotUseRS(preparedStatement, connection);
        }
        return id;
    }

    public List<ProductImageDTO> findAllImages() {
        List<ProductImageDTO> allImages = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT id, name, link, productId, nameInStorage, createdDate, ")
                .append("createdBy, modifiedDate, modifiedBy ")
                .append("FROM images ");

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = OpenConnectionUtil.openConnection();
            preparedStatement = connection.prepareStatement(sql.toString());
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                ProductImageDTO imageDTO = new ProductImageDTO();
                imageDTO.setId(resultSet.getInt("id"));
                imageDTO.setName(resultSet.getString("name"));
                imageDTO.setLink(resultSet.getString("link"));
                imageDTO.setProductId(resultSet.getInt("productId"));
                imageDTO.setNameInStorage(resultSet.getString("nameInStorage"));
                imageDTO.setCreatedDate(resultSet.getTimestamp("createdDate"));
                imageDTO.setCreatedBy(resultSet.getString("createdBy"));
                imageDTO.setModifiedDate(resultSet.getTimestamp("modifiedDate"));
                imageDTO.setModifiedBy(resultSet.getString("modifiedBy"));

                allImages.add(imageDTO);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            CloseResourceUtil.closeResource(resultSet, preparedStatement, connection);
        }
        return allImages;
    }

    public void updateImage(ProductImageDTO image) {
        String sql = "UPDATE images SET name = ?, link = ?, productId = ?, modifiedBy = ? " +
                "WHERE id = ?";

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = OpenConnectionUtil.openConnection();
            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement(sql);

            SetParameterUtil.setParameter(preparedStatement, image.getName(), image.getLink(), image.getProductId(),
                    image.getModifiedBy(), image.getId());
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        } finally {
            CloseResourceUtil.closeNotUseRS(preparedStatement, connection);
        }
    }

    public void updateImageNotPart(ProductImageDTO image) {
        String sql = "UPDATE images SET name = ?, link = ?, productId = ?, modifiedDate = ?, modifiedBy = ? " +
                "WHERE id = ?";

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = OpenConnectionUtil.openConnection();
            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement(sql);

            SetParameterUtil.setParameter(preparedStatement, image.getName(), image.getLink(), image.getProductId(),
                    image.getModifiedDate(), image.getModifiedBy(),
                    image.getId());
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        } finally {
            CloseResourceUtil.closeNotUseRS(preparedStatement, connection);
        }
    }

    public String findNameInStorageById(int id) {
        String sql = "SELECT nameInStorage FROM images WHERE id = ?";

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = OpenConnectionUtil.openConnection();
            preparedStatement = connection.prepareStatement(sql);
            SetParameterUtil.setParameter(preparedStatement, id);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getString("nameInStorage");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            CloseResourceUtil.closeResource(resultSet, preparedStatement, connection);
        }
        return null;
    }

    public int deleteImage(int id) {
        int affectRows = -1;
        String sql = "DELETE FROM images WHERE id = ?";

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = OpenConnectionUtil.openConnection();
            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement(sql);
            SetParameterUtil.setParameter(preparedStatement, id);
            affectRows = preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
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

    public ProductImageDTO findOneByProductId(int productId) {
        ProductImageDTO imageDTO = new ProductImageDTO();
        String sql = "SELECT id, name, link, productId FROM images " +
                "WHERE productId = ? " +
                "LiMIT 1";

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = OpenConnectionUtil.openConnection();
            preparedStatement = connection.prepareStatement(sql);
            SetParameterUtil.setParameter(preparedStatement, productId);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                imageDTO.setId(resultSet.getInt("id"));
                imageDTO.setName(resultSet.getString("name"));
                imageDTO.setLink(resultSet.getString("link"));
                imageDTO.setProductId(resultSet.getInt("productId"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            CloseResourceUtil.closeResource(resultSet, preparedStatement, connection);
        }
        return imageDTO;
    }

    public List<ProductImageDTO> getThumbnailByProductId(int productId) {
        List<ProductImageDTO> thumbnail = new ArrayList<>();
        String sql = "SELECT id, name, link, productId FROM images " +
                "WHERE productId = ? " +
                "LiMIT 1";

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = OpenConnectionUtil.openConnection();
            preparedStatement = connection.prepareStatement(sql);
            SetParameterUtil.setParameter(preparedStatement, productId);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                ProductImageDTO imageDTO = new ProductImageDTO();
                imageDTO.setId(resultSet.getInt("id"));
                imageDTO.setName(resultSet.getString("name"));
                imageDTO.setLink(resultSet.getString("link"));
                imageDTO.setProductId(resultSet.getInt("productId"));

                thumbnail.add(imageDTO);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            CloseResourceUtil.closeResource(resultSet, preparedStatement, connection);
        }
        return thumbnail;
    }

    public List<ProductImageDTO> findImagesByProductId(int productId) {
        List<ProductImageDTO> imageDTOs = new ArrayList<>();
        String query = "SELECT id, link FROM images WHERE productId = ?";

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = OpenConnectionUtil.openConnection();
            preparedStatement = connection.prepareStatement(query);
            SetParameterUtil.setParameter(preparedStatement, productId);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                ProductImageDTO imageDTO = new ProductImageDTO();
                imageDTO.setId(resultSet.getInt("id"));
                imageDTO.setLink(resultSet.getString("link"));
                imageDTOs.add(imageDTO);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            CloseResourceUtil.closeResource(resultSet, preparedStatement, connection);
        }

        return imageDTOs;
    }
}
