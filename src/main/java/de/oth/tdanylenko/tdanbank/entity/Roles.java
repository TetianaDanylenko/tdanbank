package de.oth.tdanylenko.tdanbank.entity;

import javax.persistence.*;

@Entity
public class Roles {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Override
    public String toString() {
        return "Roles{" +
                "id=" + id +
                ", name=" + name +
                '}';
    }

    @Enumerated(EnumType.STRING)
    private RoleTypes name;

    public Roles() {
        super();
    }

    public Roles(RoleTypes roleName) {
        this.name = roleName;
    }

    public long getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public RoleTypes getName() {
        return name;
    }

    public void setName(RoleTypes name) {
        this.name = name;
    }
}

