package com.expert.orders.controllers;

import com.expert.orders.model.DataManager;
import com.expert.orders.model.Order;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;

import java.util.ArrayList;
import java.util.List;

public class StatsController {
    @FXML private Label totalOrders;
    @FXML private Label avgPrice;
    @FXML private PieChart statusChart;

    // Храним ссылки на слушатели, если захочется потом отписаться (опционально)
    private final List<Order> observedOrders = new ArrayList<>();

    @FXML
    public void initialize() {
        // Подписываемся на изменения списка заказов (добавление / удаление)
        DataManager.orders.addListener((ListChangeListener<Order>) change -> {
            while (change.next()) {
                if (change.wasRemoved()) {
                    for (Order rem : change.getRemoved()) detachOrderListeners(rem);
                }
                if (change.wasAdded()) {
                    for (Order add : change.getAddedSubList()) attachOrderListeners(add);
                }
            }
            updateStats();
        });

        // Для уже существующих заказов повесим слушатели
        for (Order o : DataManager.orders) {
            attachOrderListeners(o);
        }

        // Инициалный расчёт
        updateStats();
    }

    // Повесить слушатели на свойства заказа, которые влияют на статистику
    private void attachOrderListeners(Order o) {
        if (observedOrders.contains(o)) return;
        observedOrders.add(o);

        // при изменении статуса — пересчитываем статистику
        o.statusProperty().addListener((obs, oldV, newV) -> updateStats());

        // при изменении товара (цены) — пересчитать среднюю
        o.productProperty().addListener((obs, oldP, newP) -> updateStats());

        // при изменении оплаты — пересчитать (если нужно учитывать)
        o.paidProperty().addListener((obs, oldV, newV) -> updateStats());
    }

    // Отписка (очистка)
    private void detachOrderListeners(Order o) {
        // В простом варианте у нас нет сохранённых объектных ссылок на сам listeners,
        // поэтому тут достаточно убрать из observedOrders — GC очистит.
        observedOrders.remove(o);
        // Если нужны явные removeListener — можно хранить ссылки на listener-объекты.
    }

    // Пересчёт статистики
    private void updateStats() {
        int total = DataManager.orders.size();
        totalOrders.setText(String.valueOf(total));

        double avg = DataManager.orders.stream()
                .mapToDouble(Order::getTotalPrice)
                .average()
                .orElse(0.0);
        avgPrice.setText(String.format("%.2f", avg));

        long newCount = DataManager.orders.stream().filter(o -> "В обработке".equals(o.getStatus())).count();
        long assembling = DataManager.orders.stream().filter(o -> "В сборке".equals(o.getStatus())).count();
        long delivered = DataManager.orders.stream().filter(o -> "Доставлен".equals(o.getStatus())).count();
        long turndelivered = DataManager.orders.stream().filter(o -> "Доставка в процессе".equals(o.getStatus())).count();


        ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList(
                new PieChart.Data("В обработке", newCount),
                new PieChart.Data("В сборке", assembling),
                new PieChart.Data("Доставлен", delivered),
                new PieChart.Data("Доставка в процессе", turndelivered)
        );

        statusChart.setData(pieData);
    }
}
