package org.bitcoin.storage;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PriceRecord {
    private static final DateFormat DATE_STRING_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    private Date date;
    private String open;
    private String high;
    private String low;
    private String close;

    public PriceRecord(Date date, String open, String high, String low, String close) {
        this.date = date;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
    }

    @Override
    public String toString() {
        return String.join(", ", List.of(getDateString(), open, high, low, close));
    }

    public String getDateString() {
        return DATE_STRING_FORMAT.format(date);
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getOpen() {
        return open;
    }

    public void setOpen(String open) {
        this.open = open;
    }

    public String getHigh() {
        return high;
    }

    public void setHigh(String high) {
        this.high = high;
    }

    public String getLow() {
        return low;
    }

    public void setLow(String low) {
        this.low = low;
    }

    public String getClose() {
        return close;
    }

    public void setClose(String close) {
        this.close = close;
    }
}
