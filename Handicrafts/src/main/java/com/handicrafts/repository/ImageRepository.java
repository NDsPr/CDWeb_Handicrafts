package com.handicrafts.repository;

import com.ltw.bean.ProductImageBean;
import com.ltw.util.CloseResourceUtil;
import com.ltw.util.OpenConnectionUtil;
import com.ltw.util.SetParameterUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ImageRepository {
    // TODO: Cần thêm status cho ảnh (Sẽ làm sau)
    public ProductImageBean findImageById(int id) {
        ProductImageBean productImageBean = null;
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
                productImageBean = new ProductImageBean();
                productImageBean.setId(resultSet.getInt("id"));
                productImageBean.setName(resultSet.getString("name"));
                productImageBean.setLink(resultSet.getString("link"));
                productImageBean.setProductId(resultSet.getInt("productId"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            CloseResourceUtil.closeResource(resultSet, preparedStatement, connection);
        }
        return productImageBean;
    }

    public int insertProductImage(ProductImageBean image) {
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
            // Do đây là non-query (Non-query là INSERT, UPDATE, DELETE trong SQL) nên cần setAutoCommit(false)...
            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);

            SetParameterUtil.setParameter(preparedStatement, image.getName(), image.getLink(), image.getProductId(),
                                                image.getCreatedBy(), image.getModifiedBy());
            id = preparedStatement.executeUpdate();

            // ... và commit ở đây...
            connection.commit();
        } catch (SQLException e) {
            try {
                // ... cũng như rollback khi bị lỗi SQL ở đây.
                connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        } finally {
            CloseResourceUtil.closeNotUseRS(preparedStatement, connection);
        }
        return id;
    }

    public List<ProductImageBean> findAllImages() {
        List<ProductImageBean> allImages = new ArrayList<>();
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
                ProductImageBean productImageBean = new ProductImageBean();
                productImageBean.setId(resultSet.getInt("id"));
                productImageBean.setName(resultSet.getString("name"));
                productImageBean.setLink(resultSet.getString("link"));
                productImageBean.setProductId(resultSet.getInt("productId"));
                productImageBean.setNameInStorage(resultSet.getString("nameInStorage"));
                productImageBean.setCreatedDate(resultSet.getTimestamp("createdDate"));
                productImageBean.setCreatedBy(resultSet.getString("createdBy"));
                productImageBean.setModifiedDate(resultSet.getTimestamp("modifiedDate"));
                productImageBean.setModifiedBy(resultSet.getString("modifiedBy"));

                allImages.add(productImageBean);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            CloseResourceUtil.closeResource(resultSet, preparedStatement, connection);
        }
        return allImages;
    }


    public void updateImage(ProductImageBean image) {
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

    public void updateImageNotPart(ProductImageBean image) {
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

    public ProductImageBean findOneByProductId(int productId) {
        ProductImageBean productImageBean = new ProductImageBean();
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
                productImageBean.setId(resultSet.getInt("id"));
                productImageBean.setName(resultSet.getString("name"));
                productImageBean.setLink(resultSet.getString("link"));
                productImageBean.setProductId(resultSet.getInt("productId"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            CloseResourceUtil.closeResource(resultSet, preparedStatement, connection);
        }
        return productImageBean;
    }

    public List<ProductImageBean> getThumbnailByProductId(int productId) {
        List<ProductImageBean> thumbnail = new ArrayList<>();
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
                ProductImageBean productImageBean = new ProductImageBean();
                productImageBean.setId(resultSet.getInt("id"));
                productImageBean.setName(resultSet.getString("name"));
                productImageBean.setLink(resultSet.getString("link"));
                productImageBean.setProductId(resultSet.getInt("productId"));

                thumbnail.add(productImageBean);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            CloseResourceUtil.closeResource(resultSet, preparedStatement, connection);
        }
        return thumbnail;
    }

    public List<ProductImageBean> findImagesByProductId(int productId) {
        List<ProductImageBean> productImageBeans = new ArrayList<>();
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
                ProductImageBean productImageBean = new ProductImageBean();
                productImageBean.setId(resultSet.getInt("id"));
                productImageBean.setLink(resultSet.getString("link"));
                productImageBeans.add(productImageBean);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            CloseResourceUtil.closeResource(resultSet, preparedStatement, connection);
        }

        return productImageBeans;
    }
}