package com.handicrafts.repository;

import com.handicrafts.dto.ContactDTO;
import com.handicrafts.util.OpenConnectionUtil;
import com.handicrafts.util.SetParameterUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ContactRepository {
    private static final Logger logger = LoggerFactory.getLogger(ContactRepository.class);

    private final OpenConnectionUtil openConnectionUtil;

    @Autowired
    public ContactRepository(OpenConnectionUtil openConnectionUtil) {
        this.openConnectionUtil = openConnectionUtil;
    }

    public int createContact(ContactDTO contactDTO) {
        int id = -1;
        String sql = "INSERT INTO contacts (email, firstName, lastName, message, status) VALUES (?, ?, ?, ?, 1)";

        try (Connection connection = openConnectionUtil.openConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            connection.setAutoCommit(false);
            SetParameterUtil.setParameter(preparedStatement, contactDTO.getEmail(), contactDTO.getFirstName(),
                    contactDTO.getLastName(), contactDTO.getMessage());

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                ResultSet resultSet = preparedStatement.getGeneratedKeys();
                if (resultSet.next()) {
                    id = resultSet.getInt(1);
                }
                resultSet.close();
            }
            connection.commit();
        } catch (SQLException e) {
            logger.error("Error creating contact", e);
        }
        return id;
    }

    public ContactDTO findContactById(int id) {
        ContactDTO contact = null;
        String sql = "SELECT * FROM contacts WHERE id = ?";

        try (Connection connection = openConnectionUtil.openConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            SetParameterUtil.setParameter(preparedStatement, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    contact = extractContactFromResultSet(resultSet);
                }
            }
        } catch (SQLException e) {
            logger.error("Error finding contact by ID: {}", id, e);
        }
        return contact;
    }

    public List<ContactDTO> getContactsDatatable(int start, int length, String columnOrder, String orderDir, String searchValue) {
        List<ContactDTO> contacts = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM contacts");

        if (searchValue != null && !searchValue.trim().isEmpty()) {
            sql.append(" WHERE email LIKE ? OR firstName LIKE ? OR lastName LIKE ? OR message LIKE ?");
        }
        sql.append(" ORDER BY ").append(columnOrder).append(" ").append(orderDir);
        sql.append(" LIMIT ?, ?");

        try (Connection connection = openConnectionUtil.openConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql.toString())) {

            int index = 1;
            if (searchValue != null && !searchValue.trim().isEmpty()) {
                String query = "%" + searchValue + "%";
                for (int i = 0; i < 4; i++) {
                    preparedStatement.setString(index++, query);
                }
            }
            preparedStatement.setInt(index++, start);
            preparedStatement.setInt(index, length);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    contacts.add(extractContactFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            logger.error("Error getting contacts datatable. Start: {}, Length: {}, Order: {}, Direction: {}, Search: {}",
                    start, length, columnOrder, orderDir, searchValue, e);
        }
        return contacts;
    }

    public int getRecordsTotal() {
        int count = 0;
        String sql = "SELECT COUNT(*) FROM contacts";
        try (Connection conn = openConnectionUtil.openConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            logger.error("Error getting total records count", e);
        }
        return count;
    }

    public int getRecordsFiltered(String searchValue) {
        int count = 0;
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM contacts");

        if (searchValue != null && !searchValue.trim().isEmpty()) {
            sql.append(" WHERE email LIKE ? OR firstName LIKE ? OR lastName LIKE ? OR message LIKE ?");
        }

        try (Connection conn = openConnectionUtil.openConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            if (searchValue != null && !searchValue.trim().isEmpty()) {
                String query = "%" + searchValue + "%";
                for (int i = 1; i <= 4; i++) {
                    ps.setString(i, query);
                }
            }

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    count = rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            logger.error("Error getting filtered records count with search value: {}", searchValue, e);
        }
        return count;
    }

    public int deleteContact(int id) {
        String sql = "DELETE FROM contacts WHERE id = ?";
        int affectedRows = -1;

        try (Connection connection = openConnectionUtil.openConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            connection.setAutoCommit(false);
            SetParameterUtil.setParameter(preparedStatement, id);
            affectedRows = preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            logger.error("Error deleting contact with ID: {}", id, e);
        }
        return affectedRows;
    }

    private ContactDTO extractContactFromResultSet(ResultSet rs) throws SQLException {
        ContactDTO contact = new ContactDTO();
        contact.setId(rs.getInt("id"));
        contact.setEmail(rs.getString("email"));
        contact.setFirstName(rs.getString("firstName"));
        contact.setLastName(rs.getString("lastName"));
        contact.setTitle(rs.getString("title"));
        contact.setMessage(rs.getString("message"));
        contact.setStatus(rs.getInt("status"));
        contact.setCreatedDate(rs.getTimestamp("createdDate"));
        contact.setCreatedBy(rs.getString("createdBy"));
        contact.setModifiedDate(rs.getTimestamp("modifiedDate"));
        contact.setModifiedBy(rs.getString("modifiedBy"));
        return contact;
    }

    public int updateContact(ContactDTO contactDTO) {
        String sql = "UPDATE contacts SET email = ?, firstName = ?, lastName = ?, message = ?, status = ?, modifiedDate = ?, modifiedBy = ? WHERE id = ?";
        int affectedRows = -1;

        try (Connection connection = openConnectionUtil.openConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            connection.setAutoCommit(false);

            SetParameterUtil.setParameter(preparedStatement,
                    contactDTO.getEmail(),
                    contactDTO.getFirstName(),
                    contactDTO.getLastName(),
                    contactDTO.getMessage(),
                    contactDTO.getStatus(),
                    contactDTO.getModifiedDate(),
                    contactDTO.getModifiedBy(),
                    contactDTO.getId());

            affectedRows = preparedStatement.executeUpdate();
            connection.commit();

        } catch (SQLException e) {
            logger.error("Error updating contact with ID: {}", contactDTO.getId(), e);
        }

        return affectedRows;
    }
}
