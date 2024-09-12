package org.bitcoin;

import org.bitcoin.storage.PriceRecord;
import org.bitcoin.storage.PriceStore;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class PriceLoader {
    private static final String DATE = "Date";
    private static final String TIMESTAMP = "Timestamp";
    private static final String OPEN = "Open";
    private static final String HIGH = "High";
    private static final String LOW = "Low";
    private static final String CLOSE = "Close";

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("MMMddyyyy");
    private static final DateFormat DATE_FORMAT_2 = new SimpleDateFormat("MMMdyyyy");
    private static final DateFormat TIMESTAMP_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    private final Date MIN_DATE;
    private final Date MAX_DATE;

    private final PriceStore store;

    public PriceLoader(PriceStore store) throws Exception {
        this.store = store;

        MIN_DATE = TIMESTAMP_FORMAT.parse("2009-01-01");
        MAX_DATE = TIMESTAMP_FORMAT.parse("2026-01-01");
    }

    public void loadPriceFiles(String basePath) throws IOException {
        // Load all the price files from the base path
        try (Stream<Path> stream = Files.list(Path.of(basePath))) {
            stream.forEach(this::importPriceFile);
        }
    }

    private void importPriceFile(Path priceFile) {
        try (BufferedReader br = new BufferedReader(new FileReader(priceFile.toString()))) {
            // Process the CSV header
            Map<String, Integer> headerMap = processHeader(br.readLine());

            // Read the remainder of the file
            br.lines().forEach(line -> {
                try {
                    store.addRecord(processLine(headerMap, line));
                } catch (Exception e) {
                    System.out.println("Skipping Bad Line: " + line);
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            throw new RuntimeException("Failed to import file: " + priceFile, e);
        }
    }

    private Map<String, Integer> processHeader(String header) {
        // Split the header by comma
        String[] split = header.split(",");

        // Build a map of column name -> index
        Map<String, Integer> headerMap = new HashMap<>();
        for (int i = 0; i < split.length; i++) {
            headerMap.put(split[i], i);
        }

        // Validate required headers
        if (!headerMap.containsKey(DATE) && !headerMap.containsKey(TIMESTAMP)) {
            throw new IllegalArgumentException("Either 'Date' or 'Timestamp' header is required");
        }
        for (String requiredHeader : List.of(OPEN, HIGH, LOW, CLOSE)) {
            if (!headerMap.containsKey(requiredHeader)) {
                throw new IllegalArgumentException(requiredHeader + " header is required");
            }
        }

        return headerMap;
    }

    private PriceRecord processLine(Map<String, Integer> headerMap, String line) throws ParseException {
        // Split the line by comma
        String[] splitLine = line.split(",");

        // Get the date
        Date date = getDate(headerMap, splitLine);
        //System.out.println(TIMESTAMP_FORMAT.format(date));

        // Get the price data
        String open = splitLine[headerMap.get(OPEN)];
        String high = splitLine[headerMap.get(HIGH)];
        String low = splitLine[headerMap.get(LOW)];
        String close = splitLine[headerMap.get(CLOSE)];
        validatePricing(open, high, low, close);

        return new PriceRecord(date, open, high, low, close);
    }

    private void validatePricing(String openStr, String highStr, String lowStr, String closeStr) {
        double open = Double.parseDouble(openStr);
        double high = Double.parseDouble(highStr);
        double low = Double.parseDouble(lowStr);
        double close = Double.parseDouble(closeStr);

        if (high < Math.max(Math.max(open, low), close)) {
            throw new IllegalArgumentException("High should be the largest number");
        }
        if (low > Math.min(Math.min(open, high), close)) {
            throw new IllegalArgumentException("Low should be the smallest number");
        }
    }

    private Date getDate(Map<String, Integer> headerMap, String[] splitLine) throws ParseException {
        // Parse the date from one of two formats
        Date date;
        if (headerMap.containsKey(DATE)) {
            String dateStr = splitLine[headerMap.get(DATE)];
            if (dateStr.length() == 9) {
                // Normal format
                date = DATE_FORMAT.parse(dateStr);
            } else if (dateStr.length() == 8) {
                // Days are not padded with a zero
                date = DATE_FORMAT_2.parse(dateStr);
            } else {
                throw new IllegalArgumentException("Invalid date string: " + dateStr);
            }
        } else {
            String timestampStr = splitLine[headerMap.get(TIMESTAMP)];
            date = TIMESTAMP_FORMAT.parse(timestampStr);
        }

        // Validate the data
        if (date.compareTo(MIN_DATE) <= 0 || date.compareTo(MAX_DATE) >= 0) {
            throw new IllegalArgumentException("Bad Date: " + date);
        }

        return date;
    }
}
