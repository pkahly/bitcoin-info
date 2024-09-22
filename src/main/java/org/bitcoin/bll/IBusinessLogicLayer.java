package org.bitcoin.bll;

import org.bitcoin.bll.model.Price;
import org.bitcoin.exception.BitcoinDatabaseException;

import java.text.ParseException;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused")
public interface IBusinessLogicLayer {
    void create(Price price) throws BitcoinDatabaseException;

    Optional<Price> getPrice(String dateStr);

    List<Price> getPriceRange(String startDateStr, String endDateStr, PriceRangeType rangeType) throws ParseException;

    void delete(String id);

    void update(Price price);
}
