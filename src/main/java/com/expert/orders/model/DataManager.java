package com.expert.orders.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;

public class DataManager {
    public static final ObservableList<Order> orders = FXCollections.observableArrayList();
    public static final ObservableList<Product> products = FXCollections.observableArrayList();

    static {
        products.add(new Product("Телевизор Samsung 55\"", "Телевизоры", 54999));
        products.add(new Product("Стиральная машина Bosch", "Стиральные машины", 39999));
        products.add(new Product("Холодильник LG", "Холодильники", 45999));

    }

    public static void addOrder(Order o) { orders.add(o); }
    public static void removeOrder(Order o) { orders.remove(o); }
}
