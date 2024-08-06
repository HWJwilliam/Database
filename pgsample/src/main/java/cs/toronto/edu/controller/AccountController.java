package cs.toronto.edu.controller;

import cs.toronto.edu.models.Account;
import cs.toronto.edu.utils.Helper;

import java.sql.*;
import java.util.ArrayList;

public class AccountController {

    /**
     * A helper for register a user from the backend
     * __________________________
     */
    public static boolean register(String username, String passwd)  {
        Statement stmt = Helper.getStmt();
        String sqlSelect = "SELECT Username FROM Account WHERE Username = '" + username + "';";
        ResultSet rs = null;
        try {
            rs = stmt.executeQuery(sqlSelect);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (!rs.next()) {
                String sqlInsert = "INSERT INTO Account (Username, Password) " +
                        "VALUES ('" + username + "', '" + passwd + "');";
                stmt.executeUpdate(sqlInsert);
                System.out.println("Register Success");
                return true;
            } else {
                System.out.println("Such username has been taken");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Failed to register");
        return false;
    }

    /**
     * A helper for login a user from the backend
     * __________________________
     */
    public static boolean login(String username, String passwd)  {
        Statement stmt = Helper.getStmt();
        String sqlSelect = "SELECT * FROM Account WHERE Username = '" + username + "' AND Password = '" + passwd + "';";
        ResultSet rs = null;
        try {
            rs = stmt.executeQuery(sqlSelect);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (rs.next()) {
                System.out.println("Login Success");
                return true;
            } else {
                System.out.println("Invalid Username or Password");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * A helper for showing all the friends from the backend
     * __________________________
     */
    public static ArrayList<String> showFriends(String username)  {
        ArrayList<String> lst_friends = new ArrayList<String>();
        Statement stmt = Helper.getStmt();
        ResultSet rs = null;
        String sqlSelect = "(SELECT Receive_username AS Name FROM Friend_request WHERE Decision = TRUE " +
                "AND Request_username = '" + username + "') UNION (SELECT Request_username AS Name FROM " +
                "Friend_request WHERE Decision = TRUE AND Receive_username = '" + username + "');";
        try {
            rs = stmt.executeQuery(sqlSelect);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            int i = 1;
            String str = "";
            while (rs.next()) {
                lst_friends.add(rs.getString("Name"));
                str = str + i + ". " + rs.getString("Name") + "\n";
                i += 1;
            }
            if (lst_friends.size() == 0) {
                System.out.println("You are currently friends with no one");
            } else {
                System.out.println("You are currently friends with: \n" + str);
            }
            return lst_friends;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * A helper for removing the friends from the backend
     * __________________________
     */
    public static void removeFriends(String username)  {
        Statement stmt = Helper.getStmt();
        Timestamp time = new Timestamp(System.currentTimeMillis());
        String curruser = Helper.getAccount().getUsername();
        String sqlUpdate = "UPDATE Friend_request SET Decision = FALSE AND Time = '" + time +
                "' WHERE (Request_username = '" + curruser + "' AND Receive_username = '" + username + "') OR " +
                "(Receive_username = '" + curruser + "' AND Request_username = '" + username + "');";
        try {
            stmt.executeUpdate(sqlUpdate);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * A helper for showing all the incoming request from the backend
     * __________________________
     */
    public static ArrayList<String> showIncomingRequest(String username)  {
        ArrayList<String> lst_user = new ArrayList<String>();
        Statement stmt = Helper.getStmt();
        ResultSet rs = null;
        String sqlSelect = "(SELECT Request_username AS Name FROM Friend_request WHERE Decision IS NULL AND " +
                "Receive_username = '" + username + "');";
        try {
            rs = stmt.executeQuery(sqlSelect);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            String str = "";
            int count = 1;
            while (rs.next()) {
                str = str + count + ". " + rs.getString("Name") + "\n";
                lst_user.add(rs.getString("Name"));
                count += 1;
            }
            if (str.isEmpty()) {
                System.out.println("You currently have no incoming friends request");
            } else {
                System.out.println("You currently have those incoming friends request: \n" + str.substring(0,
                        str.length()));
            }
            return lst_user;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * A helper for processing the incoming request from the backend
     * __________________________
     */
    public static void process_request(String user, String input) {
        Statement stmt = Helper.getStmt();
        Timestamp time = new Timestamp(System.currentTimeMillis());
        String sqlUpdate = null;
        if (input.equals("1")) {
            sqlUpdate = "UPDATE Friend_request SET Decision = TRUE, Time = '" + time +
                    "' WHERE Request_username = '" + user + "' AND Receive_username = '" +
                    Helper.getAccount().getUsername() + "';";
            System.out.println("Request accepted");
        } else if (input.equals("2")) {
            sqlUpdate = "UPDATE Friend_request SET Decision = FALSE, Time = '" + time +
                    "' WHERE Request_username = '" + user + "' AND Receive_username = '" +
                    Helper.getAccount().getUsername() + "';";
            System.out.println("Request rejected");
        }
        try {
            stmt.executeUpdate(sqlUpdate);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * A helper for showing all the users from the backend
     * __________________________
     */
    public static void showUsers()  {
        Statement stmt = Helper.getStmt();
        ResultSet rs = null;
        String sqlSelect = "(SELECT Username FROM Account)";
        String output = "";
        int i = 1;
        try {
            rs = stmt.executeQuery(sqlSelect);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            while (rs.next()) {
                String user = rs.getString("Username");
                output = output + i + ". " + user + "\n";
                i += 1;
            }
            System.out.println(output);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * A helper for sending a request to a user from the backend
     * __________________________
     */
    public static boolean sendRequest(String receive_username, String request_username)  {
        Statement stmt = Helper.getStmt();
        ResultSet rs = null;
        String sqlSelect = "SELECT * FROM Account WHERE Username = '" + receive_username + "';";
        try {
            rs = stmt.executeQuery(sqlSelect);
            if (!rs.next()) {
                System.out.println("There is no such user");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        sqlSelect = "SELECT Decision, Time FROM Friend_request WHERE (Request_username = '" + request_username +
                "' AND Receive_username = '" + receive_username + "') OR (Request_username = '" + receive_username +
                "' AND Receive_username = '" + request_username + "');";
        try {
            rs = stmt.executeQuery(sqlSelect);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Boolean decision = null;
        Timestamp time = null;
        try {
            if (rs.next()) {
                time = rs.getTimestamp("Time");
                decision = rs.getBoolean("Decision");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (time != null) {
            try {
                if (rs.wasNull()) {
                    System.out.println("2");
                    System.out.println("Such friend request has been sent");
                    return false;
                } else if (decision == true) {
                    System.out.println("You are already a friend of this user");
                    return false;
                } else {
                    long l = ((new Timestamp(System.currentTimeMillis())).getTime() - time.getTime()) / 1000 / 60;
                    if (l < 5) {
                        System.out.println("You need to wait for at least 5 minutes after this user rejects the request to send a new one");
                        return false;
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        time = new Timestamp(System.currentTimeMillis());
        String sqlInsert = "INSERT INTO Friend_request (Request_username, Receive_username, Decision, Time) " +
                "VALUES ('" + request_username + "', '" + receive_username + "', NULL, '" + time + "');";
        try {
            stmt.executeUpdate(sqlInsert);
            System.out.println("Friend request is sent successfully");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * A helper for showing all the unprocessed outgoing request from the backend
     * __________________________
     */
    public static void showOutgoingRequest(String username)  {
        Statement stmt = Helper.getStmt();
        ResultSet rs = null;
        String sqlSelect = "(SELECT Receive_username AS Name FROM Friend_request WHERE Decision is NULL AND " +
                "Request_username = '" + username + "');";
        try {
            rs = stmt.executeQuery(sqlSelect);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            String str = "";
            int count = 1;
            while (rs.next()) {
                str = str + count + ". " + rs.getString("Name") + "\n";
                count += 1;
            }
            if (str.isEmpty()) {
                System.out.println("You currently have no pending outgoing friends request");
            } else {
                System.out.println("You currently have those pending outgoing friends request: \n" + str);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
