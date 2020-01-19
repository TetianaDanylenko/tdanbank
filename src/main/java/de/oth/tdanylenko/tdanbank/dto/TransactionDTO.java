package de.oth.tdanylenko.tdanbank.dto;

import de.oth.tdanylenko.tdanbank.enums.TransactionStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransactionDTO {
    private double amount;
    private String senderIBAN;
    private String reasonForPayment;
    private Boolean isSuccessful;

    public TransactionDTO() {
        super();
    }

    public TransactionDTO(double amount, String senderIBAN, String receiverIBAN, String reasonForPayment) {
        this.amount = amount;
        this.senderIBAN = senderIBAN;
        this.reasonForPayment = reasonForPayment;
    }

    public TransactionDTO(double amount, String senderIBAN, String reasonForPayment, Boolean isSuccessful) {
        this.amount = amount;
        this.senderIBAN = senderIBAN;
        this.reasonForPayment = reasonForPayment;
        this.isSuccessful = isSuccessful;
    }

    public Boolean getSuccessful() {
        return isSuccessful;
    }

    public void setSuccessful(Boolean successful) {
        isSuccessful = successful;
    }

    public String getSenderIBAN() {
        return senderIBAN;
    }

    public void setSenderIBAN(String senderIBAN) {
        this.senderIBAN = senderIBAN;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getAmount() {
        return amount;
    }

    public String getReasonForPayment() {
        return reasonForPayment;
    }

    public void setReasonForPayment(String reasonForPayment) {
        this.reasonForPayment = reasonForPayment;
    }
}
