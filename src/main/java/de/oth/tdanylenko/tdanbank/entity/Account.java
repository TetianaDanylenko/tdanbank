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
    @Enumerated(EnumType.STRING)
    private AccountStatus status;
    private String currency;
    private long balance;
    @ElementCollection
    private List<Tan> tanList;
    private String IBAN;
    boolean isLoggedIn;
    @ElementCollection
    private List<Transaction> transactionHistory;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userID", referencedColumnName = "id")
    private User user;

    public Account () {
        super();
    }
    public Account(AccountStatus status, String currency, long balance, List<Tan> tanList, String IBAN, boolean isLoggedIn, List<Transaction> transactionHistory, User user) {
        this.status = status;
        this.currency = currency;
        this.balance = balance;
        this.tanList = tanList;
        this.IBAN = IBAN;
        this.isLoggedIn = isLoggedIn;
        this.transactionHistory = transactionHistory;
        this.user = user;
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

    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
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

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        isLoggedIn = loggedIn;
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
