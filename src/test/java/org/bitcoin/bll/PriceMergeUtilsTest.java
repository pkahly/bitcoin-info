package org.bitcoin.bll;

import org.bitcoin.bll.model.Price;
import org.bitcoin.bll.model.Transformer;
import org.bitcoin.storage.entity.PriceEntity;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PriceMergeUtilsTest {
    private static final List<PriceEntity> INPUT_PRICES = Transformer.toEntity(List.of(
            new Price("2011-01-01", "2", "4", "1", "3"),
            new Price("2011-01-02", "4", "8", "2", "6"),
            new Price("2011-01-04", "8", "16", "4", "12"),
            new Price("2011-02-01", "8", "16", "4", "12"),
            new Price("2011-02-02", "4", "8", "2", "6"),
            new Price("2011-02-03", "2", "4", "1", "3"),
            new Price("2012-01-02", "2", "4", "1", "3"),
            new Price("2012-01-05", "1", "2", "1", "1"),
            new Price("2013-05-12", "20", "21", "11", "12")
    ));

    @Test
    public void testMergeByDay() {
        // Prices should not be changed
        List<PriceEntity> resultPrices = PriceMergeUtils.mergePrices(new ArrayList<>(INPUT_PRICES), PriceRangeType.DAY);

        assertEquals(INPUT_PRICES.size(), resultPrices.size());
        assertEquals(INPUT_PRICES, resultPrices);
    }

    @Test
    public void testMergeByMonth() {
        // Prices should be combined by month
        List<PriceEntity> expectedPrices = Transformer.toEntity(List.of(
                new Price("2011-01-01", "2", "16", "1", "12"),
                new Price("2011-02-01", "8", "16", "1", "3"),
                new Price("2012-01-01", "2", "4", "1", "1"),
                new Price("2013-05-01", "20", "21", "11", "12")
        ));

        List<PriceEntity> resultPrices = PriceMergeUtils.mergePrices(new ArrayList<>(INPUT_PRICES), PriceRangeType.MONTH);

        assertEquals(expectedPrices.size(), resultPrices.size());
        assertEquals(expectedPrices, resultPrices);
    }

    @Test
    public void testMergeByYear() {
        // Prices should be combined by year
        List<PriceEntity> expectedPrices = Transformer.toEntity(List.of(
                new Price("2011-01-01", "2", "16", "1", "3"),
                new Price("2012-01-01", "2", "4", "1", "1"),
                new Price("2013-01-01", "20", "21", "11", "12")
        ));

        List<PriceEntity> resultPrices = PriceMergeUtils.mergePrices(INPUT_PRICES, PriceRangeType.YEAR);

        assertEquals(expectedPrices.size(), resultPrices.size());
        assertEquals(expectedPrices, resultPrices);
    }
}
