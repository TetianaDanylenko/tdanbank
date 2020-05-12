package de.oth.tdanylenko.tdanbank.exceptions;

public class BankNotFoundException extends RuntimeException {
    private static final String EXCEPTION_MESSAGE = "Bank not found";
    public BankNotFoundException(){
        super(EXCEPTION_MESSAGE);
    }
}