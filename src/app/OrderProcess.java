package app;

import java.sql.*;
import java.util.Scanner;

public class OrderProcess {

    Scanner scanner = new Scanner(System.in);

    public void addProductToOrder(int customerId, Connection connection) {
        try {
            int choice = promptUserToChooseProduct(connection);
            int orderId = checkIfOrderExists(customerId, choice, connection);
            addProductToOrderUsingProcedure(customerId, orderId, choice, connection);
            System.out.println("Order placed successfully!");
        } catch (SQLException e) {
            if (e.getSQLState().startsWith("45")) {
                System.out.println("Out of stock");
            } else {
                e.printStackTrace();
            }
        }
    }

    private int checkIfOrderExists(int customerId, int choice, Connection connection) throws SQLException {
        PreparedStatement checkOrder = connection.prepareStatement(
                "SELECT order_id " +
                        "FROM orders " +
                        "WHERE customer_id = ? " +
                        "AND product_id = ?");
        checkOrder.setInt(1, customerId);
        checkOrder.setInt(2, choice);
        ResultSet rs = checkOrder.executeQuery();
        int order_id = 0;
        if (rs.next()) {
            order_id = rs.getInt("order_id");
        }
        return order_id;
    }

    private void addProductToOrderUsingProcedure(int customerId, int orderId, int choice, Connection connection) throws SQLException {
        CallableStatement cs = connection.prepareCall("{CALL AddToCart(?, ?, ?)}");
        cs.setInt(1, customerId);
        cs.setInt(2, orderId);
        cs.setInt(3, choice);
        cs.execute();
    }

    private int promptUserToChooseProduct(Connection connection) {
        try {
            PreparedStatement ps = connection.prepareStatement(
                "SELECT p.product_id, p.name, p.color, p.price, p.size, c.name AS category " +
                        "FROM products p " +
                        "JOIN categories c ON p.category_id = c.category_id " +
                        "WHERE p.stock_quantity > 0");
        ResultSet rs = ps.executeQuery();
        int i = 1;
        while (rs.next()) {
            System.out.println(i +
                    ". " + rs.getString("name") +
                    " - " + rs.getString("category") +
                    " - " + rs.getString("color") +
                    " - " + rs.getDouble("size") +
                    " - " + rs.getFloat("price") + "kr");
            i++;
        }
        System.out.print("Choose a product to add to your order: ");
        return scanner.nextInt();
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }
}