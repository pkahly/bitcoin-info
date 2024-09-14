package org.bitcoin.bll.model;

import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TransformerTest {
    @Test
    public void testStrToDate() throws ParseException {
        String expected = "2008-01-01";

        // Convert from String to Date to String
        LocalDate date = Transformer.strToDate(expected);
        String result = Transformer.dateToStr(date);

        assertEquals(expected, result);
    }

    @Test
    @Deprecated
    public void testDateToStr() throws ParseException {
        LocalDate expected = LocalDate.of(2010, Month.MAY, 16);

        // Convert from Date to String to Date
        String str = Transformer.dateToStr(expected);
        LocalDate result = Transformer.strToDate(str);

        assertEquals(expected, result);
    }
}
