package de.oth.tdanylenko.tdanbank.exceptions;

public class TransactionServiceException extends RuntimeException {
    public TransactionServiceException(String message){
        super(message);
    }
}
