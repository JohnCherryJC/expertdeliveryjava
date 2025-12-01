package com.expert.orders.controllers;

import com.expert.orders.model.DataManager;
import com.expert.orders.model.Order;
import com.expert.orders.model.Product;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDate;

public class OrderFormController {
    @FXML private TextField customerName;
    @FXML private TextField customerPhone;
    @FXML private TextField address;
    @FXML private ComboBox<Product> productBox;
    @FXML private ComboBox<String> statusBox;
    @FXML private CheckBox paidBox;

    private Order editing;

    @FXML
    public void initialize() {
        productBox.setItems(DataManager.products);
        statusBox.getItems().addAll("В обработке","В сборке","Доставлен");
        statusBox.getSelectionModel().selectFirst();
    }

    public void initData(Order order) {
        this.editing = order;
        if (order != null) {
            customerName.setText(order.getCustomerName());
            customerPhone.setText(order.getCustomerPhone());
            address.setText(order.getDeliveryAddress());
            productBox.getSelectionModel().select(order.getProduct());
            statusBox.getSelectionModel().select(order.getStatus());
            paidBox.setSelected(order.isPaid());
        }
    }

    @FXML
    public void onSave() {
        if (customerName.getText().trim().isEmpty() || address.getText().trim().isEmpty() || productBox.getValue() == null) {
            new Alert(Alert.AlertType.WARNING, "Заполните обязательные поля: клиент, адрес, товар").showAndWait();
            return;
        }

        if (editing == null) {
            Order o = new Order();
            o.setId(String.valueOf(System.currentTimeMillis())); // только числа
            o.setCustomerName(customerName.getText().trim());
            o.setCustomerPhone(customerPhone.getText().trim());
            o.setDeliveryAddress(address.getText().trim());
            o.setProduct(productBox.getValue());
            o.setOrderDate(LocalDate.now());
            o.setStatus(statusBox.getValue());
            o.setPaid(paidBox.isSelected());
            DataManager.addOrder(o);
        } else {
            editing.setCustomerName(customerName.getText().trim());
            editing.setCustomerPhone(customerPhone.getText().trim());
            editing.setDeliveryAddress(address.getText().trim());
            editing.setProduct(productBox.getValue());
            editing.setStatus(statusBox.getValue());
            editing.setPaid(paidBox.isSelected());
        }

        Stage st = (Stage) customerName.getScene().getWindow();
        st.close();
    }

    @FXML
    public void onCancel() {
        Stage st = (Stage) customerName.getScene().getWindow();
        st.close();
    }
}
