package com.expert.orders.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;

public class MainController {
    @FXML
    public void onAbout() {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle("О программе");
        a.setHeaderText("Expert — Система заказов и доставки");
        a.setContentText("Версия 1.0\nРазработано для итогового проекта.");
        a.showAndWait();
    }
}
