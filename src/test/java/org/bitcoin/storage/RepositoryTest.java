package org.bitcoin.storage;

import org.bitcoin.bll.model.Transformer;
import org.bitcoin.exception.BitcoinDatabaseException;
import org.bitcoin.storage.entity.PriceEntity;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

public class RepositoryTest {
    private static final Logger LOGGER = Logger.getLogger(RepositoryTest.class.getName());

    private static Repository repository;

    @BeforeAll
    public static void setUpClass() {
        repository = new Repository();
    }

    @Test
    public void testCreateDelete() throws ParseException, BitcoinDatabaseException {
        String dateStr = "2010-01-01";
        PriceEntity entity = createPriceEntity(dateStr);

        // Create record
        repository.create(entity);

        // Verify record is present
        Optional<PriceEntity> result = repository.getPrice(dateStr);
        assertTrue(result.isPresent());
        assertEquals(entity, result.get());

        // Delete record
        repository.delete(dateStr);

        // Verify record is gone
        result = repository.getPrice(dateStr);
        assertFalse(result.isPresent());
    }

    @Test
    public void testGetPriceRange() throws ParseException, BitcoinDatabaseException {
        String tooEarly = "2008-01-01";
        String match1 = "2012-01-01";
        String match2 = "2012-01-05";
        String match3 = "2012-01-07";
        String match4 = "2012-01-10";
        String match5 = "2012-01-14";
        String tooLate = "2020-01-01";

        // Create Price records
        for (String dateStr : List.of(tooEarly, match1, match2, match3, match4, match5, tooLate)) {
            // Create
            repository.create(createPriceEntity(dateStr));

            // Verify record is present
            Optional<PriceEntity> result = repository.getPrice(dateStr);
            assertTrue(result.isPresent());
        }

        List<PriceEntity> matches = repository.getPriceRange(match1, match5);

        // Check that we have the correct matches in the correct order
        assertEquals(5, matches.size());
        assertEquals(match1, matches.get(0).getDateStr());
        assertEquals(match2, matches.get(1).getDateStr());
        assertEquals(match3, matches.get(2).getDateStr());
        assertEquals(match4, matches.get(3).getDateStr());
        assertEquals(match5, matches.get(4).getDateStr());
    }

    private PriceEntity createPriceEntity(String dateStr) throws ParseException {
        return new PriceEntity(
                dateStr,
                Transformer.strToDate(dateStr),
                "1",
                "2",
                "1",
                "1"
        );
    }
}
