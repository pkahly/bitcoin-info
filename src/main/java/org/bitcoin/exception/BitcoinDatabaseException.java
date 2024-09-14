package org.bitcoin.exception;

public class BitcoinDatabaseException extends BitcoinException {
    public BitcoinDatabaseException(Exception cause) {
        super(cause);
    }

    public BitcoinDatabaseException(String message) {
        super(message);
    }
}
