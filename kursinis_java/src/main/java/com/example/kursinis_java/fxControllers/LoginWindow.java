package com.example.kursinis_java.fxControllers;

import com.example.kursinis_java.StartGui;
import com.example.kursinis_java.ds.User;
import com.example.kursinis_java.hibernateControllers.UserHibernateController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static com.example.kursinis_java.utils.JavaFxUtils.alertMessage;

public class LoginWindow implements Initializable {
    @FXML
    public PasswordField passwordField;
    @FXML
    public TextField loginField;

    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("CourseMngSys");
    UserHibernateController userHibernateController = new UserHibernateController(entityManagerFactory);

    public void validate(ActionEvent actionEvent) throws IOException {
        User user = userHibernateController.getUserByLoginData(loginField.getText(), passwordField.getText());
        if(user != null){
            FXMLLoader fxmlLoader = new FXMLLoader(StartGui.class.getResource("main-course-window.fxml"));
            Parent root = fxmlLoader.load();

            MainCourseWindow mainCourseWindow = fxmlLoader.getController();
            mainCourseWindow.setCourseFormData(user.getId());

            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("Course Management System");
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } else {
            alertMessage("User does not exist");
        }
    }

    public void openNewUserForm(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(StartGui.class.getResource("sign-up-window.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);

        Stage stage = (Stage) loginField.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
