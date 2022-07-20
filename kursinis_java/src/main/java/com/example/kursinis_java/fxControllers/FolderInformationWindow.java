package com.example.kursinis_java.fxControllers;

import com.example.kursinis_java.StartGui;
import com.example.kursinis_java.ds.Folder;
import com.example.kursinis_java.hibernateControllers.FolderHibernateController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class FolderInformationWindow implements Initializable {
    @FXML
    public Text folderNameTextField;
    @FXML
    public Text folderNumberTextField;
    @FXML
    public Text parentFolderNameText;
    private int parentFolderId;
    private int userId;
    private int currentFolderId;
    private int subFoldersCount;

    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("CourseMngSys");
    FolderHibernateController folderHibernateController = new FolderHibernateController(entityManagerFactory);

    public void setData(int parentFolderId, int userId, int currentFolderId){
        this.parentFolderId = parentFolderId;
        this.userId = userId;
        this.currentFolderId = currentFolderId;
        folderNameTextField.setText(folderHibernateController.getFolderById(currentFolderId).getTitle());
        Folder folder = folderHibernateController.getFolderById(currentFolderId);
        subFoldersCount = 0;
        for(Folder f : folder.getSubFolders()){
            subFoldersCount++;
        }
        folderNumberTextField.setText(String.valueOf(subFoldersCount));
        if(parentFolderId != 0) parentFolderNameText.setText(folderHibernateController.getFolderById(parentFolderId).toString());
        else parentFolderNameText.setText("None");
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void returnToMain(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(StartGui.class.getResource("main-course-window.fxml"));
        Parent root = fxmlLoader.load();

        MainCourseWindow mainProjectsWindow = fxmlLoader.getController();
        mainProjectsWindow.setCourseFormData(userId);

        Scene scene = new Scene(root);
        Stage stage = (Stage) folderNameTextField.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}
