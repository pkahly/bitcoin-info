package org.bitcoin.exception;

public class BitcoinException extends Exception {
    public BitcoinException(Exception cause) {
        super(cause);
    }

    public BitcoinException(String message, Exception cause) {
        super(message, cause);
    }

    public BitcoinException(String message) {
        super(message);
    }
}
