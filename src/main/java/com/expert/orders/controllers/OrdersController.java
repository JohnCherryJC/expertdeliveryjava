package com.expert.orders.controllers;

import com.expert.orders.model.DataManager;
import com.expert.orders.model.DeliveryStore;
import com.expert.orders.model.Order;
import com.expert.orders.storage.FileUtil;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.Optional;

public class OrdersController {
    @FXML private TableView<Order> ordersTable;
    @FXML private TableColumn<Order, String> colId;
    @FXML private TableColumn<Order, String> colProduct;
    @FXML private TableColumn<Order, String> colCustomer;
    @FXML private TableColumn<Order, String> colPhone;
    @FXML private TableColumn<Order, String> colDate;
    @FXML private TableColumn<Order, String> colStatus;
    @FXML private TableColumn<Order, Boolean> colPaid;
    @FXML private TableColumn<Order, String> colPrice; // новый столбец "Цена"
    @FXML private TextField searchField;
    @FXML private ComboBox<String> statusFilter;
    @FXML private Label totalLabel;

    private FilteredList<Order> filtered;

    @FXML
    public void initialize() {
        // Привязка столбцов
        colId.setCellValueFactory(c -> c.getValue().idProperty());
        colProduct.setCellValueFactory(c -> new SimpleStringProperty(
                c.getValue().getProduct() == null ? "" : c.getValue().getProduct().getName()));
        colCustomer.setCellValueFactory(c -> c.getValue().customerNameProperty());
        colPhone.setCellValueFactory(c -> c.getValue().customerPhoneProperty());
        colDate.setCellValueFactory(c -> new SimpleStringProperty(
                c.getValue().getOrderDate() == null ? "" : c.getValue().getOrderDate().toString()));
        colStatus.setCellValueFactory(c -> c.getValue().statusProperty());

        // Столбец "Статус оплаты"
        colPaid.setCellValueFactory(c -> c.getValue().paidProperty());
        colPaid.setCellFactory(tc -> new TableCell<Order, Boolean>() {
            @Override
            protected void updateItem(Boolean paid, boolean empty) {
                super.updateItem(paid, empty);
                if (empty || paid == null) setText(null);
                else setText(paid ? "Оплачен" : "Не оплачен");
            }
        });

        // Столбец "Цена"
        colPrice.setCellValueFactory(c -> new SimpleStringProperty(
                c.getValue().getProduct() == null ? "" : String.format("%.2f", c.getValue().getTotalPrice())
        ));

        // Фильтрация
        filtered = new FilteredList<>(DataManager.orders, p -> true);
        ordersTable.setItems(filtered);

        searchField.textProperty().addListener((obs, oldV, newV) -> {
            String s = newV == null ? "" : newV.toLowerCase();
            filtered.setPredicate(order -> order.getCustomerName().toLowerCase().contains(s) ||
                    order.getDeliveryAddress().toLowerCase().contains(s));
            updateStats();
        });

        statusFilter.getItems().addAll("Все","В обработке","В сборке","Доставлен");
        statusFilter.getSelectionModel().selectFirst();
        statusFilter.valueProperty().addListener((obs, ov, nv) -> {
            if (nv == null || nv.equals("Все")) filtered.setPredicate(o -> true);
            else filtered.setPredicate(o -> o.getStatus() != null && o.getStatus().equals(nv));
            updateStats();
        });

        ordersTable.setOnMouseClicked(ev -> {
            if (ev.getClickCount() == 2) onEdit();
        });

        ordersTable.setOnKeyPressed(ev -> {
            if (ev.getCode() == KeyCode.DELETE) onDelete();
        });

        DataManager.orders.forEach(order -> order.statusProperty().addListener((obs, oldV, newV) -> {
            DeliveryStore.getDeliveries().forEach(d -> {
                if (!d.getOrders().isEmpty() && d.getOrders().get(0).getId().equals(order.getId())) {
                    ordersTable.refresh();
                }
            });
        }));

        updateStats();
    }

    @FXML
    public void onNew() { openForm(null); }

    @FXML
    public void onEdit() {
        Order sel = ordersTable.getSelectionModel().getSelectedItem();
        if (sel == null) { new Alert(Alert.AlertType.WARNING, "Выберите заказ").showAndWait(); return; }
        openForm(sel);
        ordersTable.refresh();
    }

    @FXML
    public void onDelete() {
        Order sel = ordersTable.getSelectionModel().getSelectedItem();
        if (sel == null) { new Alert(Alert.AlertType.WARNING, "Выберите заказ").showAndWait(); return; }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Вы уверены, что хотите удалить заказ?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> res = confirm.showAndWait();
        if (res.isPresent() && res.get() == ButtonType.YES) {
            DataManager.removeOrder(sel);
            updateStats();
        }
    }

    @FXML
    public void onSaveToFile() {
        FileUtil.saveOrders(DataManager.orders);
        new Alert(Alert.AlertType.INFORMATION, "Сохранено в orders.json").showAndWait();
    }

    @FXML
    public void onLoadFromFile() {
        var loaded = FileUtil.loadOrders();
        DataManager.orders.clear();
        DataManager.orders.addAll(loaded);
        ordersTable.refresh();
        updateStats();
    }

    private void openForm(Order order) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/order_form.fxml"));
            Parent root = loader.load();
            OrderFormController ctrl = loader.getController();
            ctrl.initData(order); // в форме обязательно добавить paidCheckBox
            Stage st = new Stage();
            st.initModality(Modality.APPLICATION_MODAL);
            st.setScene(new Scene(root));
            st.setTitle(order == null ? "Новый заказ" : "Редактировать заказ");
            st.showAndWait();
            ordersTable.refresh();
            updateStats();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateStats() {
        totalLabel.setText(String.valueOf(DataManager.orders.size()));
    }
}
