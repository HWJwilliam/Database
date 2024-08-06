package cs.toronto.edu.controller;

import cs.toronto.edu.utils.Helper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;

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

    /**
     * A helper for calculating beta, COV, and show the graph given the interval user inputted
     * Computation is done on Close attribute
     * Return a Hashtable with Timestamp corresponding to its Close value
     * __________________________
     */
    public static Hashtable<String, Float> betacovgraph(String input) {
        Statement stmt = Helper.getStmt();
        ResultSet rs;
        String stock = "";
        String start = "";
        String end = "";
        try {
            String[] info = input.split(",");
            stock = info[0];
            start = info[1];
            end = info[2];
        } catch (Exception e) {
            System.out.println("Invalid input");
            return null;
        }

        // Beta

        // COV

        String sqlSelect = "SELECT VAR_SAMP(Close) / AVG(Close) AS COV FROM Stock_hist_info WHERE Symbol = '" + stock +
                "' AND Timestamp BETWEEN '" + start + "' AND '" + end +"';";
        float cov = 0;
        try {
            rs = stmt.executeQuery(sqlSelect);
            if (rs.next()) {
                cov = rs.getFloat("COV");
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        System.out.println("The COV of the stock " + stock + " with your selected start and end date is " + cov);

        // Graph
        Hashtable<String, Float> graph = new Hashtable<String, Float>();
        sqlSelect = "SELECT Timestamp, Close FROM Stock_hist_info WHERE Close IS NOT NULL AND Symbol = '" + stock +
                "' AND Timestamp BETWEEN '" + start + "' AND '" + end +"' ORDER BY Timestamp;";
        try {
            rs = stmt.executeQuery(sqlSelect);
            while (rs.next()) {
                graph.put(rs.getString("Timestamp"), rs.getFloat("Close"));
                System.out.println(rs.getFloat("Close"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        return graph;
    }
}
