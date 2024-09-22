package org.bitcoin.rest;

import jakarta.ws.rs.core.Response;
import org.bitcoin.FakeBusinessLogicLayer;
import org.bitcoin.bll.PriceRangeType;
import org.bitcoin.bll.model.Price;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PriceResourceTest {
    @Test
    public void testGetPriceNoContent() {
        FakeBusinessLogicLayer bll = new FakeBusinessLogicLayer() {
            @Override
            public Optional<Price> getPrice(String dateStr) {
                return Optional.empty();
            }
        };

        PriceResource resource = new PriceResource(bll);
        Response response = resource.getPrice("");

        assertEquals(Response.Status.NO_CONTENT, response.getStatusInfo());
    }

    @Test
    public void testGetPriceOK() {
        String expectedDateStr = "2011-01-01";
        Price price = new Price(expectedDateStr, "2", "4", "1", "3");
        FakeBusinessLogicLayer bll = new FakeBusinessLogicLayer() {
            @Override
            public Optional<Price> getPrice(String dateStr) {
                if (expectedDateStr.equals(dateStr)) {
                    return Optional.of(price);
                }
                return Optional.empty();
            }
        };

        PriceResource resource = new PriceResource(bll);
        Response response = resource.getPrice(expectedDateStr);

        assertEquals(Response.Status.OK, response.getStatusInfo());
        assertEquals(price, response.getEntity());
    }

    @ParameterizedTest
    @EnumSource(PriceRangeType.class)
    public void testGetPriceRange(PriceRangeType expectedRangeType) throws ParseException {
        String expectedStartDateStr = "2011-01-01";
        String expectedEndDateStr = "2013-01-05";
        List<String> dateStrings = List.of(expectedStartDateStr, "2011-01-02", "2012-01-03", "2012-01-04", expectedEndDateStr);
        List<Price> prices = createPrices(dateStrings);

        FakeBusinessLogicLayer bll = new FakeBusinessLogicLayer() {
            @Override
            public List<Price> getPriceRange(String startDateStr, String endDateStr, PriceRangeType rangeType) {
                assertEquals(expectedStartDateStr, startDateStr);
                assertEquals(expectedEndDateStr, endDateStr);
                assertEquals(expectedRangeType, rangeType);
                return prices;
            }
        };

        PriceResource resource = new PriceResource(bll);
        Response response = resource.getPriceRange(expectedStartDateStr, expectedEndDateStr, expectedRangeType);

        assertEquals(Response.Status.OK, response.getStatusInfo());
        assertEquals(prices, response.getEntity());
    }

    private List<Price> createPrices(List<String> dateStrings) {
        List<Price> prices = new ArrayList<>();

        for (String dateStr : dateStrings) {
            prices.add(new Price(dateStr, "2", "4", "1", "3"));
        }

        return prices;
    }
}
