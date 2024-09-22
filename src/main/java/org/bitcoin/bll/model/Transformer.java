package org.bitcoin.bll.model;

import org.bitcoin.storage.entity.PriceEntity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.List;

@SuppressWarnings("unused")
public class Transformer {
    private static final DateTimeFormatter formatter = new DateTimeFormatterBuilder().appendPattern("yyyy-MM-dd").toFormatter();

    public static Price toModel(PriceEntity priceEntity) {
        return new Price(priceEntity.getDateStr(), priceEntity.getOpen(), priceEntity.getHigh(), priceEntity.getLow(), priceEntity.getClose());
    }

    public static List<Price> toModel(List<PriceEntity> priceEntities) {
        return priceEntities.stream().map(Transformer::toModel).toList();
    }

    public static PriceEntity toEntity(Price price) {
        LocalDate date = strToDate(price.getDateStr());
        return new PriceEntity(price.getDateStr(), date, price.getOpen(), price.getHigh(), price.getLow(), price.getClose());
    }

    public static List<PriceEntity> toEntity(List<Price> prices) {
        return prices.stream().map(Transformer::toEntity).toList();
    }

    public static LocalDate strToDate(String dateStr) {
        return LocalDate.parse(dateStr);
    }

    public static String dateToStr(LocalDate date) {
        return date.format(formatter);
    }
}
