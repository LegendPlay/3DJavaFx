module com.javafx {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires java.sql;

    opens com.javafx to javafx.fxml;
    exports com.javafx;
}