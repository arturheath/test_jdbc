package com.arturheath;

import java.io.FileInputStream;
import java.io.InputStream;

public class Contact {
    private String name;
    private String phoneNumber;
    private InputStream photo;

    public Contact(String name, String phoneNumber, InputStream photo) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.photo = photo;
    }

    public Contact(){
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public InputStream getPhoto() {
        return photo;
    }

    public void setPhoto(InputStream photo) {
        this.photo = photo;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "name='" + name + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", photo=" + photo +
                '}';
    }
}
