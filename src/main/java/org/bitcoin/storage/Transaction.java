package org.bitcoin.storage;

import jakarta.persistence.EntityTransaction;

public class Transaction implements AutoCloseable {
    private final EntityTransaction et;

    public Transaction(EntityTransaction et) {
        this.et = et;
        et.begin();
    }

    @Override
    public void close() {
        et.commit();
    }
}
