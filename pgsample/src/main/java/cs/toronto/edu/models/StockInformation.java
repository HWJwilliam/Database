package cs.toronto.edu.models;

public class StockInformation {

    // Attributes
    public String symbol;
    public int share;

    // Initialisation
    public StockInformation(String symbol, int share) {
        this.symbol = symbol;
        this.share = share;
    }

    // Getter and setter
    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public int getShare() {
        return share;
    }

    public void setShare(int share) {
        this.share = share;
    }
}
