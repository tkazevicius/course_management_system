package com.example.kursinis_java.ds;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.List;

@Entity
public class Person extends User {
    private String name;
    private String surname;
    private String cardNumber;
    private String studentNumber;
    @ManyToMany
    private List<Course> myEnrolledCourses;


    public Person() {
    }

    public Person(String login, String password, String name, String surname, String cardNumber, String studentNumber) {
        super(login, password);
        this.name = name;
        this.surname = surname;
        this.cardNumber = cardNumber;
        this.setUserType(UserType.STUDENT);
        this.studentNumber = studentNumber;
    }
    public Person(String login, String password, String name, String surname, String cardNumber, String studentNumber, UserType userType) {
        super(login, password);
        this.name = name;
        this.surname = surname;
        this.cardNumber = cardNumber;
        this.setUserType(UserType.MODERATOR);
        this.studentNumber = studentNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    @Override
    public String toString() {
        return getLogin();
    }
}
