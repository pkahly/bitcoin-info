package org.bitcoin;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.bitcoin.bll.BusinessLogicLayer;
import org.bitcoin.bll.IBusinessLogicLayer;
import org.bitcoin.rest.WebServer;
import org.bitcoin.storage.IRepository;
import org.bitcoin.storage.Repository;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());
    private static final String CONFIG = "/config.json";

    public static void main(String[] args) {
        try {
            // Load configuration
            Config config = loadConfig();

            // Create database connection
            IRepository repository = new Repository();
            IBusinessLogicLayer bll = new BusinessLogicLayer(repository);

            // Load price objects from CSV files
            PriceLoader priceLoader = new PriceLoader(bll);
            int numAdded = priceLoader.loadPriceFiles(config.getPriceHistoryPath());
            LOGGER.info(String.format("Loaded %s records", numAdded));

            // Start server
            WebServer webServer = new WebServer(bll);
            webServer.run();
        } catch (Throwable e) {
            LOGGER.log(Level.SEVERE, "Failed", e);
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