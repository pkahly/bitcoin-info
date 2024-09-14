package org.bitcoin.storage.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "price")
@SuppressWarnings("unused")
public class PriceEntity implements Serializable {
    @Id
    private final String dateStr;

    @Column(nullable = false, unique = true)
    private LocalDate date;

    @Column(nullable = false)
    private String open;

    @Column(nullable = false)
    private String high;

    @Column(nullable = false)
    private String low;

    @Column(nullable = false)
    private String close;

    public PriceEntity(String dateStr, LocalDate date, String open, String high, String low, String close) {
        this.dateStr = dateStr;
        this.date = date;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
    }

    @Override
    public String toString() {
        return String.join(", ", List.of(getDateStr(), open, high, low, close));
    }

    public String getDateStr() {
        return dateStr;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
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