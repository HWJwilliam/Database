package cs.toronto.edu.models;

public class Review {

    // Attributes
    public String Account_username;
    public String Stock_list_ID;
    public String Review_text;

    // Initialisation
    public Review(String account_username, String stock_list_ID, String review_text) {
        Account_username = account_username;
        Stock_list_ID = stock_list_ID;
        Review_text = review_text;
    }

    // Getter and setter
    public String getAccount_username() {
        return Account_username;
    }

    public void setAccount_username(String account_username) {
        Account_username = account_username;
    }

    public String getStock_list_ID() {
        return Stock_list_ID;
    }

    public void setStock_list_ID(String stock_list_ID) {
        Stock_list_ID = stock_list_ID;
    }

    public String getReview_text() {
        return Review_text;
    }

    public void setReview_text(String review_text) {
        Review_text = review_text;
    }
}
