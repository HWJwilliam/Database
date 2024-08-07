package cs.toronto.edu.controller;

import cs.toronto.edu.utils.Helper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Hashtable;

public class PortfolioController {

    /**
     * A helper for creating the portfolio in the backend
     * __________________________
     */
    public static String create_portfolio() {
        Statement stmt = Helper.getStmt();
        String portfolio_id = Helper.getAccount().getUsername() + new Timestamp(System.currentTimeMillis());
        ;
        String username = Helper.getAccount().getUsername();
        String sqlInsert = "INSERT INTO Portfolio (ID, Cash) " + "VALUES ('" + portfolio_id + "', " + 0 + ");";
        String sqlInsert2 = "INSERT INTO Have_portfolio (Account_username, Portfolio_ID) " + "VALUES ('" + username + "', '" +
                portfolio_id + "');";
        String sqlInsert3 = "INSERT INTO Stock_list (ID) " + "VALUES ('" + portfolio_id + "');";
        String sqlInsert4 = "INSERT INTO Creates (Account_username, Stock_list_ID) " + "VALUES ('" + username + "', '" +
                portfolio_id + "');";
        String sqlInsert5 = "INSERT INTO Have_stock_list (Stock_list_ID, Portfolio_ID) VALUES ('" + portfolio_id + "', '" +
                portfolio_id + "');";
        try {
            stmt.executeUpdate(sqlInsert);
            stmt.executeUpdate(sqlInsert2);
            stmt.executeUpdate(sqlInsert3);
            stmt.executeUpdate(sqlInsert4);
            stmt.executeUpdate(sqlInsert5);
            return portfolio_id;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * A helper for showing all the portfolio created by a user from the backend
     * __________________________
     */
    public static void show_portfolio() {
        Statement stmt = Helper.getStmt();
        String sqlSelect = "SELECT Portfolio_ID FROM Have_portfolio WHERE Account_username = '" +
                Helper.getAccount().getUsername() + "';";
        ResultSet rs = null;
        try {
            rs = stmt.executeQuery(sqlSelect);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            int count = 1;
            String str = "";
            while (rs.next()) {
                str = str + count + ". " + rs.getString("Portfolio_ID") + "\n";
                count += 1;
            }
            if (str.isEmpty()) {
                System.out.println("You currently have no created portfolios");
            } else {
                System.out.println("You currently have those portfolios: \n" + str);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * A helper for checking if a portfolio is in the stock list from the backend
     * __________________________
     */
    public static boolean check_contain_portfolio(String portfolio_id) {
        Statement stmt = Helper.getStmt();
        String sqlSelect = "SELECT * FROM Have_portfolio WHERE Account_username = '" +
                Helper.getAccount().getUsername() + "' AND Portfolio_ID = '" + portfolio_id + "';";
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
     * A helper for executing the transaction from the backend
     * __________________________
     */
    public static void execute_transaction(String input, String portfolio_id) {
        Statement stmt = Helper.getStmt();
        ResultSet rs = null;
        try {
            String[] info = input.split(",");
            String sqlSelect = "SELECT Close FROM Stock_hist_info WHERE Symbol = '" + info[3] + "' AND Timestamp = '" +
                    info[1] + "';";
            rs = stmt.executeQuery(sqlSelect);
            if (rs.next()) {
                int amount = Integer.parseInt(info[2]);
                float cost = amount * rs.getFloat("Close");
                if (info[0].equals("Buy")) {
                    String sqlSelect1 = "SELECT Cash FROM Portfolio WHERE ID = '" + portfolio_id + "';";
                    rs = stmt.executeQuery(sqlSelect1);
                    if (rs.next()) {
                        float cash = rs.getFloat("Cash");
                        if (cash >= cost) {
                            cash -= cost;
                            String sqlUpdate = "UPDATE Portfolio SET Cash = " + cash + " WHERE ID = '" +
                                    portfolio_id + "';";
                            stmt.executeUpdate(sqlUpdate);
                            String sqlSelect2 = "SELECT * FROM Bought WHERE Portfolio_ID = '" + portfolio_id +
                                    "' AND Stock_symbol = '" + info[3] + "';";
                            rs = stmt.executeQuery(sqlSelect2);
                            if (rs.next()) {
                                String sqlUpdate1 = "UPDATE Bought SET Share = Share + " + amount + " WHERE " +
                                        "Portfolio_ID = '" + portfolio_id + "' AND Stock_symbol = '" + info[3] + "');";
                                stmt.executeUpdate(sqlUpdate1);
                            } else {
                                String sqlUpdate2 = "INSERT INTO Bought (Portfolio_ID, Stock_symbol, Share) VALUES ('" +
                                        portfolio_id + "', '" + info[3] + "', " + amount + ");";
                                stmt.executeUpdate(sqlUpdate2);
                            }
                        } else {
                            System.out.println("Your fund is not sufficient to buy");
                        }
                    }
                } else if (info[0].equals("Sell")) {
                    String sqlSelect1 = "SELECT Share FROM Bought WHERE Portfolio_ID = '" + portfolio_id + "' " +
                            "AND Stock_symbol = '" + info[3] + "';";
                    rs = stmt.executeQuery(sqlSelect1);
                    if (rs.next()) {
                        int share = rs.getInt("Share");
                        if (share >= amount) {
                            share -= amount;
                            String sqlUpdate = "UPDATE Bought SET Share = " + share + " WHERE ID = '" +
                                    portfolio_id + "' AND Stock_syyymbol = '" + info[3] + "');";
                            stmt.executeUpdate(sqlUpdate);
                            String sqlUpdate1 = "UPDATE Portfolio SET Cash = Cash + " + cost + " WHERE ID = '" +
                                    portfolio_id + "');";
                            stmt.executeUpdate(sqlUpdate1);
                        } else {
                            System.out.println("You do not have enough stock bought to sell");
                        }
                    }
                } else {
                    System.out.println("Invalid input");
                }
            } else {
                System.out.println("There is no cost for close for selected date and stock, " +
                        "you need to update such information first and then try to do the transaction.\n" +
                        "You need the Closed price of " + info[3] + " on " + info[1] + ".");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Invalid input");
        }
    }

    /**
     * A helper for listing the portfolio from the backend
     * __________________________
     */
    public static void list_portfolio(String portfolio_id) {
        Statement stmt = Helper.getStmt();
        String sqlSelect = "SELECT Cash FROM Portfolio WHERE ID = '" + portfolio_id + "';";
        String sqlSelect1 = "SELECT ID FROM Portfolio WHERE ID != '" + portfolio_id + "';";
        ResultSet rs = null;
        try {
            rs = stmt.executeQuery(sqlSelect);
            if (rs.next()) {
                System.out.println("You currently have the following amount of funds: " + rs.getFloat("Cash"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            rs = stmt.executeQuery(sqlSelect1);
            String str = "";
            int count = 1;
            while (rs.next()) {
                str = str + count + ". " + rs.getString("ID") + "\n";
                count += 1;
            }
            if (str.isEmpty()) {
                System.out.println("There is no one you can transfer the money");
            } else {
                System.out.println("You can transfer the money to\n" + str);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * A helper for deposit, withdraw, and transfer from the backend
     * __________________________
     */
    public static void deposit_withdraw_transfer(String input, String portfolio_id) {
        Statement stmt = Helper.getStmt();
        try {
            String[] info = input.split(",");
            int cost = Integer.parseInt(info[1]);
            if (info[0].equals("Deposit")) {
                String sqlUpdate = "UPDATE Portfolio SET CASH = Cash + " + cost + " WHERE ID = '" +
                        portfolio_id + "';";
                stmt.executeUpdate(sqlUpdate);
                System.out.println("Deposit Success");
            } else if (info[0].equals("Withdraw")) {
                ResultSet rs = null;
                String sqlSelect = "SELECT Cash FROM Portfolio WHERE ID = '" + portfolio_id + "';";
                rs = stmt.executeQuery(sqlSelect);
                if (rs.next() && rs.getFloat("Cash") >= cost) {
                    String sqlUpdate = "UPDATE Portfolio SET CASH = Cash - " + cost + " WHERE ID = '" +
                            portfolio_id + "';";
                    stmt.executeUpdate(sqlUpdate);
                    System.out.println("Withdraw Success");
                } else {
                    System.out.println("Not enough funds");
                }
            } else if (info[0].equals("Transfer")) {
                if (info[2].equals(portfolio_id)) {
                    System.out.println("You cannot transfer to your own account");
                } else {
                    ResultSet rs = null;
                    String sqlSelect = "SELECT Cash FROM Portfolio WHERE ID = '" + portfolio_id + "';";
                    rs = stmt.executeQuery(sqlSelect);
                    if (rs.next() && rs.getFloat("Cash") >= cost) {
                        String sqlUpdate = "UPDATE Portfolio SET CASH = Cash - " + info[1] + " WHERE ID = '" +
                                portfolio_id + "';";
                        stmt.executeUpdate(sqlUpdate);
                        sqlUpdate = "UPDATE Portfolio SET CASH = Cash + " + info[1] + " WHERE ID = '" +
                                info[2] + "';";
                        stmt.executeUpdate(sqlUpdate);
                        System.out.println("Transfer Success");
                    } else {
                        System.out.println("Not enough funds");
                    }
                }
            } else {
                System.out.println("Invalid input");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Invalid input");
        }
    }

    /**
     * A helper for calculating covariance and correlation matrices given the interval user inputted
     * Computation is done on Close attribute
     * Return a Hashtable with Timestamp corresponding to its Close value
     * __________________________
     */
    public static Hashtable<String, Float[][]> matrix(String input, String portfolio_id) {
        Statement stmt = Helper.getStmt();
        ResultSet rs = null;
        Hashtable<String, Float[][]> matrices = new Hashtable<String, Float[][]>();
        String[] info = null;
        try {
            info = input.split(",");
            if (info.length != 2) {
                System.out.println("Invalid Input");
                return null;
            }
        } catch (Exception e) {
            System.out.println("Invalid Input");
            return null;
        }

        try {
            String sqlSelect = "SELECT Stock_symbol FROM Bought WHERE Portfolio_ID = '" + portfolio_id + "';";
            rs = stmt.executeQuery(sqlSelect);
            ArrayList<String> stock = new ArrayList<String>();
            while (rs.next()) {
                stock.add(rs.getString("Stock_symbol"));
            }
            int n = stock.size();
            Float[][] covariance = new Float[n][n];
            Float[][] correlation = new Float[n][n];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (i > j) {
                        covariance[i][j] = covariance[j][i];
                        correlation[i][j] = correlation[j][i];
                    } else {
                        sqlSelect = "WITH LIST_A AS (SELECT (Close - LAG(Close) OVER " +
                                "(ORDER BY Timestamp))/NULLIF(LAG(Close) OVER (ORDER BY Timestamp), 0) " +
                                "AS A, ROW_NUMBER() OVER () AS rn FROM (SELECT Close, Timestamp FROM Stock_hist_info " +
                                "WHERE Symbol = 'A' AND Timestamp IN ((SELECT Timestamp FROM Stock_hist_info WHERE " +
                                "Timestamp BETWEEN '" + info[0] + "' AND '" + info[1] + "' AND Symbol = '" + stock.get(i) + "' ) " +
                                "INTERSECT (SELECT Timestamp FROM Stock_hist_info WHERE Timestamp BETWEEN '" + info[0]
                                + "' AND '" + info[1] + "' AND Symbol = '" + stock.get(j) + "')))), LIST_B AS (SELECT " +
                                "(Close - LAG(Close) OVER (ORDER BY Timestamp))/NULLIF(LAG(Close) OVER " +
                                "(ORDER BY Timestamp), 0) AS B, ROW_NUMBER() OVER () AS rn FROM " +
                                "(SELECT Close, Timestamp FROM Stock_hist_info WHERE Symbol = '" + stock.get(i) + "' AND " +
                                "Timestamp IN ((SELECT Timestamp FROM Stock_hist_info WHERE Timestamp BETWEEN '" +
                                info[0] + "' AND '" + info[1] + "' AND Symbol = '" + stock.get(i) + "' ) INTERSECT " +
                                "(SELECT Timestamp FROM Stock_hist_info WHERE Timestamp BETWEEN '" + info[0] + "' " +
                                "AND '" + info[1] + "' AND Symbol = '" + stock.get(j) + "')))) SELECT COVAR_SAMP(A, B) " +
                                "AS covariance FROM (SELECT A,B FROM LIST_A JOIN LIST_B ON LIST_A.rn = LIST_B.rn);";
                        rs = stmt.executeQuery(sqlSelect);
                        if (rs.next()) {
                            covariance[i][j] = rs.getFloat("covariance");
                        } else {
                            System.out.println("Invalid input");
                            return null;
                        }
                        sqlSelect = "WITH LIST_A AS (SELECT (Close - LAG(Close) OVER " +
                                "(ORDER BY Timestamp))/NULLIF(LAG(Close) OVER (ORDER BY Timestamp), 0) " +
                                "AS A, ROW_NUMBER() OVER () AS rn FROM (SELECT Close, Timestamp FROM Stock_hist_info " +
                                "WHERE Symbol = 'A' AND Timestamp IN ((SELECT Timestamp FROM Stock_hist_info WHERE " +
                                "Timestamp BETWEEN '" + info[0] + "' AND '" + info[1] + "' AND Symbol = '" + stock.get(i) + "' ) " +
                                "INTERSECT (SELECT Timestamp FROM Stock_hist_info WHERE Timestamp BETWEEN '" + info[0]
                                + "' AND '" + info[1] + "' AND Symbol = '" + stock.get(j) + "')))), LIST_B AS (SELECT " +
                                "(Close - LAG(Close) OVER (ORDER BY Timestamp))/NULLIF(LAG(Close) OVER " +
                                "(ORDER BY Timestamp), 0) AS B, ROW_NUMBER() OVER () AS rn FROM " +
                                "(SELECT Close, Timestamp FROM Stock_hist_info WHERE Symbol = '" + stock.get(i) + "' AND " +
                                "Timestamp IN ((SELECT Timestamp FROM Stock_hist_info WHERE Timestamp BETWEEN '" +
                                info[0] + "' AND '" + info[1] + "' AND Symbol = '" + stock.get(i) + "' ) INTERSECT " +
                                "(SELECT Timestamp FROM Stock_hist_info WHERE Timestamp BETWEEN '" + info[0] + "' " +
                                "AND '" + info[1] + "' AND Symbol = '" + stock.get(j) + "')))) SELECT CORR(A, B) " +
                                "AS correlation FROM (SELECT A,B FROM LIST_A JOIN LIST_B ON LIST_A.rn = LIST_B.rn);";
                        rs = stmt.executeQuery(sqlSelect);
                        if (rs.next()) {
                            correlation[i][j] = rs.getFloat("correlation");
                        }
                    }
                }
            }
            matrices.put("Covariance matrix", covariance);
            matrices.put("Correlation matrix", correlation);
            return matrices;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Invalid Input");
        }
        return null;
    }

    /**
     * A helper for showing all the stock and cash value
     * __________________________
     */
    public static void show_stock(String portfolio_id) {
        Statement stmt = Helper.getStmt();
        String sqlSelect = "SELECT Cash FROM Portfolio WHERE ID = '" + portfolio_id + "';";
        String sqlSelect1 = "SELECT Stock_symbol, Share FROM Bought WHERE Portfolio_ID = '" + portfolio_id + "';";
        ResultSet rs = null;
        try {
            rs = stmt.executeQuery(sqlSelect);
            if (rs.next()) {
                System.out.println("Your current balance is: " + rs.getFloat("Cash"));
            }
            rs = stmt.executeQuery(sqlSelect1);
            String str = "";
            int count = 1;
            while (rs.next()) {
                str = str + count + ". " + rs.getString("Stock_symbol") + " with a share of " +
                        rs.getInt("Share") + "\n";
            }
            if (str.isEmpty()) {
                System.out.println("Your portfolio currently does not have any bought stock");
            } else {
                System.out.println("Your portfolio currently has the following stock:\n" + str);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}