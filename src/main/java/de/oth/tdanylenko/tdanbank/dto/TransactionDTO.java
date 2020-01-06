package de.oth.tdanylenko.tdanbank.dto;

import de.oth.tdanylenko.tdanbank.enums.TransactionStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransactionDTO {
    private Long id;
    private BigDecimal amount;
    private String senderMail;
    private String receiverMail;
    private LocalDateTime timeStamp;
    private TransactionStatus status;

    public TransactionDTO() {
        super();
    }

    public TransactionDTO(Long id, BigDecimal amount, String senderMail, String receiverMail, LocalDateTime timeStamp, TransactionStatus status) {
        this.id = id;
        this.amount = amount;
        this.senderMail = senderMail;
        this.receiverMail = receiverMail;
        this.timeStamp = timeStamp;
        this.status = status;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setSenderMail(String senderMail) {
        this.senderMail = senderMail;
    }

    public void setReceiverMail(String receiverMail) {
        this.receiverMail = receiverMail;
    }

    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getSenderMail() {
        return senderMail;
    }

    public String getReceiverMail() {
        return receiverMail;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public TransactionStatus getStatus() {
        return status;
    }
}
