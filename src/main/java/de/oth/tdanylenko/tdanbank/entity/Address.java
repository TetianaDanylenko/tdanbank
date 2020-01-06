package de.oth.tdanylenko.tdanbank.entity;

import javax.persistence.*;

@Embeddable
public class Address {
    private String street;
    private String streetAddition;
    private int houseNr;
    private String city;
    private String zip;

    public Address() {
        super();
    }

    public Address(String street, String streetAddition, int houseNr, String city, String zip) {
        this.street = street;
        this.streetAddition = streetAddition;
        this.houseNr = houseNr;
        this.city = city;
        this.zip = zip;
    }

    @Override
    public String toString() {
        return "Address{" +
                ", street='" + street + '\'' +
                ", streetAddition='" + streetAddition + '\'' +
                ", houseNr=" + houseNr +
                ", city='" + city + '\'' +
                ", zip='" + zip + '\'' +
                '}';
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getStreetAddition() {
        return streetAddition;
    }

    public void setStreetAddition(String streetAddition) {
        this.streetAddition = streetAddition;
    }

    public long getHouseNr() {
        return houseNr;
    }

    public void setHouseNr(int houseNr) {
        this.houseNr = houseNr;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }
}
