module com.example.cab302_project {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.cab302_project to javafx.fxml;
    exports com.example.cab302_project;
}