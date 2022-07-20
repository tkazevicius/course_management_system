package com.example.kursinis_java.fxControllers;

import com.example.kursinis_java.StartGui;
import com.example.kursinis_java.ds.Company;
import com.example.kursinis_java.ds.Person;
import com.example.kursinis_java.ds.User;
import com.example.kursinis_java.ds.UserType;
import com.example.kursinis_java.hibernateControllers.UserHibernateController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class UpdateUserWindow implements Initializable {
    @FXML
    public TextField loginField;
    @FXML
    public TextField personNameField;
    @FXML
    public TextField personSurnameField;
    @FXML
    public TextField personCardNumberField;
    @FXML
    public TextField personStudentNumberField;
    @FXML
    public Text personNameText;
    @FXML
    public Text personSurnameText;
    @FXML
    public Text personCardNumberText;
    @FXML
    public Text personStudentNumberText;
    @FXML
    public PasswordField passwordField;
    @FXML
    public TextField companyNameField;
    @FXML
    public Text companyNameText;
    @FXML
    public TextField companyRepresentativeField;
    @FXML
    public Text companyRepresentativeText;
    @FXML
    public TextField companyAddressField;
    @FXML
    public Text companyAddressText;
    @FXML
    public TextField companyPhoneNumberField;
    @FXML
    public Text companyPhoneNumberText;
    private int userId;
    private User user;

    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("CourseMngSys");
    UserHibernateController userHibernateController = new UserHibernateController(entityManagerFactory);

    public void setUpdateData(int userId, User user){
        this.userId = userId;
        this.user = user;
        Person person = userHibernateController.getPersonById(user.getId());
        Company company = userHibernateController.getCompanyById(user.getId());
        loginField.setText(user.getLogin());
        passwordField.setText(user.getPassword());
        if(user.getUserType() == UserType.STUDENT){
            personNameField.setText(person.getName());
            personSurnameField.setText(person.getSurname());
            personCardNumberField.setText(person.getCardNumber());
            personStudentNumberField.setText(person.getStudentNumber());
            companyNameText.setVisible(false);
            companyNameField.setVisible(false);
            companyRepresentativeText.setVisible(false);
            companyRepresentativeField.setVisible(false);
            companyAddressText.setVisible(false);
            companyAddressField.setVisible(false);
            companyPhoneNumberText.setVisible(false);
            companyPhoneNumberField.setVisible(false);
        } else if(user.getUserType() == UserType.COMPANY){
            companyNameField.setText(company.getCompanyName());
            companyRepresentativeField.setText(company.getCompanyRepresentative());
            companyAddressField.setText(company.getCompanyAddress());
            companyPhoneNumberField.setText(company.getCompanyPhone());
            personNameText.setVisible(false);
            personNameField.setVisible(false);
            personSurnameField.setVisible(false);
            personSurnameText.setVisible(false);
            personCardNumberField.setVisible(false);
            personCardNumberText.setVisible(false);
            personStudentNumberField.setVisible(false);
            personStudentNumberText.setVisible(false);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void updateUser(ActionEvent actionEvent) throws IOException {
        Person person = userHibernateController.getPersonById(user.getId());
        Company company = userHibernateController.getCompanyById(user.getId());
        if(user.getUserType() == UserType.STUDENT){
            person.setLogin(loginField.getText());
            person.setPassword(passwordField.getText());
            person.setName(personNameField.getText());
            person.setSurname(personSurnameField.getText());
            person.setCardNumber(personCardNumberField.getText());
            person.setStudentNumber(personStudentNumberField.getText());
            userHibernateController.editUser(person);
        } else if(user.getUserType() == UserType.COMPANY){
            company.setLogin(loginField.getText());
            company.setPassword(passwordField.getText());
            company.setCompanyName(companyNameField.getText());
            company.setCompanyRepresentative(companyRepresentativeField.getText());
            company.setCompanyAddress(companyAddressField.getText());
            company.setCompanyPhone(companyPhoneNumberField.getText());
            userHibernateController.editUser(company);
        }
        FXMLLoader fxmlLoader = new FXMLLoader(StartGui.class.getResource("all-users-window.fxml"));
        Parent root = fxmlLoader.load();

        AllUsersWindow allUsersWindow = fxmlLoader.getController();
        allUsersWindow.setAllUsersWindow(userId);

        Scene scene = new Scene(root);
        Stage stage = (Stage) loginField.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void returnToMain(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(StartGui.class.getResource("all-users-window.fxml"));
        Parent root = fxmlLoader.load();

        AllUsersWindow allUsersWindow = fxmlLoader.getController();
        allUsersWindow.setAllUsersWindow(userId);

        Scene scene = new Scene(root);
        Stage stage = (Stage) loginField.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}
