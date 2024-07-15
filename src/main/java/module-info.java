module com.example.stafflogin {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;


    opens com.example.stafflogin to javafx.fxml;
    exports com.example.stafflogin;
}