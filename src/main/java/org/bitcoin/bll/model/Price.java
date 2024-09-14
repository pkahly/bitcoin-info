package org.bitcoin.bll.model;

import java.time.LocalDate;

@SuppressWarnings("unused")
public class Price {
    private String dateStr;
    private String open;
    private String high;
    private String low;
    private String close;

    public Price() {}

    public Price(LocalDate date, String open, String high, String low, String close) {
        this(Transformer.dateToStr(date), open, high, low, close);
    }

    public Price(String dateStr, String open, String high, String low, String close) {
        this.dateStr = dateStr;
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
