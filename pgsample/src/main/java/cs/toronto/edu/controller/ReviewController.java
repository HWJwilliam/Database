package cs.toronto.edu.controller;

import cs.toronto.edu.utils.Helper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class ReviewController {

    /**
     * A helper for adding or editing a review on the backend
     * __________________________
     */
    public static void add_review(String list_id, String review, String username) {
        Statement stmt = Helper.getStmt();
        String sqlSelect = "SELECT * FROM Review WHERE Stock_list_ID = '" +
                list_id + "' AND Account_username = '" + username + "';";
        ResultSet rs = null;
        try {
            rs = stmt.executeQuery(sqlSelect);
            if (rs.next()) {
                String sqlUpdate = "UPDATE Review SET Review_text = '" + review + "' WHERE Account_username = '" +
                        username + "' AND Stock_list_ID = '" + list_id + "';";
                stmt.executeUpdate(sqlUpdate);
                System.out.println("Review successfully added");
            } else {
                String sqlUpdate = "INSERT INTO Review (Account_username, Stock_list_ID, Review_text) VALUES ('" +
                        username + "', '" + list_id + "', '" + review + "');";
                stmt.executeUpdate(sqlUpdate);
                System.out.println("Review successfully added");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * A helper for showing all the review in the backend
     * __________________________
     */
    public static ArrayList<String> show_review(String list_id) {
        Statement stmt = Helper.getStmt();
        ArrayList<String> review_lst = new ArrayList<String>();
        String sqlSelect = "SELECT Account_username, Review_text FROM Review WHERE Stock_list_ID = '" + list_id + "';";
        ResultSet rs = null;
        try {
            rs = stmt.executeQuery(sqlSelect);
            String str = "";
            while (rs.next()) {
                String review = rs.getString("Account_username") + ": " + rs.getString("Review_text");
                str = str + review + "\n";
                review_lst.add(review);
            }
            if (str.isEmpty()) {
                System.out.println("This stock list currently have no reviews");
            } else {
                System.out.println("This stock list has the following reviews:\nUsername | Review\n-------- | ------\n" + str);
            }
            return review_lst;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * A helper for showing all the review in the backend
     * __________________________
     */
    public static void remove_review(String list_id, String username) {
        Statement stmt = Helper.getStmt();
        String sqlSelect = "SELECT Account_username FROM Creates WHERE Stock_list_ID = '" + list_id + "';";
        ResultSet rs = null;
        try {
            rs = stmt.executeQuery(sqlSelect);
            if (rs.next() && Helper.getAccount().getUsername().equals(username)) {
                String sqlUpdate = "DELETE FROM Review WHERE Stock_list_ID = '" + list_id + "' AND Account_username " +
                        "= '" + username + "';";
                stmt.executeUpdate(sqlUpdate);
                System.out.println("Review deleted successfully");
            } else {
                if (Helper.getAccount().getUsername().equals(rs.getString("Account_username"))) {
                    String sqlUpdate = "DELETE FROM Review WHERE Stock_list_ID = '" + list_id + "' AND Account_username " +
                            "= '" + username + "';";
                    stmt.executeUpdate(sqlUpdate);
                    System.out.println("Review deleted successfully");
                } else {
                    System.out.println("You don't have the permission to remove such review");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
