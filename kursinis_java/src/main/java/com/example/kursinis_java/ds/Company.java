package com.example.kursinis_java.ds;

import javax.persistence.Entity;

@Entity
public class Company extends User{
    private String companyName;
    private String companyRepresentative;
    private String companyAddress;
    private String companyPhone;

    public Company() {
    }

    public Company(String login, String password, String companyName, String companyRepresentative, String companyAddress, String companyPhone) {
        super(login, password);
        this.companyName = companyName;
        this.companyRepresentative = companyRepresentative;
        this.companyAddress = companyAddress;
        this.companyPhone = companyPhone;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyRepresentative() {
        return companyRepresentative;
    }

    public void setCompanyRepresentative(String representative) {
        this.companyRepresentative = representative;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    public String getCompanyPhone() {
        return companyPhone;
    }

    public void setCompanyPhone(String phoneNumber) {
        this.companyPhone = phoneNumber;
    }

    @Override
    public String toString() {
        return getLogin();
    }
}
