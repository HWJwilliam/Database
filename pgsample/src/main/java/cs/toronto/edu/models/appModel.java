package cs.toronto.edu.models;

import cs.toronto.edu.controller.*;
import cs.toronto.edu.utils.Helper;

import javax.sound.sampled.Port;
import java.util.ArrayList;
import java.util.Scanner;

public class appModel {

    public String status; // Record the current status of the app

    public Scanner scanner; // For user input

    public String id_stock = null;

    public String id_portfolio = null;

    /**
     * Initialising the instance, set the initial status to Login/Register
     * As login and register is the first thing we have
     * __________________________
     */
    public appModel() {
        Helper.getConnection();
        this.status = "Login/Register";
        this.scanner = new Scanner(System.in);
    }

    /**
     * Run the appropriate helper function based on the current status
     * __________________________
     */
    public void runapp() {
        while (true) {
            if (status.equals("Login/Register")) {
                login_register();
            } else if (status.equals("Homepage")) {
                homepage();
            } else if (status.equals("Manage friends")) {
                manage_friends();
            } else if (status.equals("Incoming Friend Request")) {
                incoming_friend_request();
            } else if (status.equals("Send Friend Request")) {
                send_friend_request();
            } else if (status.equals("Unprocessed Friend Request")) {
                unprocessed_friend_request();
            } else if (status.equals("See/Remove a Friend")) {
                see_or_remove_friend();
            } else if (status.equals("Manage Stock Lists")) {
                manage_stock_lists();
            } else if (status.equals("Create Stock List")) {
                create_stock_list();
            } else if (status.equals("Share/Modify Stock Lists")) {
                share_modify_stock_list();
            } else if (status.equals("View Stock Lists")) {
                view_stock_list();
            } else if (status.equals("Manage Portfolio")) {
                manage_portfolio();
            } else if (status.equals("Create Portfolio")) {
                create_portfolio();
            } else if (status.equals("Look at Portfolio")) {
                look_at_portfolio();
            } else if (status.equals("Buy/Sell Stock")) {
                buy_and_sell();
            } else if (status.equals("Daily stock info")) {
                add_daily_info();
            } else if (status.equals("Cash Account")) {
                cash_account();
            }
        }
    }

    /**
     * Helper function used in login and register, users can type:
     * 1 to Login
     * 2 to Register
     *
     * Username and password is checked when register to ensure at least a length of 3
     * No users can have the same username
     * __________________________
     */
    public void login_register() {
        Boolean done = false;
        String input = null;
        String username = "";
        String password = "";
        while (!done) {
            System.out.println("Please enter 1 for login or 2 for Register");
            input = scanner.nextLine().trim();
            while (!input.equals("1") && !input.equals("2")) {
                System.out.println("Invalid Input");
                System.out.println("Please enter 1 for login or 2 for Register");
                input = scanner.nextLine().trim();
            }

            System.out.println("Type your Username, it must be at least three letters long with no leading or trailing spaces");
            username = scanner.nextLine().trim();
            System.out.println("Type your Password, it must be at least three letters long with no leading or trailing spaces");
            password = scanner.nextLine().trim();
            if ((username.length() >= 3 && password.length() >= 3 && input.equals("2")) || input.equals("1")) {
                done = true;
            } else {
                System.out.println("Your username or password is less than three letters long");
            }
        }

        if (input.equals("1") && AccountController.login(username, password)) {
            Helper.setAccount(new Account(username, password));
            status = "Homepage";
        } else if (input.equals("2") && AccountController.register(username, password)) {
            Helper.setAccount(new Account(username, password));
            status = "Homepage";
        }
    }

    /**
     * Helper function used after user has logged in, they can type
     * 1 to Manage Friends
     * 2 to Manage Stock Lists
     * 3 to Manage Portfolio
     * __________________________
     */
    public void homepage() {
        System.out.println("1. Manage friends; 2. Manage Stock Lists; 3. Manage Portfolio; 0. Log out");

        String input = scanner.nextLine().trim();

        while (!input.equals("1") && !input.equals("2") && !input.equals("3") && !input.equals("0")) {
            System.out.println("Invalid Input");
            System.out.println("1. Manage friends; 2. Manage Stock Lists; 3. Go to Portfolio; 0. Log out");
            input = scanner.nextLine().trim();
        }

        if (input.equals("1")) {
            status = "Manage friends";
        } else if (input.equals("2")) {
            status = "Manage Stock Lists";
        } else if (input.equals("3")) {
            status = "Manage Portfolio";
        } else if (input.equals("0")) {
            status = "Login/Register";
        }
    }

