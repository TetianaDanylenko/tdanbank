package de.oth.tdanylenko.tdanbank.entity;

import de.oth.tdanylenko.tdanbank.enums.ProductType;

import javax.persistence.*;
import java.util.List;

@Entity
public class BusinessProducts {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;
    @Enumerated(EnumType.STRING)
    private ProductType type;
    private String description;
    private double price;
    @ManyToOne(fetch = FetchType.LAZY)
    private Bank bank;

    public BusinessProducts(String name, ProductType type, String description, double price, Bank bank) {
        this.name = name;
        this.type = type;
        this.description = description;
        this.price = price;
        this.bank = bank;
    }
    public BusinessProducts() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ProductType getType() {
        return type;
    }

    public void setType(ProductType type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Bank getBank() {
        return bank;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }
}
