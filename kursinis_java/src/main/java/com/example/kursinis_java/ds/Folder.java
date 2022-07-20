package com.example.kursinis_java.ds;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.List;

@Entity
public class Folder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String title;
    @ManyToOne
    private Course parentCourse;
    @OneToMany(mappedBy = "parentFolder", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @OrderBy("id ASC")
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Folder> subFolders;
    @ManyToOne
    private Folder parentFolder;
    @ManyToOne
    private User responsible;
    @ManyToOne
    private User creator;
    @ManyToMany(mappedBy = "myFolders", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @OrderBy("id ASC")
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<User> editors;

    public Folder(int id, String title, Course parentCourse, List<Folder> subFolders, Folder parentFolder, Person responsible, User creator, List<User> editors) {
        this.id = id;
        this.title = title;
        this.parentCourse = parentCourse;
        this.subFolders = subFolders;
        this.parentFolder = parentFolder;
        this.responsible = responsible;
        this.creator = creator;
        this.editors = editors;
    }

    public Folder(String title, Course parentCourse, List<Folder> subFolders, Folder parentFolder, Person responsible, User creator, List<User> editors) {
        this.title = title;
        this.parentCourse = parentCourse;
        this.subFolders = subFolders;
        this.parentFolder = parentFolder;
        this.responsible = responsible;
        this.creator = creator;
        this.editors = editors;
    }

    public Folder() {
    }

    public Folder(String title, Folder parentFolder, User responsible, User creator) {
        this.title = title;
        this.parentFolder = parentFolder;
        this.responsible = responsible;
        this.creator = creator;
    }
    public Folder(String title, User responsible, User creator, Course parentCourse) {
        this.title = title;
        this.parentCourse = parentCourse;
        this.responsible = responsible;
        this.creator = creator;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public User getResponsible() {
        return responsible;
    }

    public void setResponsible(User responsible) {
        this.responsible = responsible;
    }

    public Folder(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Course getParentCourse() {
        return parentCourse;
    }

    public void setParentCourse(Course parentCourse) {
        this.parentCourse = parentCourse;
    }

    public List<Folder> getSubFolders() {
        return subFolders;
    }

    public void setSubFolders(List<Folder> subFolders) {
        this.subFolders = subFolders;
    }

    public Folder getParentFolder() {
        return parentFolder;
    }

    public void setParentFolder(Folder parentFolder) {
        this.parentFolder = parentFolder;
    }

    public List<User> getEditors() {
        return editors;
    }

    public void setEditors(List<User> editors) {
        this.editors = editors;
    }

    @Override
    public String toString() {
        return title;
    }
}
