package org.bitcoin;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.bitcoin.storage.Repository;

import java.io.IOException;
import java.io.InputStream;

public class Main {
    private static final String CONFIG = "/config.json";

    public static void main(String[] args) throws IOException {
        try {
            Config config = loadConfig();
            Repository store = new Repository();

            PriceLoader priceLoader = new PriceLoader(store);
            int numAdded = priceLoader.loadPriceFiles(config.getPriceHistoryPath());

            System.out.printf("Loaded %s records", numAdded);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private static Config loadConfig() throws IOException {
        try (InputStream configStream = Main.class.getResourceAsStream(CONFIG);) {
            if (configStream == null) {
                throw new IllegalArgumentException("config.json resource is required");
            }

            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(configStream, Config.class);
        }
    }
}