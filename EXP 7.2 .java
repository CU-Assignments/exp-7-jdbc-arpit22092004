import java.sql.*;
import java.util.Scanner;

public class ProductCRUD {
    static final String URL = "jdbc:mysql://localhost:3306/your_database";
    static final String USER = "your_username";
    static final String PASSWORD = "your_password";

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Scanner scanner = new Scanner(System.in)) {

            while (true) {
                System.out.println("\nProduct CRUD Operations:");
                System.out.println("1. Create Product");
                System.out.println("2. Read Products");
                System.out.println("3. Update Product");
                System.out.println("4. Delete Product");
                System.out.println("5. Exit");
                System.out.print("Enter your choice: ");
                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1: createProduct(conn, scanner); break;
                    case 2: readProducts(conn); break;
                    case 3: updateProduct(conn, scanner); break;
                    case 4: deleteProduct(conn, scanner); break;
                    case 5: System.exit(0);
                    default: System.out.println("Invalid choice. Try again!");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static void createProduct(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Enter Product Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Price: ");
        double price = scanner.nextDouble();
        System.out.print("Enter Quantity: ");
        int quantity = scanner.nextInt();

        String query = "INSERT INTO Product (ProductName, Price, Quantity) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            conn.setAutoCommit(false);
            stmt.setString(1, name);
            stmt.setDouble(2, price);
            stmt.setInt(3, quantity);
            stmt.executeUpdate();
            conn.commit();
            System.out.println("Product added successfully!");
        } catch (SQLException e) {
            conn.rollback();
            e.printStackTrace();
        }
    }

    static void readProducts(Connection conn) throws SQLException {
        String query = "SELECT * FROM Product";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            System.out.println("\nProductID | ProductName | Price | Quantity");
            while (rs.next()) {
                System.out.println(rs.getInt("ProductID") + " | " + rs.getString("ProductName") +
                        " | " + rs.getDouble("Price") + " | " + rs.getInt("Quantity"));
            }
        }
    }

    static void updateProduct(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Enter Product ID to update: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter new Price: ");
        double price = scanner.nextDouble();
        System.out.print("Enter new Quantity: ");
        int quantity = scanner.nextInt();

        String query = "UPDATE Product SET Price = ?, Quantity = ? WHERE ProductID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            conn.setAutoCommit(false);
            stmt.setDouble(1, price);
            stmt.setInt(2, quantity);
            stmt.setInt(3, id);
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                conn.commit();
                System.out.println("Product updated successfully!");
            } else {
                System.out.println("Product not found.");
            }
        } catch (SQLException e) {
            conn.rollback();
            e.printStackTrace();
        }
    }

    static void deleteProduct(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Enter Product ID to delete: ");
        int id = scanner.nextInt();

        String query = "DELETE FROM Product WHERE ProductID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            conn.setAutoCommit(false);
            stmt.setInt(1, id);
            int rowsDeleted = stmt.executeUpdate();
            if (rowsDeleted > 0) {
                conn.commit();
                System.out.println("Product deleted successfully!");
            } else {
                System.out.println("Product not found.");
            }
        } catch (SQLException e) {
            conn.rollback();
            e.printStackTrace();
        }
    }
}
