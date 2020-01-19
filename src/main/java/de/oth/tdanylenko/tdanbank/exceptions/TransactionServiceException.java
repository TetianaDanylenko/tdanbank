package de.oth.tdanylenko.tdanbank.exceptions;

public class TransactionServiceException extends RuntimeException {
    private static final String EXCEPTION_MESSAGE = "Transaction failed";
    public TransactionServiceException(){
        super(EXCEPTION_MESSAGE);
    }
}
