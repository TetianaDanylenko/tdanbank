package de.oth.tdanylenko.tdanbank.entity;

import javax.persistence.*;

@Embeddable
public class Tan {
    private String value;
    private boolean isUsed;
    private long userID;
}
