module com.example.kursinis_java {
    requires javafx.controls;
    requires javafx.fxml;
    requires mysql.connector.java;
    requires java.persistence;
    requires org.hibernate.orm.core;
    requires java.sql;

    opens com.example.kursinis_java to javafx.fxml;
    exports com.example.kursinis_java;

    opens com.example.kursinis_java.ds to javafx.fxml, org.hibernate.orm.core, java.persistence;
    exports com.example.kursinis_java.ds;
    exports com.example.kursinis_java.fxControllers to javafx.fxml;
    opens com.example.kursinis_java.fxControllers to javafx.fxml;
}