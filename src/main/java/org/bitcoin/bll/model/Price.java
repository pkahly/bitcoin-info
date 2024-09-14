package org.bitcoin.bll.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@SuppressWarnings("unused")
public class Price {
    private static final DateFormat DATE_STRING_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    private String dateStr;
    private Date date;
    private String open;
    private String high;
    private String low;
    private String close;

    public Price() {}

    public Price(Date date, String open, String high, String low, String close) {
        this(DATE_STRING_FORMAT.format(date), date, open, high, low, close);
    }

    public Price(String dateStr, Date date, String open, String high, String low, String close) {
        this.dateStr = dateStr;
        this.date = date;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
    }

    public String getDateStr() {
        return dateStr;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
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
