package cs.toronto.edu.utils;

import cs.toronto.edu.models.Account;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Helper {
    public static Account my_account = null;

    public static Connection conn = null;

    public static Account getAccount() {
        return my_account;
    }

    public static void setAccount(Account my_account) {
        Helper.my_account = my_account;
    }

    public static void getConnection() {
        // Register the PostgreSQL driver
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        // Connect to the database
        try {
            conn = DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5433/mydb", "postgres", "postgres");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Statement getStmt(){
        Statement stmt = null;

        // Create a statement object
        try {
            stmt = conn.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stmt;
    }
}
