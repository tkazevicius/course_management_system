package com.example.kursinis_java.fxControllers;

import com.example.kursinis_java.StartGui;
import com.example.kursinis_java.ds.Course;
import com.example.kursinis_java.ds.Folder;
import com.example.kursinis_java.ds.UserType;
import com.example.kursinis_java.hibernateControllers.CourseHibernateController;

import com.example.kursinis_java.hibernateControllers.FolderHibernateController;
import com.example.kursinis_java.hibernateControllers.UserHibernateController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MainCourseWindow implements Initializable {
    @FXML
    public ListView myEnrolledCoursesList;
    @FXML
    public TreeView courseFolders;
    @FXML
    public Button createCourseButton;
    @FXML
    public MenuItem deleteCourseButton;
    @FXML
    public MenuItem editCourseButton;
    @FXML
    public MenuItem allCourses;
    @FXML
    public MenuItem addNewFolderButton;
    @FXML
    public MenuItem editFolderButton;
    @FXML
    public MenuItem deleteFolderButton;
    @FXML
    public Menu UsersButton;

    private int userId;

    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("CourseMngSys");
    CourseHibernateController courseHibernateController = new CourseHibernateController(entityManagerFactory);
    UserHibernateController userHibernateController = new UserHibernateController(entityManagerFactory);
    FolderHibernateController folderHibernateController = new FolderHibernateController(entityManagerFactory);

    public void setCourseFormData(int id){
        this.userId = id;
        fillTables();
        buttonView(userId);
    }

    private void fillTables(){
        myEnrolledCoursesList.getItems().clear();
        List<Course> myCoursesFromDb = courseHibernateController.getCourseByUserId(userId);
        for(Course p : myCoursesFromDb){
            myEnrolledCoursesList.getItems().add(p.getId() + ":" + p.getTitle());
        }
    }

    public void createCourse(ActionEvent actionEvent) throws IOException {
        loadCourseWindow(false, 0);
    }

    public void editSelected() throws IOException {
        loadCourseWindow(true, Integer.parseInt(myEnrolledCoursesList.getSelectionModel().getSelectedItem().toString().split(":")[0]));
    }
    public void deleteSelected() throws IOException {
        courseHibernateController.removeCourse(Integer.parseInt(myEnrolledCoursesList.getSelectionModel().getSelectedItem().toString().split(":")[0]));
        fillTables();
        courseFolders.getRoot().getChildren().clear();
    }

    public void loadCourseWindow(boolean edit, int courseId) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(StartGui.class.getResource("new-course-window.fxml"));
        Parent root = fxmlLoader.load();
        NewCourseWindow newCourseWindow = fxmlLoader.getController();
        newCourseWindow.setCourseFormData(userId, edit, courseId);
        Scene scene = new Scene(root);
        Stage stage = (Stage) myEnrolledCoursesList.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void buttonView(int userId){
        if(userHibernateController.getUserById(userId).getUserType() == UserType.COMPANY){
            createCourseButton.setVisible(false);
            deleteCourseButton.setVisible(false);
            editCourseButton.setVisible(false);
        }
        if(userHibernateController.getUserById(userId).getUserType() != UserType.MODERATOR){
            UsersButton.setVisible(false);
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public void loadFolders() {
        Course selectedCourse = courseHibernateController.getCourseById(
                Integer.parseInt(myEnrolledCoursesList.getSelectionModel().getSelectedItem().toString().split(":")[0])
        );
        courseFolders.setRoot(new TreeItem<String>("Course folders:"));
        courseFolders.setShowRoot(false);
        courseFolders.getRoot().setExpanded(true);
        selectedCourse.getCourseFolders().forEach(folder -> addTreeItem(folder, courseFolders.getRoot()));
        selectedCourse.getCourseFolders().forEach(folder -> {
            if((folder.getResponsible().getId() == userId) || (folder.getCreator().getId() == userId)){
                editFolderButton.setVisible(true);
                addNewFolderButton.setVisible(true);
                deleteFolderButton.setVisible(true);
            } else{
                editFolderButton.setVisible(false);
                addNewFolderButton.setVisible(false);
                deleteFolderButton.setVisible(false);
            }
        });
    }

    private void addTreeItem(Folder folder, TreeItem parentFolder) {

        TreeItem<Folder> treeItem = new TreeItem<>(folder);
        parentFolder.getChildren().add(treeItem);
        folder.getSubFolders().forEach(sub -> addTreeItem(sub, treeItem));

    }

    public void addFolder(ActionEvent actionEvent) throws IOException {
        TreeItem<Folder> folderTreeItem = (TreeItem<Folder>) courseFolders.getSelectionModel().getSelectedItem();
        if(folderTreeItem == null){
            loadFolderWindow(false, 0, Integer.parseInt(myEnrolledCoursesList.getSelectionModel().getSelectedItem().toString().split(":")[0]), 0);
        } else{
            loadFolderWindow(false, folderTreeItem.getValue().getId(), 0, 0);
        }
    }

    public void editFolder(ActionEvent actionEvent) throws IOException {
        TreeItem<Folder> folderTreeItem = (TreeItem<Folder>) courseFolders.getSelectionModel().getSelectedItem();
        loadFolderWindow(true, 0, 0, folderTreeItem.getValue().getId());
    }

    public void deleteFolder(ActionEvent actionEvent) {
        TreeItem<Folder> folderTreeItem = (TreeItem<Folder>) courseFolders.getSelectionModel().getSelectedItem();
        folderHibernateController.remove(folderTreeItem.getValue().getId());
        courseFolders.getRoot().getChildren().clear();
    }

    private void loadFolderWindow(boolean edit, int folderId, int courseId, int currentFolder) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(StartGui.class.getResource("new-folder-window.fxml"));
        Parent root = fxmlLoader.load();
        NewFolderWindow newFolderWindow = fxmlLoader.getController();
        newFolderWindow.setData(edit, folderId, courseId, userId, currentFolder);
        Scene scene = new Scene(root);
        Stage stage = (Stage) courseFolders.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void goToAllCourses(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(StartGui.class.getResource("all-courses-window.fxml"));
        Parent root = fxmlLoader.load();
        AllCoursesWindow allCoursesWindow = fxmlLoader.getController();
        allCoursesWindow.setAllCoursesWindow(userId);
        Scene scene = new Scene(root);
        Stage stage = (Stage) myEnrolledCoursesList.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void checkFolderInformation(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(StartGui.class.getResource("folder-information-window.fxml"));
        Parent root = fxmlLoader.load();
        FolderInformationWindow folderInformationWindow = fxmlLoader.getController();
        TreeItem<Folder> folderTreeItem = (TreeItem<Folder>) courseFolders.getSelectionModel().getSelectedItem();;
        int parentFolderId = 0;
        if(folderTreeItem.getValue().getParentFolder() != null) parentFolderId = folderTreeItem.getValue().getParentFolder().getId();
        folderInformationWindow.setData(parentFolderId, userId ,folderTreeItem.getValue().getId());
        Scene scene = new Scene(root);
        Stage stage = (Stage) myEnrolledCoursesList.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void goToAllUsers(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(StartGui.class.getResource("all-users-window.fxml"));
        Parent root = fxmlLoader.load();
        AllUsersWindow allUsersWindow = fxmlLoader.getController();
        allUsersWindow.setAllUsersWindow(userId);
        Scene scene = new Scene(root);
        Stage stage = (Stage) myEnrolledCoursesList.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}
