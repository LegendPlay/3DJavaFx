module com.javafx {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;

    opens com.flight_sim to javafx.fxml;
    exports com.flight_sim;
}