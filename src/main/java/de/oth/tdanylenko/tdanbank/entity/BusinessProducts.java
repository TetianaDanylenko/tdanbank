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
    private long price;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "bankID", referencedColumnName = "id")
    private Bank bankInstitutionId;
}
