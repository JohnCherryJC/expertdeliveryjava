package com.expert.orders.model;

import javafx.beans.property.*;
import java.time.LocalDate;

public class Order {
    private StringProperty id = new SimpleStringProperty();
    private ObjectProperty<Product> product = new SimpleObjectProperty<>();
    private StringProperty customerName = new SimpleStringProperty();
    private StringProperty customerPhone = new SimpleStringProperty();
    private StringProperty deliveryAddress = new SimpleStringProperty();
    private ObjectProperty<LocalDate> orderDate = new SimpleObjectProperty<>();
    private StringProperty status = new SimpleStringProperty();
    private BooleanProperty paid = new SimpleBooleanProperty();

    public Order() {}

    public Order(String id, Product product, String customerName, String customerPhone, String deliveryAddress,
                 LocalDate orderDate, String status, boolean paid) {
        this.id.set(id);
        this.product.set(product);
        this.customerName.set(customerName);
        this.customerPhone.set(customerPhone);
        this.deliveryAddress.set(deliveryAddress);
        this.orderDate.set(orderDate);
        this.status.set(status);
        this.paid.set(paid);
    }

    // ---- Getters / Setters ----
    public String getId() { return id.get(); }
    public void setId(String id) { this.id.set(id); }
    public StringProperty idProperty() { return id; }

    public Product getProduct() { return product.get(); }
    public void setProduct(Product p) { this.product.set(p); }
    public ObjectProperty<Product> productProperty() { return product; }

    public String getCustomerName() { return customerName.get(); }
    public void setCustomerName(String n) { this.customerName.set(n); }
    public StringProperty customerNameProperty() { return customerName; }

    public String getCustomerPhone() { return customerPhone.get(); }
    public void setCustomerPhone(String p) { this.customerPhone.set(p); }
    public StringProperty customerPhoneProperty() { return customerPhone; }

    public String getDeliveryAddress() { return deliveryAddress.get(); }
    public void setDeliveryAddress(String a) { this.deliveryAddress.set(a); }
    public StringProperty deliveryAddressProperty() { return deliveryAddress; }

    public LocalDate getOrderDate() { return orderDate.get(); }
    public void setOrderDate(LocalDate d) { this.orderDate.set(d); }
    public ObjectProperty<LocalDate> orderDateProperty() { return orderDate; }

    public String getStatus() { return status.get(); }
    public void setStatus(String s) { this.status.set(s); }
    public StringProperty statusProperty() { return status; }

    public boolean isPaid() { return paid.get(); }
    public void setPaid(boolean p) { this.paid.set(p); }
    public BooleanProperty paidProperty() { return paid; }

    public double getTotalPrice() {
        return product.get() == null ? 0 : product.get().getPrice();
    }

    // ---- NORMAL DISPLAY IN LISTS ----
    @Override
    public String toString() {
        return String.format(
                "%s | %s | %s",
                getId(),
                getCustomerName(),
                getProduct() != null ? getProduct().getName() : "Нет товара"
        );
    }
}
