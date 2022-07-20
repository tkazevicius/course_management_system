package com.example.kursinis_java.fxControllers;

import com.example.kursinis_java.StartGui;
import com.example.kursinis_java.ds.Company;
import com.example.kursinis_java.ds.Person;
import com.example.kursinis_java.hibernateControllers.UserHibernateController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import static com.example.kursinis_java.utils.JavaFxUtils.alertMessage;

public class AdminCreateUserWindow implements Initializable {
    @FXML
    public TextField loginField;
    @FXML
    public PasswordField passwordField;
    @FXML
    public PasswordField repeatPasswordField;
    @FXML
    public RadioButton radioPerson;
    @FXML
    public RadioButton radioCompany;
    @FXML
    public TextField personNameField;
    @FXML
    public TextField personSurnameField;
    @FXML
    public TextField personStudentNumberField;
    @FXML
    public TextField personStudentCardNumberField;
    @FXML
    public TextField companyNameField;
    @FXML
    public TextField companyRepresentativeField;
    @FXML
    public TextField companyAddressField;
    @FXML
    public TextField companyPhoneNumberField;
    private int userId;

    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("CourseMngSys");
    UserHibernateController userHibernateController = new UserHibernateController(entityManagerFactory);

    public void setSignUpWindow(int userId) {
        this.userId = userId;
    }

    public void createUser(ActionEvent actionEvent) throws SQLException, IOException {
        if(radioPerson.isSelected()){
            Person person = new Person(loginField.getText(), passwordField.getText(), personNameField.getText(), personSurnameField.getText(), personStudentCardNumberField.getText(), personStudentCardNumberField.getText());
            userHibernateController.createUser(person);
        } else {
            Company company = new Company(loginField.getText(), passwordField.getText(), companyNameField.getText(), companyRepresentativeField.getText(), companyAddressField.getText(), companyPhoneNumberField.getText());
            userHibernateController.createUser(company);
        }
        alertMessage("User created successfully");
        returnToPrevious();
    }

    public void returnToLogin(ActionEvent actionEvent) throws IOException {
        returnToPrevious();
    }
    private void returnToPrevious() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(StartGui.class.getResource("all-users-window.fxml"));
        Parent root = fxmlLoader.load();

        AllUsersWindow allUsersWindow = fxmlLoader.getController();
        allUsersWindow.setAllUsersWindow(userId);

        Scene scene = new Scene(root);
        Stage stage = (Stage) loginField.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        personNameField.setDisable(true);
        personStudentCardNumberField.setDisable(true);
        personStudentNumberField.setDisable(true);
        personSurnameField.setDisable(true);
        companyAddressField.setDisable(true);
        companyNameField.setDisable(true);
        companyPhoneNumberField.setDisable(true);
        companyRepresentativeField.setDisable(true);
    }

    public void enableFields(ActionEvent actionEvent) {
        if(radioPerson.isSelected()){
            personNameField.setDisable(false);
            personStudentNumberField.setDisable(false);
            personSurnameField.setDisable(false);
            personStudentCardNumberField.setDisable(false);
            companyAddressField.setDisable(true);
            companyNameField.setDisable(true);
            companyPhoneNumberField.setDisable(true);
            companyRepresentativeField.setDisable(true);
        }else{
            personNameField.setDisable(true);
            personStudentNumberField.setDisable(true);
            personSurnameField.setDisable(true);
            personStudentCardNumberField.setDisable(true);
            companyAddressField.setDisable(false);
            companyNameField.setDisable(false);
            companyPhoneNumberField.setDisable(false);
            companyRepresentativeField.setDisable(false);
        }
    }
}
