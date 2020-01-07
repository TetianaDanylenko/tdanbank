package de.oth.tdanylenko.tdanbank.entity;

import de.oth.tdanylenko.tdanbank.enums.AccountStatus;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userID", referencedColumnName = "id")
    private User user;
    private double balance;
    private String currency;
    @ElementCollection
    private List<Tan> tanList;
    private String IBAN;
    @ElementCollection
    private List<Transaction> transactionHistory;
    @Enumerated(EnumType.STRING)
    private AccountStatus status;
    public Account () {
        super();
    }

    public Account(User user, double balance, String currency, List<Tan> tanList, String IBAN, List<Transaction> transactionHistory, AccountStatus status) {
        this.user = user;
        this.balance = balance;
        this.currency = currency;
        this.tanList = tanList;
        this.IBAN = IBAN;
        this.transactionHistory = transactionHistory;
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public AccountStatus getStatus() {
        return status;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public List<Tan> getTanList() {
        return tanList;
    }

    public void setTanList(List<Tan> tanList) {
        this.tanList = tanList;
    }

    public String getIBAN() {
        return IBAN;
    }

    public void setIBAN(String IBAN) {
        this.IBAN = IBAN;
    }

    public List<Transaction> getTransactionHistory() {
        return transactionHistory;
    }

    public void setTransactionHistory(List<Transaction> transactionHistory) {
        this.transactionHistory = transactionHistory;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
