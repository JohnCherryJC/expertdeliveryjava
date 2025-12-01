package com.expert.orders.model;

import java.time.LocalDateTime;
import java.util.List;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Delivery {
    private String courierName;
    private LocalDateTime dateTime;
    private List<Order> orders;

    private final StringProperty status = new SimpleStringProperty();
    private final SimpleObjectProperty<LocalDateTime> deliveryDateTime = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<LocalDateTime> plannedDeliveryDate = new SimpleObjectProperty<>();

    // Пустой конструктор
    public Delivery() {}

    // Основной конструктор с плановой датой
    public Delivery(String courierName, LocalDateTime dateTime, List<Order> orders, LocalDateTime plannedDate) {
        this.courierName = courierName;
        this.dateTime = dateTime;
        this.orders = orders;
        this.plannedDeliveryDate.set(plannedDate);
        updateStatus();

        // Добавляем слушатель изменения статуса первого заказа (если есть)
        if (orders != null && !orders.isEmpty()) {
            orders.get(0).statusProperty().addListener((obs, oldV, newV) -> updateStatus());
        }
    }

    private void updateStatus() {
        if (orders == null || orders.isEmpty()) {
            status.set("Нет заказов");
            deliveryDateTime.set(null);
        } else {
            String orderStatus = orders.get(0).getStatus();
            if ("Доставлен".equals(orderStatus)) {
                status.set("Доставка выполнена");
                deliveryDateTime.set(LocalDateTime.now());
            } else {
                status.set("В процессе доставки");
                deliveryDateTime.set(null);
            }
        }
    }

    // --- Getters и Property для биндинга ---
    public StringProperty statusProperty() { return status; }
    public String getStatus() { return status.get(); }

    public SimpleObjectProperty<LocalDateTime> deliveryDateTimeProperty() { return deliveryDateTime; }
    public LocalDateTime getDeliveryDateTime() { return deliveryDateTime.get(); }

    public SimpleObjectProperty<LocalDateTime> plannedDeliveryDateProperty() { return plannedDeliveryDate; }
    public LocalDateTime getPlannedDeliveryDate() { return plannedDeliveryDate.get(); }
    public void setPlannedDeliveryDate(LocalDateTime dt) { this.plannedDeliveryDate.set(dt); }

    public String getCourierName() { return courierName; }
    public void setCourierName(String c) { this.courierName = c; }

    public LocalDateTime getDateTime() { return dateTime; }
    public void setDateTime(LocalDateTime d) { this.dateTime = d; }

    public List<Order> getOrders() { return orders; }
    public void setOrders(List<Order> o) { this.orders = o; updateStatus(); }

    public String getOrderId() { return orders == null || orders.isEmpty() ? "" : orders.get(0).getId(); }
    public String getProductName() { return orders == null || orders.isEmpty() ? "" : orders.get(0).getProduct().getName(); }
}
