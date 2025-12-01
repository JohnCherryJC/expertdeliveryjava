package com.expert.orders.controllers;

import com.expert.orders.model.Delivery;

import java.util.ArrayList;
import java.util.List;

public class DeliveryControllerDeliveriesStore {
    private static final List<Delivery> deliveries = new ArrayList<>();

    public static List<Delivery> getDeliveries() { return deliveries; }
    public static void addDelivery(Delivery d) { deliveries.add(d); }
}
