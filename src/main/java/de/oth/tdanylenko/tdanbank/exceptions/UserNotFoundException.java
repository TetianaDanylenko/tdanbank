package de.oth.tdanylenko.tdanbank.exceptions;

public class UserNotFoundException extends RuntimeException {
    private static final String EXCEPTION_MESSAGE = "User not found";

    public UserNotFoundException (){
        super(EXCEPTION_MESSAGE);
    }
}
