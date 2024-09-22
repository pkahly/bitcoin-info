package org.bitcoin;

import org.bitcoin.bll.IBusinessLogicLayer;
import org.bitcoin.bll.PriceRangeType;
import org.bitcoin.bll.model.Price;
import org.bitcoin.exception.BitcoinDatabaseException;

import java.text.ParseException;
import java.util.List;
import java.util.Optional;

public class FakeBusinessLogicLayer implements IBusinessLogicLayer {
    @Override
    public void create(Price price) throws BitcoinDatabaseException {
        throw new IllegalArgumentException("not implemented");
    }

    @Override
    public Optional<Price> getPrice(String dateStr) {
        throw new IllegalArgumentException("not implemented");
    }

    @Override
    public List<Price> getPriceRange(String startDateStr, String endDateStr) throws ParseException {
        throw new IllegalArgumentException("not implemented");
    }

    @Override
    public List<Price> getPriceRange(String startDateStr, String endDateStr, PriceRangeType rangeType) throws ParseException {
        throw new IllegalArgumentException("not implemented");
    }

    @Override
    public void delete(String id) {
        throw new IllegalArgumentException("not implemented");
    }

    @Override
    public void update(Price price) {
        throw new IllegalArgumentException("not implemented");
    }
}