    /**
     * Called when user goes to Manage Friends sections from Homepage, they can type
     * 1 to See all the friends
     * 2 to Send Friend Request
     * 3 to See Outgoing unprocessed Friend Request
     * 4 to See Incoming Friend Request
     * 0 to Go back to Homepage
     * __________________________
     */
    public void manage_friends() {
        System.out.println("1. See/Remove a Friend; 2. Send Friend Request; 3. See Outgoing unprocessed Friend " +
                "Request; 4. See Incoming Friend Request; 0. Go Back");
        String input = scanner.nextLine().trim();

        while (!input.equals("1") && !input.equals("2") && !input.equals("3") && !input.equals("4") && !input.equals("0")) {
            System.out.println("Invalid Input");
            System.out.println("1. See/Remove a friend; 2. Send Friend Request; 3. See Outgoing unprocessed Friend " +
                    "Request; 4. See Incoming Friend Request; 0. Go Back");
            input = scanner.nextLine().trim();
        }

        if (input.equals("1")) {
            status = "See/Remove a Friend";
        } else if (input.equals("2")) {
            status = "Send Friend Request";
        } else if (input.equals("3")) {
            status = "Unprocessed Friend Request";
        } else if (input.equals("4")) {
            status = "Incoming Friend Request";
        } else if (input.equals("0")) {
            status = "Homepage";
        }
    }

