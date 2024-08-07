package cs.toronto.edu.controller;

import cs.toronto.edu.utils.Helper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.text.SimpleDateFormat;

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
    public static Hashtable<Date, Float> betacovgraph(String input) {
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
        String sqlSelect = "WITH LIST_A AS (SELECT (Close - LAG(Close) OVER (ORDER BY Timestamp))/NULLIF(LAG(Close) " +
                "OVER (ORDER BY Timestamp), 0) AS A, ROW_NUMBER() OVER () AS rn FROM (SELECT Close, Timestamp FROM " +
                "Stock_hist_info WHERE Symbol = '" + stock + "' AND Timestamp IN (SELECT Timestamp FROM " +
                "Stock_hist_info WHERE Timestamp BETWEEN '" + start + "' AND '" + end + "' AND Symbol = '" + stock +
                "' ))), LIST_B AS (SELECT (Total - LAG(Total) OVER (ORDER BY Timestamp))/NULLIF(LAG(Total) " +
                "OVER (ORDER BY Timestamp), 0) AS B, ROW_NUMBER() OVER () AS rn FROM (SELECT SUM(Volume * Close) " +
                "as Total, Timestamp FROM Stock_hist_info WHERE Timestamp IN (SELECT Timestamp FROM Stock_hist_info " +
                "WHERE Timestamp BETWEEN '" + start + "' AND '" + end + "' AND Symbol = '" + stock + "') GROUP BY " +
                "Timestamp ORDER BY Timestamp)) SELECT COVAR_SAMP(A, B)/VAR_SAMP(B) AS BETA FROM " +
                "(SELECT A, B FROM LIST_A JOIN LIST_B ON LIST_A.rn = LIST_B.rn);";
        float beta = 0;
        try {
            rs = stmt.executeQuery(sqlSelect);
            if (rs.next()) {
                beta = rs.getFloat("BETA");
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        System.out.println("The BETA of the stock " + stock + " with your selected start and end date is " + beta);

        // COV
        sqlSelect = "SELECT VAR_SAMP(Close) / AVG(Close) AS COV FROM Stock_hist_info WHERE Symbol = '" + stock +
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
        Hashtable<Date, Float> graph = new Hashtable<Date, Float>();
        sqlSelect = "SELECT Timestamp, Close FROM Stock_hist_info WHERE Close IS NOT NULL AND Symbol = '" + stock +
                "' AND Timestamp BETWEEN '" + start + "' AND '" + end +"' ORDER BY Timestamp;";
        try {
            rs = stmt.executeQuery(sqlSelect);
            while (rs.next()) {
                graph.put(rs.getDate("Timestamp"), rs.getFloat("Close"));
                System.out.println(rs.getFloat("Close"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        return graph;
    }

    /**
     * A helper for calculating prediction
     * Return a Hashtable with Timestamp corresponding to its Close value
     * __________________________
     */
    public static Hashtable<Date, Float> predict(String input) {
        Statement stmt = Helper.getStmt();
        ResultSet rs;
        try {
            String[] info = input.split(",");
            String sqlSelect = "SELECT MAX(Timestamp) AS Timestamp FROM Stock_hist_info WHERE Symbol = '" + info[0] + "';";
            rs = stmt.executeQuery(sqlSelect);
            if (rs.next()) {
                Date date = rs.getDate("Timestamp");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date input_date = sdf.parse(info[1]);
                if (date.compareTo(input_date) >= 0) {
                    System.out.println("You cannot predict stock that is before the latest date of information available");
                    return null;
                } else {
                    Hashtable<Date, Float> graph = new Hashtable<Date, Float>();
                    Float[] predictor = new Float[5];
                    int count = 0;
                    sqlSelect = "SELECT Timestamp, Close FROM (SELECT Timestamp, Close FROM Stock_hist_info WHERE " +
                            "Symbol = '" + info[0] + "' ORDER BY Timestamp DESC LIMIT 5) ORDER BY Timestamp ASC;";
                    rs = stmt.executeQuery(sqlSelect);
                    while (rs.next()) {
                        graph.put(rs.getDate("Timestamp"), rs.getFloat("Close"));
                        predictor[count] = rs.getFloat("Close");
                        count = (count + 1) % 5;
                    }
                    Calendar calendar = Calendar.getInstance();
                    Random random = new Random();
                    while (date.compareTo(input_date) < 0) {;
                        calendar.setTime(date);
                        calendar.add(Calendar.DAY_OF_MONTH, 1);
                        date = calendar.getTime();
                        float estimate = (float) ((predictor[0] + predictor[1] + predictor[2] + predictor[3] +
                                predictor[4])/5 + random.nextGaussian());
                        graph.put(date, estimate);
                        predictor[count] = estimate;
                        count = (count + 1) % 5;
                    }
                    return graph;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Invalid Input");
        }
        return null;
    }
}
