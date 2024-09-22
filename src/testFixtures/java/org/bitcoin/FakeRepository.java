package org.bitcoin;

import org.bitcoin.exception.BitcoinDatabaseException;
import org.bitcoin.storage.IRepository;
import org.bitcoin.storage.entity.PriceEntity;

import java.text.ParseException;
import java.util.List;
import java.util.Optional;

public class FakeRepository implements IRepository {
    @Override
    public void create(PriceEntity price) throws BitcoinDatabaseException {
        throw new IllegalArgumentException("not implemented");
    }

    @Override
    public Optional<PriceEntity> getPrice(String dateStr) {
        throw new IllegalArgumentException("not implemented");
    }

    @Override
    public List<PriceEntity> getPriceRange(String startDateStr, String endDateStr) throws ParseException {
        throw new IllegalArgumentException("not implemented");
    }

    @Override
    public void update(PriceEntity price) {
        throw new IllegalArgumentException("not implemented");
    }

    @Override
    public void delete(String dateStr) {
        throw new IllegalArgumentException("not implemented");
    }
}
