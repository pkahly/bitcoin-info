package org.bitcoin.bll.model;

import java.time.LocalDate;
import java.util.List;

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

    @Override
    public String toString() {
        return String.join(", ", List.of(getDateStr(), open, high, low, close));
    }

    @Override
    public boolean equals(Object otherObj) {
        if (!(otherObj instanceof Price other)) {
            return false;
        }
        return dateStr.equals(other.dateStr)
                && compareNums(open, other.open)
                && compareNums(high, other.high)
                && compareNums(low, other.low)
                && compareNums(close, other.close);
    }

    private boolean compareNums(String numStr1, String numStr2) {
        return Double.parseDouble(numStr1) == Double.parseDouble(numStr2);
    }
}
