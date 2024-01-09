module com.example.taxireservation {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires java.sql;

    opens com.example.taxireservation to javafx.fxml;
    exports com.example.taxireservation;
}