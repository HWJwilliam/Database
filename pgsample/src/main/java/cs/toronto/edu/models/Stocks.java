package cs.toronto.edu.models;

public class Stocks {
    // Attributes
    public String Symbol;

    // Initialisation
    public Stocks(String symbol) {
        Symbol = symbol;
    }

    // Getter and setter
    public String getSymbol() {
        return Symbol;
    }

    public void setSymbol(String symbol) {
        Symbol = symbol;
    }
}
