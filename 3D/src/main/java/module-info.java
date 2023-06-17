module com.example.abcdef {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.abcdef to javafx.fxml;
    exports com.example.abcdef;
}