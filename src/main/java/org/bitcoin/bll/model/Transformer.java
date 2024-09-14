package org.bitcoin.bll.model;

import org.bitcoin.storage.entity.PriceEntity;

public class Transformer {
    public static Price toModel(PriceEntity priceEntity) {
        return new Price(priceEntity.getDateStr(), priceEntity.getDate(), priceEntity.getOpen(), priceEntity.getHigh(), priceEntity.getLow(), priceEntity.getClose());
    }

    public static PriceEntity toEntity(Price price) {
        return new PriceEntity(price.getDateStr(), price.getDate(), price.getOpen(), price.getHigh(), price.getLow(), price.getClose());
    }
}
