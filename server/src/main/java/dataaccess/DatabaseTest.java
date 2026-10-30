package dataaccess;

public class DatabaseTest {
    public static void main(String[] args) {
        try {
            DatabaseManager.createDatabase();

            try (var conn = DatabaseManager.getConnection()) {
                System.out.println("Connected successfully!");
                System.out.println("Database: " + conn.getCatalog());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}