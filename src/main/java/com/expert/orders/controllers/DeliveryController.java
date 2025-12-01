package com.expert.orders.controllers;

import com.expert.orders.model.DataManager;
import com.expert.orders.model.Delivery;
import com.expert.orders.model.DeliveryStore;
import com.expert.orders.model.Order;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.time.LocalDateTime;
import java.util.List;

public class DeliveryController {
    @FXML private ListView<Order> deliveryListView;
    @FXML private TextField courierField;
    @FXML private DatePicker datePicker;
    @FXML private Label deliveriesCount;

    @FXML
    public void initialize() {
        deliveryListView.setItems(DataManager.orders);
        updateStats();
    }

    @FXML
    public void createDelivery() {
        if (courierField.getText().trim().isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Введите имя курьера").showAndWait();
            return;
        }

        Order sel = deliveryListView.getSelectionModel().getSelectedItem();
        if (sel == null) {
            new Alert(Alert.AlertType.WARNING, "Выберите заказ для доставки").showAndWait();
            return;
        }

        if (datePicker.getValue() == null) {
            new Alert(Alert.AlertType.WARNING, "Выберите плановую дату доставки").showAndWait();
            return;
        }

        // Установим статус "В процессе" у заказа
        sel.setStatus("Доставка в процессе");

        // Получаем дату из DatePicker и преобразуем в LocalDateTime
        LocalDateTime plannedDate = datePicker.getValue().atStartOfDay();

        // Создаём объект Delivery с плановой датой
        Delivery d = new Delivery(courierField.getText().trim(),
                LocalDateTime.now(),
                List.of(sel),
                plannedDate);

        DeliveryStore.addDelivery(d);

        updateStats();
        new Alert(Alert.AlertType.INFORMATION, "Доставка создана").showAndWait();
    }

    private void updateStats() {
        deliveriesCount.setText(String.valueOf(DeliveryStore.getDeliveries().size()));
    }
}
