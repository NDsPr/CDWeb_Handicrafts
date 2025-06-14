package com.handicrafts.util;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;

public class SetParameterUtil {
    public static void setParameter(PreparedStatement statement, Object... parameters) {
        // TODO Auto-generated method stub
        for (int i = 0; i < parameters.length; i++) {
            Object parameter = parameters[i];
            int index = i + 1;
            try {
                if (parameter instanceof Integer) {
                    statement.setInt(index, (int) parameter);
                } else if (parameter instanceof String) {
                    statement.setString(index, (String) parameter);
                } else if (parameter instanceof Double) {
                    statement.setDouble(index, (double) parameter);
                } else if (parameter instanceof Timestamp) {
                    statement.setTimestamp(index, (Timestamp) parameter);
                } else if (parameter == null) {
                    statement.setNull(index, Types.NULL);
                }
            } catch (SQLException e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        }
    }
}
