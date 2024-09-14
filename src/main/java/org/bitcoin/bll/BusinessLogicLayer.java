package org.bitcoin.bll;

import org.bitcoin.bll.model.Price;
import org.bitcoin.bll.model.Transformer;
import org.bitcoin.exception.BitcoinDatabaseException;
import org.bitcoin.storage.Repository;
import org.bitcoin.storage.entity.PriceEntity;

import java.util.List;
import java.util.Optional;

public class BusinessLogicLayer {
    private final Repository repository;

    public BusinessLogicLayer(Repository repository) {
        this.repository = repository;
    }

    public void create(Price price) throws BitcoinDatabaseException {
        repository.create(Transformer.toEntity(price));
    }

    public Optional<Price> findById(String id) {
        Optional<PriceEntity> priceEntity = repository.findById(id);
        return priceEntity.map(Transformer::toModel);
    }

    public List<Price> getPriceRange(String startDateStr, String endDateStr) {
        List<PriceEntity> priceEntities = repository.getPriceRange(startDateStr, endDateStr);
        return priceEntities.stream().map(Transformer::toModel).toList();
    }

    public void delete(String id) {
        repository.delete(id);
    }

    public void update(Price price) {
        repository.update(Transformer.toEntity(price));
    }
}
