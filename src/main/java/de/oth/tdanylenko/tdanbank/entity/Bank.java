package de.oth.tdanylenko.tdanbank.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
public class Bank {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String BIC;
    private String name;
    @OneToMany(mappedBy = "bank", orphanRemoval = true, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Collection<BusinessProducts> businessProducts = new ArrayList<BusinessProducts>();;

    public Bank() {
      super();
    }
    public Bank(String BIC, String name, Collection<BusinessProducts> businessProducts) {
        this.BIC = BIC;
        this.name = name;
        this.businessProducts = businessProducts;
    }
    public Bank(String BIC, String name) {
        this.BIC = BIC;
        this.name = name;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBIC() {
        return BIC;
    }

    public void setBIC(String BIC) {
        this.BIC = BIC;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<BusinessProducts> getBusinessProducts() {
        return businessProducts;
    }

    public void setBusinessProducts(Collection<BusinessProducts> businessProducts) {
        this.businessProducts = businessProducts;
    }
}

