package com.example.kursinis_java.fxControllers;

import com.example.kursinis_java.StartGui;
import com.example.kursinis_java.ds.User;
import com.example.kursinis_java.ds.UserType;
import com.example.kursinis_java.hibernateControllers.UserHibernateController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import static com.example.kursinis_java.utils.JavaFxUtils.alertMessage;

public class AllUsersWindow implements Initializable {
    @FXML
    public ListView allUsersList;
    @FXML
    public Button backButton;
    private int userId;

    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("CourseMngSys");
    UserHibernateController userHibernateController = new UserHibernateController(entityManagerFactory);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void returnToMain(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(StartGui.class.getResource("main-course-window.fxml"));
        Parent root = fxmlLoader.load();

        MainCourseWindow mainProjectsWindow = fxmlLoader.getController();
        mainProjectsWindow.setCourseFormData(userId);

        Scene scene = new Scene(root);
        Stage stage = (Stage) allUsersList.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void createUser(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(StartGui.class.getResource("admin-create-user-window.fxml"));
        Parent root = fxmlLoader.load();

        AdminCreateUserWindow adminCreateUserWindow = fxmlLoader.getController();
        adminCreateUserWindow.setSignUpWindow(userId);

        Scene scene = new Scene(root);
        Stage stage = (Stage) allUsersList.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void updateUser(ActionEvent actionEvent) throws IOException {
        User user = userHibernateController.getUserById(Integer.parseInt(allUsersList.getSelectionModel().getSelectedItem().toString().split(":")[0]));
        if(user.getUserType() != UserType.MODERATOR) {
            FXMLLoader fxmlLoader = new FXMLLoader(StartGui.class.getResource("update-user-window.fxml"));
            Parent root = fxmlLoader.load();

            UpdateUserWindow updateUserWindow = fxmlLoader.getController();
            updateUserWindow.setUpdateData(userId, user);

            Scene scene = new Scene(root);
            Stage stage = (Stage) allUsersList.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } else{
            alertMessage("Modifying moderator accounts is not allowed");
        }
    }

    public void deleteUser(ActionEvent actionEvent) {
        User user = userHibernateController.getUserById(Integer.parseInt(allUsersList.getSelectionModel().getSelectedItem().toString().split(":")[0]));
        if(user.getUserType() != UserType.MODERATOR) userHibernateController.removeUser(user.getId());
        else alertMessage("Modifying moderator accounts is not allowed");
        fillTables();
    }

    public void setAllUsersWindow(int userId) {
        this.userId = userId;
        fillTables();
    }

    private void fillTables(){
        allUsersList.getItems().clear();
        List<User> usersFromDb = userHibernateController.getAllUsers(true, -1, -1);
        for(User u : usersFromDb){
            allUsersList.getItems().add(u.getId() + ":" + u.getLogin());
        }
    }
}
