package com.example.kursinis_java.fxControllers;

import com.example.kursinis_java.StartGui;
import com.example.kursinis_java.ds.Course;
import com.example.kursinis_java.ds.User;
import com.example.kursinis_java.hibernateControllers.CourseHibernateController;
import com.example.kursinis_java.hibernateControllers.UserHibernateController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.IOException;
import java.net.URL;
import java.time.ZoneId;
import java.util.Date;
import java.util.ResourceBundle;

public class NewCourseWindow implements Initializable {
    @FXML
    public TextField titleField;
    @FXML
    public DatePicker startDatePicker;
    @FXML
    public DatePicker endDatePicker;
    @FXML
    public TextArea descriptionField;
    @FXML
    public Button createButton;
    @FXML
    public Button updateButton;
    private int userId;
    private int courseId;

    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("CourseMngSys");
    CourseHibernateController courseHibernateController = new CourseHibernateController(entityManagerFactory);
    UserHibernateController userHibernateController = new UserHibernateController(entityManagerFactory);

    public void setCourseFormData(int id, boolean edit, int courseId) {
        this.userId = id;
        this.courseId = courseId;
        if(!edit) updateButton.setVisible(false);
        if (edit) {
            createButton.setVisible(false);
            Course course = courseHibernateController.getCourseById(courseId);
            titleField.setText(course.getTitle());
            descriptionField.setText(course.getDescription());
            startDatePicker.setValue(course.getStartDate().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate());
            endDatePicker.setValue(course.getEndDate().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate());
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void createCourse(ActionEvent actionEvent) throws IOException {
        User user = userHibernateController.getUserById(userId);
        Course course = new Course(titleField.getText(), descriptionField.getText(), Date.from(startDatePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()), Date.from(endDatePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        courseHibernateController.createCourse(course);
        course.getCourseModerator().add(user);
        user.getMyModeratedCourses().add(course);
        userHibernateController.editUser(user);
        returnToCourses();
    }

    public void returnToMain(ActionEvent actionEvent) throws IOException {
        returnToCourses();
    }
    public void returnToCourses() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(StartGui.class.getResource("main-course-window.fxml"));
        Parent root = fxmlLoader.load();

        MainCourseWindow mainProjectsWindow = fxmlLoader.getController();
        mainProjectsWindow.setCourseFormData(userId);

        Scene scene = new Scene(root);
        Stage stage = (Stage) titleField.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void updateCourse(ActionEvent actionEvent) throws IOException {
        Course course = courseHibernateController.getCourseById(courseId);
        course.setDescription(descriptionField.getText());
        course.setTitle(titleField.getText());
        course.setStartDate(Date.from(startDatePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        course.setEndDate(Date.from(endDatePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        courseHibernateController.editCourse(course);
        returnToCourses();
    }
}