    /**
     * Called when user decided to see all the friends or remove a friend
     * A list of all friends would be shown
     * Then the user can type
     * The number corresponding to the friend to remove
     * 0 to Go back to Homepage
     * __________________________
     */
    public void see_or_remove_friend() {
        ArrayList<String> lst_friends = AccountController.showFriends(Helper.getAccount().getUsername());

        boolean valid = false;

        while (!valid) {
            System.out.println("Type the number corresponding to the username to remove a friend or 0 to Go back");
            int i = 0;
            String input = scanner.nextLine().trim();

            try {
                i = Integer.parseInt(input);
                if (i >= 0 && i <= lst_friends.size()) {
                    valid = true;
                } else {
                    System.out.println("Invalid input");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input");
            }

            if (i == 0) {
                status = "Manage friends";
            } else {
                AccountController.removeFriends(lst_friends.get(i - 1));
            }
        }
    }

    /**
     * Called when user decided to see incoming friend request from Manage friends section. List of
     * requests will be listed with numbers, they can type
     * Numbers of the request to process them
     * 0 to Go back to Homepage
     * __________________________
     */
    public void incoming_friend_request() {
        ArrayList<String> lst_user = AccountController.showIncomingRequest(Helper.getAccount().getUsername());
        boolean valid = false;
        int i = 0;

        while (!valid) {
            System.out.println("Type number of the friend request to manage or 0 to Go back");
            try {
                String input = scanner.nextLine().trim();
                i = Integer.parseInt(input);
                if (i >= 0 && i <= lst_user.size()) {
                    valid = true;
                } else {
                    System.out.println("Invalid input");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input");
            }
        }

        if (i == 0) {
            status = "Manage friends";
        } else {
            valid = false;
            while (!valid) {
                System.out.println("You are processing the friend request from: " + lst_user.get(i - 1));
                System.out.println("1. Approve, 2. Reject, 0. Go back");
                String input = scanner.nextLine().trim();
                if (!input.equals("1") && !input.equals("2") && !input.equals("0")) {
                    System.out.println("Invalid input");
                } else if (input.equals("0")) {
                    valid = true;
                } else {
                    AccountController.process_request(lst_user.get(i - 1), input);
                    valid = true;
                }
            }
        }
    }

    /**
     * Called when user decided to send outgoing friend request
     * A list of all users would be shown
     * Then the user can type
     * The username to send a friend request to that user
     * 0 to Go back to Homepage
     * __________________________
     */
    public void send_friend_request() {
        AccountController.showUsers();
        System.out.println("Type the username to send a friend request or 0 to Go back");
        String input = scanner.nextLine().trim();

        if (input.equals("0")) {
            status = "Manage friends";
        } else if (input.equals(Helper.getAccount().getUsername())) {
            System.out.println("You cannot send request to yourself");
        } else {
            AccountController.sendRequest(input, Helper.getAccount().getUsername());
        }
    }

    /**
     * Called when user decided to see unprocessed friend request
     * A list of all unprocessed request would be shown
     * Then the user can type
     * 0 to Go back to Homepage
     * __________________________
     */
    public void unprocessed_friend_request() {
        AccountController.showOutgoingRequest(Helper.getAccount().getUsername());
        boolean valid = false;

        while (!valid) {
            System.out.println("Type 0 to Go back");
            String input = scanner.nextLine().trim();

            if (input.equals("0")) {
                status = "Manage friends";
                valid = true;
            }
        }
    }

    /**
     * Called when user decided to see unprocessed friend request
     * A list of all unprocessed request would be shown
     * Then the user can type
     * 0 to Go back to Homepage
     * __________________________
     */
    public void manage_stock_lists() {
        boolean valid = false;
        String input = null;

        while (!valid) {
            System.out.println("1. Create a new Stock List; 2. Share/Modify my Stocklists; 3. View Stocklists; 0. Go back");
            input = scanner.nextLine().trim();

            if (!input.equals("1") && !input.equals("2") && !input.equals("3") && !input.equals("0")) {
                System.out.println("Invalid Input");
            } else {
                valid = true;
            }
        }

        if (input.equals("1")) {
            status = "Create Stock List";
        } else if (input.equals("2")) {
            status = "Share/Modify Stock Lists";
        } else if (input.equals("3")) {
            status = "View Stock Lists";
        } else if (input.equals("0")) {
            status = "Homepage";
        }
    }

    /**
     * Called when user decided to create a stock_list
     * __________________________
     */
    public void create_stock_list() {
        String list_id = StockListController.create_stock_list();
        id_stock = list_id;
        status = "Share/Modify Stock Lists";
        modify_stock_list(true);
    }

    /**
     * Called when user decided to modify a stock_list and also serves as a helper for create
     * __________________________
     */
    public void modify_stock_list(Boolean bool) {
        String list_id = id_stock;
        while (true) {
            System.out.println("Type ADD and then the stock and share you wanted to add in the stock, separate them " +
                    "by a comma to add a stock, e.g. ADD,AAL,3. Type REMOVE and then the stock name to remove a stock. " +
                    "e.g. REMOVE,AAL. Type SHARE to share the stock list. Type DELETE to delete the stock list. " +
                    "Type EXIT to quit. ");
            String input = scanner.nextLine().trim();
            if (input.equals("EXIT")) {
                id_stock = null;
                if (bool) {
                    status = "Manage Stock Lists";
                } else {
                    status = "Look at portfolio";
                }
                break;
            } else if (input.equals("DELETE")) {
                StockListController.delete_stocklist(list_id);
                break;
            } else if (input.equals("SHARE")) {
                share_stock_list(list_id);
            } else if (input.contains(",")) {
                String[] request = input.split(",");
                if (request[0].equals("ADD")) {
                    try {
                        if (StockController.check_contain_stock(request[1])) {
                            int stock = Integer.parseInt(request[2]);
                            StockListController.list_add_stock(list_id, request[1], stock);
                        } else {
                            System.out.println("Invalid stock");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input");
                    }
                } else if (request[0].equals("REMOVE")) {
                    try {
                        if (StockController.check_contain_stock(request[1])) {
                            StockListController.list_remove_stock(list_id, request[1]);
                        } else {
                            System.out.println("Invalid input");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input");
                    }
                } else {
                    System.out.println("Invalid input");
                }
            } else {
                System.out.println("Invalid input");
            }
        }
    }

    /**
     * Called when user decided to share and modify stock list
     * __________________________
     */
    public void share_modify_stock_list() {
        StockListController.show_stock_list();
        System.out.println("Type the ID of the stock for go into the stock");
        String input = scanner.nextLine().trim();
        if (StockListController.check_contain_stocklist(input)) {
            id_stock = input;
            modify_stock_list(true);
        } else {
            System.out.println("No such stock list is found");
            status = "Manage Stock Lists";
        }
    }

    /**
     * Called when user decided to share the stock list
     * __________________________
     */
    public void share_stock_list(String list_id){
        ArrayList<String> user_lst = AccountController.showFriends(Helper.getAccount().getUsername());
        boolean valid = false;
        while (!valid) {
            System.out.println("Type the number corresponding to the friend to share to the friend or " +
                    "type PUBLIC to share to all people or type EXIT to quit");
            String input = scanner.nextLine().trim();
            if (input.equals("PUBLIC")) {
                StockListController.share_stocklist_public(list_id, Helper.getAccount().getUsername());
                valid = true;
            } else if (input.equals("EXIT")) {
                valid = true;
            } else {
                try {
                    int friend = Integer.parseInt(input);
                    if (1 <= friend && friend <= user_lst.size()) {
                        StockListController.share_stocklist(list_id, Helper.getAccount().getUsername(),
                                user_lst.get(friend - 1));
                    } else {
                        System.out.println("Invalid Input");
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Called when user decided to view all stock list
     * __________________________
     */
    public void view_stock_list(){
        boolean valid = false;
        while (!valid) {
            StockListController.show_all_stock_list();
            System.out.println("Type the ID corresponding to the stock list to view or type 0 to go out");
            String input = scanner.nextLine().trim();
            if (input.equals("0")) {
                valid = true;
                status = "Manage Stock Lists";
            } else {
                if (StockListController.user_contains_stocklist(input)) {
                    id_stock = input;
                    StockListController.display_stock_list(input);
                    review_page();
                    id_stock = null;
                } else {
                    System.out.println("Invalid stock ID");
                }
            }
        }
    }

    /**
     * Helper function for reviewing
     * __________________________
     */
    public void review_page(){
        String list_id = id_stock;
        while (true) {
            System.out.println("1. Create/Edit a review; 2. Show/Remove Reviews; 0. Go back");
            String input = scanner.nextLine().trim();
            if (!input.equals("0") && !input.equals("1") && !input.equals("2")) {
                System.out.println("Invalid Input");
            } else if (input.equals("0")) {
                break;
            } else if (input.equals("1")) {
                System.out.println("Give your review:");
                input = scanner.nextLine().trim();
                while (input.length() > 4000) {
                    System.out.println("The length of the review must be no more than 4000 characters");
                    input = scanner.nextLine().trim();
                }
                ReviewController.add_review(list_id, input, Helper.getAccount().getUsername());
            } else if (input.equals("2")) {
                ArrayList<String> review_lst = ReviewController.show_review(list_id);
                System.out.println("Type the username corresponding the comment to delete, or 0 to go back");
                while (true) {
                    input = scanner.nextLine().trim();
                    if (input.equals("0")) {
                        break;
                    } else {
                        try {
                            boolean valid = false;
                            for (String review : review_lst) {
                                int num = review.indexOf(":");
                                if (input.equals(review.substring(0, num))) {
                                    ReviewController.remove_review(list_id, review.substring(0, num));
                                    valid = true;
                                }
                            }
                            if (!valid) {
                                System.out.println("Invalid username");
                            }
                            break;
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    /**
     * Called when user decided to manage portfolio
     * User can type
     * 1 to Create a new Portfolio
     * 2 to Look at a Portfolio
     * 0 to Go back to Homepage
     * __________________________
     */
    public void manage_portfolio() {
        System.out.println("1. Create a new Portfolio; 2. Look at a Portfolio; 0. Go back");
        boolean valid = false;
        String input = null;

        while (!valid) {
            input = scanner.nextLine().trim();
            if (!input.equals("1") && !input.equals("2") && !input.equals("0")) {
                System.out.println("Invalid Input");
            } else {
                valid = true;
            }
        }

        if (input.equals("1")) {
            status = "Create Portfolio";
        } else if (input.equals("2")) {
            status = "Look at Portfolio";
        } else if (input.equals("0")) {
            status = "Homepage";
        }
    }

    /**
     * Called when user decided to create a portfolio
     * __________________________
     */
    public void create_portfolio(){
        // own stock list
        id_portfolio = PortfolioController.create_portfolio();
        id_stock = id_portfolio;
        status = "Look at Portfolio";
        go_to_portfolio();
    }

    /**
     * Called when user decided to look at the portfolios
     * __________________________
     */
    public void look_at_portfolio(){
        PortfolioController.show_portfolio();
        System.out.println("Type the ID of the portfolio for go into the stock");
        String input = scanner.nextLine().trim();
        if (PortfolioController.check_contain_portfolio(input)) {
            id_portfolio = input;
            id_stock = input;
            status = "Look at Portfolio";
            go_to_portfolio();
        } else {
            System.out.println("No such portfolio found");
        }
    }

    /**
     * Called when user decided to go to a portfolio
     * __________________________
     */
    public void go_to_portfolio() {
        String portfolio_id = id_portfolio;
        String input = null;

        while (true) {
            while (true) {
                System.out.println("1. To Buy/Sell Stock; 2. Share/Modify stock list; 3. View stock list; " +
                        "4. Covariance/Correlation matrix; 5. Beta Value; 6. COV; 7. Cash Account " +
                        "8. Graph; 9. Daily stock info; 0. Go back");

                input = scanner.nextLine().trim();
                if (!input.equals("1") && !input.equals("2") && !input.equals("3") && !input.equals("4")
                        && !input.equals("5") && !input.equals("6") && !input.equals("7") && !input.equals("8")
                        && !input.equals("0")) {
                    System.out.println("Invalid Input");
                } else {
                    break;
                }
            }

            if (input.equals("1")) {
                status = "Buy/Sell Stock";
                break;
            } else if (input.equals("2")) {
                status = "Share/Modify Stock Lists";
                modify_stock_list(false);
                status = "Look at Portfolio";
            } else if (input.equals("3")) {
                status = "View Stock Lists";
                StockListController.display_stock_list(portfolio_id);
                review_page();
                status = "Look at Portfolio";
            } else if (input.equals("4")) {
                status = "Matrix";
                break;
            } else if (input.equals("5")) {
                status = "Beta";
                break;
            } else if (input.equals("6")) {
                status = "COV";
                break;
            } else if (input.equals("7")) {
                status = "Cash Account";
                break;
            } else if (input.equals("8")) {
                status = "Graph";
                break;
            } else if (input.equals("9")) {
                status = "Daily stock info";
                break;
            } else if (input.equals("0")) {
                status = "Manage Portfolio";
                break;
            }
        }
    }

    public void buy_and_sell(){
        while (true) {
            System.out.println("Type Buy/Sell, date in YYYY-MM-DD, the stock ID, share connect them in comma to " +
                    "buy/sell a stock with a certain share. E.g. Buy,2018-02-01,3,AAL");
            String input = scanner.nextLine().trim();
            PortfolioController.execute_transaction(input, id_portfolio);
        }
    }

    public void add_daily_info(){
        while (true) {
            System.out.println("Type Buy/Sell and YYYY-MM-DD and Stock and Share, separate them by a comma to indicate" +
                    "buy/sell a stock with some share. E.g. Buy,2015-04-19,AAL,4");

        }
        // Input: AAL 20, A 50, AB 90
        // Input: Transaction Time
        /*
        1. compute cost and compare with cash; -> search history information to get close price
        2. Insert into Buy(Portfolio_ID,Stock_symbol,Share, Transaction_time) Values (...)
        3. Update Portfolio SET Cash = value WHERE ID = portfolio_id;
         */
    }

    public void cash_account(){
        while (true) {
            PortfolioController.list_portfolio(id_portfolio);
            System.out.println("Type Deposit/Withdraw/Transfer and the amount and the Portfolio_ID " +
                    "(if transfer is selected), connect them in comma. E.g. Deposit,3, or " +
                    "Transfer,3,Hello2024-08-05 15:37:48.573. Or type 0 to go back");
            String input = scanner.nextLine().trim();
            if (input.equals("0")) {
                status = "Look at Portfolio";
                break;
            } else {
                PortfolioController.deposit_withdraw_transfer(input, id_portfolio);
            }
        }
    }

    public void statistic_stock_in_portfolio(){

        // Input: AAL 20, A 50, AB 90
        // Input: Transaction Time
        /*
        1. find bought stocks in portfolio: Select * from Buy where Portfolio_ID = portfolio_id
        2. find history information for stocks til Transaction_time
        3. calculate cov for each stock -> cov matrix
         */
    }



}