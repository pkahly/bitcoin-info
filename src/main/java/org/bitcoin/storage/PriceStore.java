package org.bitcoin.storage;

import java.util.HashMap;
import java.util.Map;

// TODO - replace with a database
public class PriceStore {
    private final Map<String, PriceRecord> records = new HashMap<>();

    public void addRecord(PriceRecord record) {
        String key = record.getDateString();
        if (records.containsKey(key)) {
            System.out.println("Found duplicate record for key: " + key);
            System.out.println(records.get(key));
            System.out.println(record);
        }

        records.put(key, record);
    }

    public int size() {
        return records.size();
    }
}
