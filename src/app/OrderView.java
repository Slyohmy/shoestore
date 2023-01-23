package app;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderView {

    public void viewCustomerOrders(int customerId, Connection connection) {

        try {
            String query =
                    "SELECT p.name, p.color, p.size, o.quantity, o.total_price " +
                    "FROM orders o " +
                    "JOIN products p " +
                    "ON o.product_id = p.product_id " +
                    "WHERE o.customer_id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, customerId);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                String productName = rs.getString("name");
                String color = rs.getString("color");
                double size = rs.getDouble("size");
                int quantity = rs.getInt("quantity");
                double totalPrice = rs.getDouble("total_price");
                System.out.println(
                        "Product name: " + productName +
                        ", Color: " + color +
                        ", Size: " + size +
                        ", Quantity: " + quantity +
                        ", Total price: " + totalPrice + " kr");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
