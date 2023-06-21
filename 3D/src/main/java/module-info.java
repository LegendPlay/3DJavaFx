module com.javafx {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;

    opens com.javafx to javafx.fxml;
    exports com.javafx;
}