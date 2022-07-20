package com.example.kursinis_java;

import com.example.kursinis_java.ds.*;
import com.example.kursinis_java.hibernateControllers.CourseHibernateController;
import com.example.kursinis_java.hibernateControllers.UserHibernateController;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class CreateAdminAccount {
    public static void main(String[] args) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("CourseMngSys");
        UserHibernateController userHibernateController = new UserHibernateController(entityManagerFactory);
        CourseHibernateController courseHibernateController = new CourseHibernateController(entityManagerFactory);
        Person person = new Person("admin", "admin", "admin", "admin", "admin", "admin", UserType.MODERATOR);
        userHibernateController.createUser(person);

    }
}
