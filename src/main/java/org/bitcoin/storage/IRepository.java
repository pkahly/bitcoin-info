package org.bitcoin.storage;

import org.bitcoin.exception.BitcoinDatabaseException;
import org.bitcoin.storage.entity.PriceEntity;

import java.text.ParseException;
import java.util.List;
import java.util.Optional;

public interface IRepository {
    void create(PriceEntity price) throws BitcoinDatabaseException;

    Optional<PriceEntity> getPrice(String dateStr);

    List<PriceEntity> getPriceRange(String startDateStr, String endDateStr) throws ParseException;

    void update(PriceEntity price);

    void delete(String dateStr);
}
