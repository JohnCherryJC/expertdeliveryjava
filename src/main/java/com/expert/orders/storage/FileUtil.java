package com.expert.orders.storage;

import com.expert.orders.model.Order;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.lang.reflect.Type;
import java.util.List;

public class FileUtil {
    private static final String FILE_PATH = "orders.json";

    private static Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(Order.class, new OrderAdapter())
            .create();

    public static void saveOrders(ObservableList<Order> orders) {
        try (Writer writer = new FileWriter(FILE_PATH)) {
            gson.toJson(orders, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ObservableList<Order> loadOrders() {
        try (Reader reader = new FileReader(FILE_PATH)) {
            Type listType = new TypeToken<List<Order>>() {}.getType();
            List<Order> list = gson.fromJson(reader, listType);
            if (list == null) return FXCollections.observableArrayList();
            return FXCollections.observableArrayList(list);
        } catch (FileNotFoundException e) {
            // Файл не найден — возвращаем пустой список
            return FXCollections.observableArrayList();
        } catch (IOException e) {
            e.printStackTrace();
            return FXCollections.observableArrayList();
        }
    }
}
