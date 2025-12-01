module com.expert.orders {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    opens com.expert.orders to javafx.fxml, com.google.gson;
    opens com.expert.orders.controllers to javafx.fxml;
    opens com.expert.orders.model to com.google.gson, javafx.fxml;
    exports com.expert.orders;
    exports com.expert.orders.controllers;
}
