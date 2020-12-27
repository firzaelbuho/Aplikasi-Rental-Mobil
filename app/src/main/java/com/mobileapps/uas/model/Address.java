package com.mobileapps.uas.model;

public class Address{

    private String city;
    private String district;
    private String subDistrict;
    private String street ;

    public Address(String city, String district, String subDistrict, String street) {
        this.city = city;
        this.district = district;
        this.subDistrict = subDistrict;
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getSubDistrict() {
        return subDistrict;
    }

    public void setSubDistrict(String subDistrict) {
        this.subDistrict = subDistrict;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }
}
