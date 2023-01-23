package app;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserValidation {
    public int validateUser(String username, String password, Connection connection) {
        try {
            String query =
                    "SELECT customer_id " +
                    "FROM customers " +
                    "WHERE username = ? " +
                    "AND password = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return rs.getInt("customer_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
}