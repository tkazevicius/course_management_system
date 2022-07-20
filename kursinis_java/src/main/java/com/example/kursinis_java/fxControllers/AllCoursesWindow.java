package com.example.kursinis_java.fxControllers;

import com.example.kursinis_java.StartGui;
import com.example.kursinis_java.ds.Course;
import com.example.kursinis_java.ds.Folder;
import com.example.kursinis_java.ds.User;
import com.example.kursinis_java.hibernateControllers.CourseHibernateController;
import com.example.kursinis_java.hibernateControllers.UserHibernateController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.stage.Stage;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class AllCoursesWindow implements Initializable {
    @FXML
    public ListView<String> courseList;
    @FXML
    public TreeView<String> courseFolders;
    private int userId;

    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("CourseMngSys");
    CourseHibernateController courseHibernateController = new CourseHibernateController(entityManagerFactory);
    UserHibernateController userHibernateController = new UserHibernateController(entityManagerFactory);

    public void setAllCoursesWindow(int id){
        this.userId = id;
        fillTables();
    }
    private void fillTables(){
        courseList.getItems().clear();
        List<Course> myCoursesFromDb = courseHibernateController.getAllCourses(true, -1, -1);
        for(Course p : myCoursesFromDb){
            courseList.getItems().add(p.getId() + ":" + p.getTitle());
        }
    }

    public void registerToCourse(ActionEvent actionEvent) {
        Course course = courseHibernateController.getCourseById(Integer.parseInt(courseList.getSelectionModel().getSelectedItem().toString().split(":")[0]));
        User user =  userHibernateController.getUserById(userId);
        System.out.println(user);
        System.out.println(course);
        user.getMyModeratedCourses().add(course);
        userHibernateController.editUser(user);
    }

    public void returnToMain(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(StartGui.class.getResource("main-course-window.fxml"));
        Parent root = fxmlLoader.load();

        MainCourseWindow mainProjectsWindow = fxmlLoader.getController();
        mainProjectsWindow.setCourseFormData(userId);

        Scene scene = new Scene(root);
        Stage stage = (Stage) courseList.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    public void loadFolders() {
        Course selectedCourse = courseHibernateController.getCourseById(
                Integer.parseInt(courseList.getSelectionModel().getSelectedItem().split(":")[0])
        );
        courseFolders.setRoot(new TreeItem<String>("Course folders:"));
        courseFolders.setShowRoot(false);
        courseFolders.getRoot().setExpanded(true);
        selectedCourse.getCourseFolders().forEach(folder -> addTreeItem(folder, courseFolders.getRoot()));
    }

    private void addTreeItem(Folder folder, TreeItem parentFolder) {
        TreeItem<Folder> treeItem = new TreeItem<>(folder);
        parentFolder.getChildren().add(treeItem);
        folder.getSubFolders().forEach(sub -> addTreeItem(sub, treeItem));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
