package app;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Main {
    public static void main(String[] args) {
        OrderProcess orderProcess = new OrderProcess();
        OrderView orderView = new OrderView();
        UserValidation userValidation = new UserValidation();
        Connection connection = null;
        try {
            Properties prop = new Properties();
            prop.load(new FileInputStream("src/app/Settings.properties"));
            connection = DriverManager.getConnection(
                    prop.getProperty("connectionString"),
                    prop.getProperty("name"),
                    prop.getProperty("password"));
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }

        System.out.print("Enter username: ");
        String username = orderProcess.scanner.next();
        System.out.print("Enter password: ");
        String password = orderProcess.scanner.next();

        int customerId = userValidation.validateUser(username, password, connection);
        if (customerId > 0) {
            while (true) {
                System.out.println(
                        "1. Make an order\n" +
                        "2. View orders\n" +
                        "3. Exit application\n" +
                        "Enter your choice: ");

                int choice = orderProcess.scanner.nextInt();
                switch (choice) {
                    case 1 -> orderProcess.addProductToOrder(customerId, connection);
                    case 2 -> orderView.viewCustomerOrders(customerId, connection);
                    case 3 -> {
                        System.out.println("Exiting Application");
                        return;
                    }
                    default -> System.out.println("Invalid choice, please select again");
                }
            }
        } else {
            System.out.println("Invalid username or password.");
        }
    }
}

