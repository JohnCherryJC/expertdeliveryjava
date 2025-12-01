package com.expert.orders.storage;

import com.expert.orders.model.Order;
import com.expert.orders.model.Product;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.LocalDate;

public class OrderAdapter implements JsonSerializer<Order>, JsonDeserializer<Order> {

    @Override
    public JsonElement serialize(Order order, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject obj = new JsonObject();
        obj.addProperty("id", order.getId());
        obj.addProperty("customerName", order.getCustomerName());
        obj.addProperty("customerPhone", order.getCustomerPhone());
        obj.addProperty("deliveryAddress", order.getDeliveryAddress());
        obj.addProperty("orderDate", order.getOrderDate() == null ? "" : order.getOrderDate().toString());
        obj.addProperty("status", order.getStatus());
        obj.addProperty("paid", order.isPaid());

        // Сохраняем Product
        if (order.getProduct() != null) {
            JsonObject prod = new JsonObject();
            prod.addProperty("name", order.getProduct().getName());
            prod.addProperty("category", order.getProduct().getCategory());
            prod.addProperty("price", order.getProduct().getPrice());
            obj.add("product", prod);
        } else {
            obj.add("product", JsonNull.INSTANCE);
        }

        return obj;
    }

    @Override
    public Order deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject obj = json.getAsJsonObject();
        Order order = new Order();
        order.setId(obj.get("id").getAsString());
        order.setCustomerName(obj.get("customerName").getAsString());
        order.setCustomerPhone(obj.get("customerPhone").getAsString());
        order.setDeliveryAddress(obj.get("deliveryAddress").getAsString());
        order.setStatus(obj.get("status").getAsString());
        order.setPaid(obj.get("paid").getAsBoolean());

        String dateStr = obj.get("orderDate").getAsString();
        if (!dateStr.isEmpty()) order.setOrderDate(LocalDate.parse(dateStr));

        // Читаем Product
        JsonObject prodObj = obj.getAsJsonObject("product");
        if (prodObj != null && !prodObj.isJsonNull()) {
            Product prod = new Product(
                    prodObj.get("name").getAsString(),
                    prodObj.get("category").getAsString(),
                    prodObj.get("price").getAsDouble()
            );
            order.setProduct(prod);
        }

        return order;
    }
}
