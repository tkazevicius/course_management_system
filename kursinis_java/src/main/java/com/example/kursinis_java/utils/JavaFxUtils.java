package com.example.kursinis_java.utils;

import javafx.scene.control.Alert;
import javafx.stage.Modality;

public class JavaFxUtils {
    public static void alertMessage(String s) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText("Message text:");
        alert.setContentText(s);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.showAndWait();
    }
}
