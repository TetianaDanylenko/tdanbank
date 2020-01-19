package de.oth.tdanylenko.tdanbank.entity;

import de.oth.tdanylenko.tdanbank.enums.TransactionStatus;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @ManyToOne(fetch = FetchType.EAGER,  cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    private Account from;
    @ManyToOne(fetch = FetchType.EAGER,  cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    private Account to;
    private LocalDateTime timeStamp;
    private String TAN;
    private double amount;
    private String reasonForPayment;
    private String currency;
    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    public Transaction(Account from, Account to, LocalDateTime timeStamp, String TAN, double amount, String reasonForPayment, String currency, TransactionStatus status) {
        this.from = from;
        this.to = to;
        this.timeStamp = timeStamp;
        this.TAN = TAN;
        this.amount = amount;
        this.reasonForPayment = reasonForPayment;
        this.currency = currency;
        this.status = status;
    }
    public Transaction(Account from, LocalDateTime timeStamp, double amount, String reasonForPayment, String currency, TransactionStatus status) {
        this.from = from;
        this.timeStamp = timeStamp;
        this.amount = amount;
        this.reasonForPayment = reasonForPayment;
        this.currency = currency;
        this.status = status;
    }
    public Transaction() {
        super();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Account getFrom() {
        return from;
    }

    public void setFrom(Account from) {
        this.from = from;
    }

    public Account getTo() {
        return to;
    }

    public void setTo(Account to) {
        this.to = to;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getReasonForPayment() {
        return reasonForPayment;
    }

    public void setReasonForPayment(String reasonForPayment) {
        this.reasonForPayment = reasonForPayment;
    }

    public String getTAN() {
        return TAN;
    }

    public void setTAN(String TAN) {
        this.TAN = TAN;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", from=" + from +
                ", to=" + to +
                ", timeStamp=" + timeStamp +
                ", TAN='" + TAN + '\'' +
                ", amount=" + amount +
                ", reasonForPayment='" + reasonForPayment + '\'' +
                ", currency='" + currency + '\'' +
                ", status=" + status +
                '}';
    }
}
