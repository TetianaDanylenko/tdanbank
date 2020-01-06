package de.oth.tdanylenko.tdanbank.exceptions;

public class TransactionNotFoundException extends RuntimeException {
    private static final String EXCEPTION_MESSAGE = "Transaction failed";

    public TransactionNotFoundException (){
        super(EXCEPTION_MESSAGE);
    }
}

