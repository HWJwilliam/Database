package cs.toronto.edu.models;

import java.util.Date;

public class StockHistory {

    // Attributes
    public Date timestamp;
    public String symbol;
    public float open;
    public float close;
    public float high;
    public float low;
    public int volume;

    // Init
    public StockHistory(String symbol, Date timestamp, float open, float close, float high, float low, int volume) {
        this.symbol = symbol;
        this.timestamp = timestamp;
        this.open = open;
        this.close = close;
        this.high = high;
        this.low = low;
        this.volume = volume;
    }

    // Get and set functions
    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date Timestamp) {
        this.timestamp = timestamp;
    }

    public float getOpen() {
        return open;
    }

    public void setOpen(float open) {
        this.open = open;
    }

    public float getClose() {
        return close;
    }

    public void setClose(float close) {
        this.close = close;
    }

    public float getHigh() {
        return high;
    }

    public void setHigh(float high) {
        this.high = high;
    }

    public float getLow() {
        return low;
    }

    public void setLow(float low) {
        this.low = low;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

}
