package com.expert.orders.controllers;

import com.expert.orders.model.Delivery;
import com.expert.orders.model.DeliveryStore;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.time.format.DateTimeFormatter;

public class DeliveryStatusController {

    @FXML private TableView<Delivery> statusTable;
    @FXML private TableColumn<Delivery, String> colId;
    @FXML private TableColumn<Delivery, String> colProduct;
    @FXML private TableColumn<Delivery, String> colCourier;
    @FXML private TableColumn<Delivery, String> colDateCreated;
    @FXML private TableColumn<Delivery, String> colStatus;
    @FXML private TableColumn<Delivery, String> colDateDelivered;
    @FXML private TableColumn<Delivery, String> colPlannedDeliveryDate; // новый столбец

    @FXML
    public void initialize() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        colId.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getOrderId()));
        colProduct.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getProductName()));
        colCourier.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getCourierName()));
        colDateCreated.setCellValueFactory(c -> new SimpleStringProperty(
                c.getValue().getDateTime() == null ? "" : c.getValue().getDateTime().format(dtf)
        ));
        colStatus.setCellValueFactory(c -> c.getValue().statusProperty());

        colDateDelivered.setCellValueFactory(c ->
                Bindings.createStringBinding(
                        () -> {
                            var dt = c.getValue().getDeliveryDateTime();
                            return dt == null ? "" : dt.format(dtf);
                        },
                        c.getValue().deliveryDateTimeProperty()
                )
        );

        // Привязка нового столбца к plannedDeliveryDate
        colPlannedDeliveryDate.setCellValueFactory(c ->
                Bindings.createStringBinding(
                        () -> {
                            var dt = c.getValue().getPlannedDeliveryDate();
                            return dt == null ? "" : dt.format(dtf);
                        },
                        c.getValue().plannedDeliveryDateProperty()
                )
        );

        statusTable.setItems(DeliveryStore.getDeliveries());
    }
}
