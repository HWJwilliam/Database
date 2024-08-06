package cs.toronto.edu.controller;

import cs.toronto.edu.models.StockHistory;
import cs.toronto.edu.utils.Helper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

public class StockHistoryController {
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
