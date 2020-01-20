package de.oth.tdanylenko.tdanbank.exceptions;

public class BalanceInsufficientException extends RuntimeException {
    private static final String EXCEPTION_MESSAGE = "Transaction failed: Insufficient balance ";
    public BalanceInsufficientException (){
        super(EXCEPTION_MESSAGE);
    }
}