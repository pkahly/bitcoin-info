package org.bitcoin.bll;

import org.bitcoin.FakeRepository;
import org.bitcoin.bll.model.Price;
import org.bitcoin.bll.model.Transformer;
import org.bitcoin.exception.BitcoinDatabaseException;
import org.bitcoin.storage.IRepository;
import org.bitcoin.storage.entity.PriceEntity;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class BusinessLogicLayerTest {
    @Test
    public void testCreate() throws BitcoinDatabaseException {
        Price expectedPrice = new Price("2011-01-01", "2", "4", "1", "3");
        IRepository repository = new FakeRepository() {
            @Override
            public void create(PriceEntity priceEntity) {
                Price price = Transformer.toModel(priceEntity);
                assertEquals(expectedPrice, price);
            }
        };

        BusinessLogicLayer bll = new BusinessLogicLayer(repository);
        bll.create(expectedPrice);
    }

    @Test
    public void testGetPriceEmpty() throws BitcoinDatabaseException {
        String expectedDateStr = "2011-01-01";
        IRepository repository = new FakeRepository() {
            @Override
            public Optional<PriceEntity> getPrice(String dateStr) {
                assertEquals(expectedDateStr, dateStr);
                return Optional.empty();
            }
        };

        BusinessLogicLayer bll = new BusinessLogicLayer(repository);
        Optional<Price> resultPrice = bll.getPrice(expectedDateStr);

        assertTrue(resultPrice.isEmpty());
    }

    @Test
    public void testGetPrice() throws BitcoinDatabaseException {
        Price expectedPrice = new Price("2011-01-01", "2", "4", "1", "3");
        IRepository repository = new FakeRepository() {
            @Override
            public Optional<PriceEntity> getPrice(String dateStr) {
                assertEquals(expectedPrice.getDateStr(), dateStr);
                return Optional.of(Transformer.toEntity(expectedPrice));
            }
        };

        BusinessLogicLayer bll = new BusinessLogicLayer(repository);
        Optional<Price> resultPrice = bll.getPrice(expectedPrice.getDateStr());

        assertTrue(resultPrice.isPresent());
        assertEquals(expectedPrice, resultPrice.get());
    }

    @Test
    public void testGetPriceRangeDay() throws BitcoinDatabaseException, ParseException {
        Price expectedPrice = new Price("2011-01-01", "2", "4", "1", "3");
        IRepository repository = new FakeRepository() {
            @Override
            public List<PriceEntity> getPriceRange(String startDateStr, String endDateStr) {
                assertEquals(expectedPrice.getDateStr(), startDateStr);
                assertEquals(expectedPrice.getDateStr(), endDateStr);
                return List.of(Transformer.toEntity(expectedPrice));
            }
        };

        BusinessLogicLayer bll = new BusinessLogicLayer(repository);
        List<Price> resultPrices = bll.getPriceRange(expectedPrice.getDateStr(), expectedPrice.getDateStr(), PriceRangeType.DAY);

        assertEquals(1, resultPrices.size());
        assertEquals(List.of(expectedPrice), resultPrices);
    }

    @Test
    public void testGetPriceRangeMonth() throws BitcoinDatabaseException, ParseException {
        String expectedStartDate = "2011-01-01";
        String expectedEndDate = "2013-01-01";
        List<Price> inputPrices = List.of(
                new Price("2011-01-01", "2", "4", "1", "3"),
                new Price("2011-01-02", "4", "8", "2", "6"),
                new Price("2011-02-01", "4", "8", "2", "6"),
                new Price("2011-02-02", "2", "4", "1", "3"),
                new Price("2012-01-01", "2", "4", "1", "3"),
                new Price("2012-01-05", "1", "2", "1", "1")
        );

        // Prices should be combined by month
        List<Price> expectedPrices = List.of(
                new Price("2011-01-01", "2", "8", "1", "6"),
                new Price("2011-02-01", "4", "8", "1", "3"),
                new Price("2012-01-01", "2", "4", "1", "1")
        );

        IRepository repository = new FakeRepository() {
            @Override
            public List<PriceEntity> getPriceRange(String startDateStr, String endDateStr) {
                assertEquals(expectedStartDate, startDateStr);
                assertEquals(expectedEndDate, endDateStr);
                return Transformer.toEntity(inputPrices);
            }
        };

        BusinessLogicLayer bll = new BusinessLogicLayer(repository);
        List<Price> resultPrices = bll.getPriceRange(expectedStartDate, expectedEndDate, PriceRangeType.MONTH);

        assertEquals(expectedPrices.size(), resultPrices.size());
        assertEquals(expectedPrices, resultPrices);
    }

    @Test
    public void testGetPriceRangeNull() {
        BusinessLogicLayer bll = new BusinessLogicLayer(new FakeRepository());
        Exception e;

        // Range Type is null
        e = assertThrows(IllegalArgumentException.class,
                () -> bll.getPriceRange("2011-01-01", "2011-01-01", null));
        assertEquals("PriceRangeType cannot be null", e.getMessage());

        // Start Date is null
        e = assertThrows(IllegalArgumentException.class,
                () -> bll.getPriceRange(null, "2011-01-01", PriceRangeType.DAY));
        assertEquals("Both dates are required", e.getMessage());

        // Start Date is empty
        e = assertThrows(IllegalArgumentException.class,
                () -> bll.getPriceRange("", "2011-01-01", PriceRangeType.DAY));
        assertEquals("Both dates are required", e.getMessage());

        // End Date is null
        e = assertThrows(IllegalArgumentException.class,
                () -> bll.getPriceRange("2011-01-01", null, PriceRangeType.DAY));
        assertEquals("Both dates are required", e.getMessage());

        // End Date is empty
        e = assertThrows(IllegalArgumentException.class,
                () -> bll.getPriceRange("2011-01-01", "", PriceRangeType.DAY));
        assertEquals("Both dates are required", e.getMessage());
    }

    @Test
    public void testUpdate() throws BitcoinDatabaseException {
        Price expectedPrice = new Price("2011-01-01", "2", "4", "1", "3");
        IRepository repository = new FakeRepository() {
            @Override
            public void update(PriceEntity priceEntity) {
                Price price = Transformer.toModel(priceEntity);
                assertEquals(expectedPrice, price);
            }
        };

        BusinessLogicLayer bll = new BusinessLogicLayer(repository);
        bll.update(expectedPrice);
    }

    @Test
    public void testDelete() throws BitcoinDatabaseException {
        String expectedDateStr = "2011-01-01";
        IRepository repository = new FakeRepository() {
            @Override
            public void delete(String dateStr) {
                assertEquals(expectedDateStr, dateStr);
            }
        };

        BusinessLogicLayer bll = new BusinessLogicLayer(repository);
        bll.delete(expectedDateStr);
    }
}
