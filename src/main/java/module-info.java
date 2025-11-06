module org.example.employee {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens org.example.employee to javafx.fxml;
    exports org.example.employee;
}