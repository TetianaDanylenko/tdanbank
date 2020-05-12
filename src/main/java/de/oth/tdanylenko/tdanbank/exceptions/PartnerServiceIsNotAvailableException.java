package de.oth.tdanylenko.tdanbank.exceptions;

public class PartnerServiceIsNotAvailableException extends RuntimeException{
        private static final String EXCEPTION_MESSAGE = "Partner service is temporary unavailable";
        public PartnerServiceIsNotAvailableException() {
            super(EXCEPTION_MESSAGE);
        }
}
