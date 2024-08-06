package cs.toronto.edu.controller;

import cs.toronto.edu.models.StockHistory;
import cs.toronto.edu.utils.Helper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

public class StockHistoryController {

    public static void add_stock_history(String input) {
        Statement stmt = Helper.getStmt();
        String[] info = input.split(",");
        ResultSet rs = null;
        try {
            String date = info[0];
            String stock = info[1];
            int open = Integer.parseInt(info[2]);
            int close = Integer.parseInt(info[3]);
            int high = Integer.parseInt(info[4]);
            int low = Integer.parseInt(info[5]);
            int volume = Integer.parseInt(info[6]);
            String sqlSelect = "SELECT * FROM Stocks WHERE Symbol = '" + stock + "';";
            rs = stmt.executeQuery(sqlSelect);
            if (!rs.next()) {
                System.out.println("You are adding a new stock");
                String sqlUpdate = "INSERT INTO Stocks (Symbol) VALUES ('" + stock + "');";
                stmt.executeUpdate(sqlUpdate);
                sqlUpdate = "INSERT INTO Stock_hist_info (Timestamp, Symbol, Open, Close, High, Low, Volume) " +
                        "VALUES ('" + date + "', '" + stock + "', " + open + ", " + close + ", " + high + ", " +
                        low + ", " + volume + ");";
                stmt.executeUpdate(sqlUpdate);
                System.out.println("Stock added successfully");
            } else {
                sqlSelect = "SELECT * FROM Stock_hist_info WHERE Timestamp = '" + date + "' AND Symbol = '"
                        + stock + "';";
                rs = stmt.executeQuery(sqlSelect);
                if (!rs.next()) {
                    System.out.println("You are adding a new information for a stock");
                    String sqlUpdate = "INSERT INTO Stock_hist_info (Timestamp, Symbol, Open, Close, High, Low, Volume) " +
                            "VALUES ('" + date + "', '" + stock + "', " + open + ", " + close + ", " + high + ", " +
                            low + ", " + volume + ";";
                    stmt.executeUpdate(sqlUpdate);
                    System.out.println("Stock added successfully");
                } else {
                    System.out.println("You are updating a new information for a stock");
                    String sqlUpdate = "UPDATE Stock_hist_info SET Open = " + open + ", Close = " + close + ", High = " + high + ", Low = " + low
                            + ", Volume = " + volume + " WHERE Timestamp = '" + date + "' AND Symbol = '" + stock + "';";
                    stmt.executeUpdate(sqlUpdate);
                    System.out.println("Stock added successfully");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Invalid input");
        }
    }
    public static ArrayList<StockHistory> findBySymbol(String symbol){
        Statement stmt = Helper.getStmt();
        String sqlSelect = "SELECT * FROM Stock_hist_info WHERE Symbol = '" + symbol + "';";
        ResultSet rs = null;
        ArrayList<StockHistory> results = new ArrayList<>();
        try {
            rs = stmt.executeQuery(sqlSelect);
            while (rs.next()) {
                results.add(new StockHistory(
                        rs.getString("name"),
                        rs.getDate("timestamp"),
                        rs.getFloat("open"),
                        rs.getFloat("close"),
                        rs.getFloat("high"),
                        rs.getFloat("low"),
                        rs.getInt("volume")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }
    public static StockHistory findBySymbolAndTimestamp(String symbol, String timestamp){
        Statement stmt = Helper.getStmt();
        String sqlSelect = "SELECT * FROM Stock_hist_info WHERE Symbol = '" + symbol + "' and Timestamp = '" + timestamp + "' ;";
        ResultSet rs = null;
        StockHistory result = null;
        try {
            rs = stmt.executeQuery(sqlSelect);
            while (rs.next()) {
                result = new StockHistory(
                        rs.getString("name"),
                        rs.getDate("timestamp"),
                        rs.getFloat("open"),
                        rs.getFloat("close"),
                        rs.getFloat("high"),
                        rs.getFloat("low"),
                        rs.getInt("volume")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}
