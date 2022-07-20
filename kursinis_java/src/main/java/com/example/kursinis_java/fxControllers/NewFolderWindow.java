package com.example.kursinis_java.fxControllers;

import com.example.kursinis_java.StartGui;
import com.example.kursinis_java.ds.Course;
import com.example.kursinis_java.ds.Folder;
import com.example.kursinis_java.ds.User;
import com.example.kursinis_java.hibernateControllers.CourseHibernateController;
import com.example.kursinis_java.hibernateControllers.FolderHibernateController;
import com.example.kursinis_java.hibernateControllers.UserHibernateController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class NewFolderWindow implements Initializable {
    @FXML
    public TextField folderTitleField;
    @FXML
    public ComboBox responsiblePerson;
    @FXML
    public Button createFolderButton;
    @FXML
    public Button updateFolderButton;
    @FXML
    public Text responsibleTextField;
    private int courseId;
    private int parentFolderId;
    private int userId;
    private int currentFolderId;

    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("CourseMngSys");
    CourseHibernateController courseHibernateController = new CourseHibernateController(entityManagerFactory);
    UserHibernateController userHibernateController = new UserHibernateController(entityManagerFactory);
    FolderHibernateController folderHibernateController = new FolderHibernateController(entityManagerFactory);

    public void setData(boolean edit, int parentFolderId, int courseId, int userId, int currentFolderId){
        this.courseId = courseId;
        this.parentFolderId = parentFolderId;
        this.userId = userId;
        this.currentFolderId = currentFolderId;
        if(edit){
            createFolderButton.setVisible(false);
            responsiblePerson.setVisible(true);
            responsibleTextField.setVisible(true);
            loadFormData();
        }
        if(!edit) updateFolderButton.setVisible(false);
    }
    private void loadFormData(){
        Folder folder = folderHibernateController.getFolderById(currentFolderId);
        folderTitleField.setText(folder.getTitle());
        responsiblePerson.getSelectionModel().select(folder.getResponsible());
        folderHibernateController.edit(folder);
    }

    public void createFolder(ActionEvent actionEvent) throws IOException {
        if(parentFolderId != 0){
            Folder parentFolder = folderHibernateController.getFolderById(parentFolderId);
            Folder folder = new Folder(folderTitleField.getText(), parentFolder, userHibernateController.getUserById(Integer.parseInt(responsiblePerson.getValue().toString().split(":")[0])), userHibernateController.getUserById(userId));
            parentFolder.getSubFolders().add(folder);
            folderHibernateController.edit(parentFolder);
        } else if(courseId != 0){
            Course course = courseHibernateController.getCourseById(courseId);
            Folder folder = new Folder(folderTitleField.getText(), userHibernateController.getUserById(Integer.parseInt(responsiblePerson.getValue().toString().split(":")[0])), userHibernateController.getUserById(userId), course);
            course.getCourseFolders().add(folder);
            courseHibernateController.editCourse(course);
        }
        returnToPrevious();
    }
    public void returnToPrevious() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(StartGui.class.getResource("main-course-window.fxml"));
        Parent root = fxmlLoader.load();

        MainCourseWindow mainProjectsWindow = fxmlLoader.getController();
        mainProjectsWindow.setCourseFormData(userId);

        Scene scene = new Scene(root);
        Stage stage = (Stage) folderTitleField.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    public void returnToMain(ActionEvent actionEvent) throws IOException {
        returnToPrevious();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<User> users = userHibernateController.getAllUsers(true, -1, -1);
        users.forEach(u -> responsiblePerson.getItems().add(u.getId() + ":" + u.getLogin()));
    }

    public void updateFolder(ActionEvent actionEvent) throws IOException {
        Folder folder = folderHibernateController.getFolderById(currentFolderId);
        folder.setTitle(folderTitleField.getText());
        folder.setResponsible(userHibernateController.getUserById(Integer.parseInt(responsiblePerson.getValue().toString().split(":")[0])));
        folderHibernateController.edit(folder);
        returnToPrevious();
    }
}
