package de.oth.tdanylenko.tdanbank.exceptions;

public class AccountNotFoundException extends RuntimeException {
    private static final String EXCEPTION_MESSAGE = "Account not found";

    public AccountNotFoundException (){
        super(EXCEPTION_MESSAGE);
    }
}
