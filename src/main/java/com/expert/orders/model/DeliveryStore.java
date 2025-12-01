package com.expert.orders.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DeliveryStore {
    private static final ObservableList<Delivery> deliveries = FXCollections.observableArrayList();

    public static void addDelivery(Delivery d) {
        deliveries.add(d);
    }

    public static ObservableList<Delivery> getDeliveries() {
        return deliveries;
    }
}
