package com.handicrafts.repository;

import com.handicrafts.dto.ContactDTO;
import com.handicrafts.util.OpenConnectionUtil;
import com.handicrafts.util.SetParameterUtil;
import org.springframework.stereotype.Repository;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;
@Repository
public class ContactRepository {
    public int createContact(ContactDTO contactDTO) {
        int id = -1;
        String sql = "INSERT INTO contacts (email, firstName, lastName, message, status) VALUES (?, ?, ?, ?, 1)";

        try (Connection connection = OpenConnectionUtil.openConnection();
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
            e.printStackTrace();
        }
        return id;
    }

    public ContactDTO findContactById(int id) {
        ContactDTO contact = null;
        String sql = "SELECT * FROM contacts WHERE id = ?";

        try (Connection connection = OpenConnectionUtil.openConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            SetParameterUtil.setParameter(preparedStatement, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                contact = extractContactFromResultSet(resultSet);
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return contact;
    }

    public List<ContactDTO> findAllContacts() {
        List<ContactDTO> contactList = new ArrayList<>();
        String sql = "SELECT * FROM contacts";

        try (Connection connection = OpenConnectionUtil.openConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                contactList.add(extractContactFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return contactList;
    }

    public void updateContact(ContactDTO contact) {
        String sql = "UPDATE contacts SET firstName = ?, lastName = ?, message = ?, status = ?, modifiedDate = ?, modifiedBy = ? WHERE id = ?";

        try (Connection connection = OpenConnectionUtil.openConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            connection.setAutoCommit(false);
            SetParameterUtil.setParameter(preparedStatement, contact.getFirstName(), contact.getLastName(),
                    contact.getMessage(), contact.getStatus(), contact.getModifiedDate(), contact.getModifiedBy(), contact.getId());
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int deleteContact(int id) {
        String sql = "DELETE FROM contacts WHERE id = ?";
        int affectedRows = -1;

        try (Connection connection = OpenConnectionUtil.openConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            connection.setAutoCommit(false);
            SetParameterUtil.setParameter(preparedStatement, id);
            affectedRows = preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
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

    // The rest methods (getContactsDatatable, getRecordsTotal, getRecordsFiltered) should also follow the same ContactDTO refactor.
}
