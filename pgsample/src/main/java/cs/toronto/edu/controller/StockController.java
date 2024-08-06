package cs.toronto.edu.controller;

import cs.toronto.edu.utils.Helper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class StockController {

    /**
     * A helper for checking if a stock is contained in the database from the backend
     * __________________________
     */
    public static boolean check_contain_stock(String stock) {
        Statement stmt = Helper.getStmt();
        String sqlSelect = "SELECT * FROM Stocks WHERE Symbol = '" + stock + "';";
        ResultSet rs = null;
        try {
            rs = stmt.executeQuery(sqlSelect);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (rs.next()) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
