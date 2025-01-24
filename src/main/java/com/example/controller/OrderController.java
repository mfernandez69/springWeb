package com.example.controller;

import com.example.models.*;
import com.example.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/orders")
public class OrderController {
    private final OrderRepository repository;

    @Autowired
    public OrderController(OrderRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        List<Order> pedidos = this.repository.findAll();

        // Datos para el gráfico de líneas (total de ventas por día)
        Map<LocalDate, Double> ventasPorDia = pedidos.stream()
                .collect(Collectors.groupingBy(
                        order -> order.getFechaPedido().toLocalDate(),
                        Collectors.summingDouble(Order::getTotal)
                ));

        List<String> labels1 = new ArrayList<>(ventasPorDia.keySet().stream()
                .sorted()
                .map(date -> date.format(DateTimeFormatter.ISO_DATE))
                .collect(Collectors.toList()));
        List<Double> data1 = labels1.stream()
                .map(label -> ventasPorDia.get(LocalDate.parse(label)))
                .collect(Collectors.toList());

        // Datos para el gráfico de barras (cantidad de productos vendidos)
        Map<String, Long> productosCantidad = pedidos.stream()
                .filter(order -> order.getItems() != null)  // Filtrar órdenes con items null
                .flatMap(order -> order.getItems().stream())
                .filter(item -> item.getProduct() != null)  // Filtrar items con producto null
                .collect(Collectors.groupingBy(
                        item -> item.getProduct().getTitle(),
                        Collectors.summingLong(OrderItem::getCantidad)
                ));

        List<String> labels2 = new ArrayList<>(productosCantidad.keySet());
        List<Long> data2 = labels2.stream()
                .map(productosCantidad::get)
                .collect(Collectors.toList());

        model.addAttribute("labels1", labels1);
        model.addAttribute("data1", data1);
        model.addAttribute("labels2", labels2);
        model.addAttribute("data2", data2);

        return "orders/dashboard";
    }

}
