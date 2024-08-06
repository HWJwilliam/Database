package cs.toronto.edu.models;

public class Portfolio {

    // Attributes
    public String id;
    public int cash;

    // Init
    public Portfolio(String ID, int cash) {
        this.id = ID;
        this.cash = cash;
    }

    // Get and Set functions
    public String getID() {
        return id;
    }

    public void setID(String ID) {
        this.id = ID;
    }

    public int getCash() {
        return cash;
    }

    public void setCash(int cash) {
        this.cash = cash;
    }
}
