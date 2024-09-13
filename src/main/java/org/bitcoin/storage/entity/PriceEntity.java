package org.bitcoin.storage.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "price")
public class PriceEntity implements Serializable {
    private static final DateFormat DATE_STRING_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    @Id
    private final String dateStr;

    private Date date;
    private String open;
    private String high;
    private String low;
    private String close;

    public PriceEntity(Date date, String open, String high, String low, String close) {
        this.date = date;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;

        // Set primary key
        this.dateStr = DATE_STRING_FORMAT.format(date);
    }

    @Override
    public String toString() {
        return String.join(", ", List.of(getDateStr(), open, high, low, close));
    }

    public String getDateStr() {
        return dateStr;
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