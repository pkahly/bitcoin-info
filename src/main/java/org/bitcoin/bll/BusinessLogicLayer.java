package org.bitcoin.bll;

import org.bitcoin.bll.model.Price;
import org.bitcoin.bll.model.Transformer;
import org.bitcoin.exception.BitcoinDatabaseException;
import org.bitcoin.storage.IRepository;
import org.bitcoin.storage.entity.PriceEntity;
import org.eclipse.jetty.util.StringUtil;

import java.text.ParseException;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused")
public class BusinessLogicLayer implements IBusinessLogicLayer {
    private final IRepository repository;

    public BusinessLogicLayer(IRepository repository) {
        this.repository = repository;
    }

    @Override
    public void create(Price price) throws BitcoinDatabaseException {
        repository.create(Transformer.toEntity(price));
    }

    @Override
    public Optional<Price> getPrice(String dateStr) {
        Optional<PriceEntity> priceEntity = repository.getPrice(dateStr);
        return priceEntity.map(Transformer::toModel);
    }

    @Override
    public List<Price> getPriceRange(String startDateStr, String endDateStr, PriceRangeType rangeType) throws ParseException {
        if (rangeType == null) {
            throw new IllegalArgumentException("PriceRangeType cannot be null");
        }
        if (StringUtil.isBlank(startDateStr) || StringUtil.isBlank(endDateStr)) {
            throw new IllegalArgumentException("Both dates are required");
        }

        List<PriceEntity> priceEntities = repository.getPriceRange(startDateStr, endDateStr);

        // Merge prices according to the range type
        priceEntities = PriceMergeUtils.mergePrices(priceEntities, rangeType);

        return priceEntities.stream().map(Transformer::toModel).toList();
    }

    @Override
    public void delete(String id) {
        repository.delete(id);
    }

    @Override
    public void update(Price price) {
        repository.update(Transformer.toEntity(price));
    }
}
