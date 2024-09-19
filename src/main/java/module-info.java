module com.example.cab302prac4 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;
    requires org.junit.jupiter.api;


    opens com.example.cab302prac4 to javafx.fxml;
    exports com.example.cab302prac4;
    exports com.example.cab302prac4.controller;
    opens com.example.cab302prac4.controller to javafx.fxml;
    exports com.example.cab302prac4.model;
    opens com.example.cab302prac4.model to javafx.fxml;
    exports Tests;
    opens Tests to javafx.fxml;
}