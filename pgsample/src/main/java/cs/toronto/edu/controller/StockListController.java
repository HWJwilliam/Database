package cs.toronto.edu.controller;

import cs.toronto.edu.utils.Helper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

public class StockListController {

    /**
     * A helper for creating the stock list in the backend
     * __________________________
     */
    public static String create_stock_list() {
        Statement stmt = Helper.getStmt();
        String list_id = Helper.getAccount().getUsername() + new Timestamp(System.currentTimeMillis());;
        String username = Helper.getAccount().getUsername();
        String sqlInsert = "INSERT INTO Stock_list (ID) " + "VALUES ('" + list_id + "');";
        String sqlInsert2 = "INSERT INTO Creates (Account_username, Stock_list_ID) " + "VALUES ('" + username + "', '" +
        list_id + "');";
        try {
            stmt.executeUpdate(sqlInsert);
            stmt.executeUpdate(sqlInsert2);
            return list_id;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * A helper for checking if a stock list is in the stock list from the backend
     * __________________________
     */
    public static boolean check_contain_stocklist(String list_id) {
        Statement stmt = Helper.getStmt();
        String sqlSelect = "SELECT * FROM Stock_list WHERE ID = '" + list_id + "';";
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
     * A helper for adding a stock and share to a stock list from the backend
     * __________________________
     */
    public static void list_add_stock(String list_id, String stock, int share) {
        Statement stmt = Helper.getStmt();
        String sqlSelect = "SELECT * FROM Have_stock WHERE Symbol = '" + stock + "' AND Stock_list_ID = '" + list_id +
                "';";
        ResultSet rs = null;
        try {
            rs = stmt.executeQuery(sqlSelect);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (rs.next()) {
                String sqlUpdate = "UPDATE Have_stock SET Share = Share + " + share + " WHERE Symbol = '" + stock + "';";
                try {
                    stmt.executeUpdate(sqlUpdate);
                    System.out.println("Stock added successfully");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                String sqlInsert = "INSERT INTO Have_stock (Symbol, Share, Stock_list_ID) VALUES ('" + stock + "', "
                        + share + ", '" + list_id + "');";
                try {
                    stmt.executeUpdate(sqlInsert);
                    System.out.println("Stock added successfully");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * A helper for removing a stock to a stock list from the backend
     * __________________________
     */
    public static void list_remove_stock(String list_id, String stock) {
        Statement stmt = Helper.getStmt();
        String sqlSelect = "SELECT * FROM Have_stock WHERE Symbol = '" + stock + "' AND Stock_list_ID = '" + list_id +
                "';";
        ResultSet rs = null;
        try {
            rs = stmt.executeQuery(sqlSelect);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (rs.next()) {
                String sqlUpdate = "DELETE FROM Have_stock WHERE Symbol = '" + stock + "' AND Stock_list_ID = '"
                        + list_id + "';";
                try {
                    stmt.executeUpdate(sqlUpdate);
                    System.out.println("Stock removed successfully");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("There is no such valid stock");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * A helper for showing all the stock list created by a user from the backend
     * __________________________
     */
    public static void show_stock_list(){
        Statement stmt = Helper.getStmt();
        String sqlSelect = "SELECT Stock_list_ID FROM Creates WHERE Account_username = '" +
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
                str = str + count + ". " + rs.getString("Stock_list_ID") + "\n";
                count += 1;
            }
            if (str.isEmpty()) {
                System.out.println("You currently have no created stocks");
            } else {
                System.out.println("You currently have those stocks: \n" + str);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * A helper for deleting the stock list created by a user from the backend
     * __________________________
     */
    public static void delete_stocklist(String list_id) {
        Statement stmt = Helper.getStmt();
        String sqlUpdate = "DELETE FROM Review WHERE Stock_list_ID = '" + list_id + "';";
        try {
            stmt.executeUpdate(sqlUpdate);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        sqlUpdate = "DELETE FROM Have_stock WHERE Stock_list_ID = '" + list_id + "';";
        try {
            stmt.executeUpdate(sqlUpdate);
            System.out.println("Stock list deleted successfully");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        sqlUpdate = "DELETE FROM Creates WHERE Stock_list_ID = '" + list_id + "';";
        try {
            stmt.executeUpdate(sqlUpdate);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        sqlUpdate = "DELETE FROM Stock_list WHERE ID = '" + list_id + "';";
        try {
            stmt.executeUpdate(sqlUpdate);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * A helper for sharing the stock list created by a user to public from the backend
     * __________________________
     */
    public static void share_stocklist_public(String list_id, String username) {
        Statement stmt = Helper.getStmt();
        String sqlSelect = "SELECT Username FROM Account WHERE Username != '" + username + "';";
        ResultSet rs = null;
        try {
            rs = stmt.executeQuery(sqlSelect);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            while (rs.next()) {
                String user = rs.getString("Username");
                String sqlUpdate = "INSERT INTO Share (Sharing_account_username, To_share_account_username, Stock_list_ID)" +
                        " VALUES ('" + username + "', '" + user + "', '" + list_id + "');";
                stmt.executeUpdate(sqlUpdate);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * A helper for sharing the stock list created by a user from the backend
     * __________________________
     */
    public static void share_stocklist(String list_id, String username, String user) {
        try {
            Statement stmt = Helper.getStmt();
            String sqlUpdate = "INSERT INTO Share (Sharing_account_username, To_share_account_username, Stock_list_ID)" +
                    " VALUES ('" + username + "', '" + user + "', '" + list_id + "');";
            stmt.executeUpdate(sqlUpdate);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * A helper for showing all the stock list shared to a user from the backend
     * __________________________
     */
    public static void show_all_stock_list(){
        Statement stmt = Helper.getStmt();
        String sqlSelect = "(SELECT Stock_list_ID FROM Share WHERE To_share_account_username = '" +
                Helper.getAccount().getUsername() + "') UNION (SELECT Stock_list_ID FROM Creates WHERE " +
                "Account_username = '" + Helper.getAccount().getUsername() + "');";
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
                str = str + count + ". " + rs.getString("Stock_list_ID") + "\n";
                count += 1;
            }
            if (str.isEmpty()) {
                System.out.println("You currently have no stocks shared to you");
            } else {
                System.out.println("You currently have those stocks: \n" + str);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * A helper for showing all the stock list shared to a user from the backend
     * __________________________
     */
    public static boolean user_contains_stocklist(String list_id) {
        Statement stmt = Helper.getStmt();
        String sqlSelect = "(SELECT Stock_list_ID FROM Share WHERE To_share_account_username = '" + Helper.getAccount().getUsername()
                + "' AND Stock_list_ID = '" + list_id + "') UNION (SELECT Stock_list_ID FROM Creates WHERE " +
                "Account_username = '" + Helper.getAccount().getUsername() + "' AND Stock_list_ID = '" + list_id
                + "');";
        ResultSet rs = null;
        try {
            rs = stmt.executeQuery(sqlSelect);
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
     * A helper for showing all the stock list shared to a user from the backend
     * __________________________
     */
    public static void display_stock_list(String list_id){
        Statement stmt = Helper.getStmt();
        String sqlSelect = "SELECT Symbol, Share FROM Have_stock WHERE Stock_list_ID = '" +
               list_id + "';";
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
                str = str + count + ". Stock: " + rs.getString("Symbol") +
                        ", with share of: " + rs.getInt("Share") + "\n";
                count += 1;
            }
            if (str.isEmpty()) {
                System.out.println("There is no interested stocks in this stock list");
            } else {
                System.out.println("The stock list has those interested stocks: \n" + str);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
