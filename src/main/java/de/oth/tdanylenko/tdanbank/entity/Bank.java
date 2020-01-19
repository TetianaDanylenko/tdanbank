package de.oth.tdanylenko.tdanbank.entity;

import javax.persistence.*;
import java.util.List;

@Entity
public class Bank {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String BIC;
    private String name;
    @ElementCollection
    private List<BusinessProducts> businessProducts;
}
