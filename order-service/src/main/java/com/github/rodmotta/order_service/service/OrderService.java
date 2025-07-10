package com.github.rodmotta.order_service.service;

import com.github.rodmotta.order_service.entity.Order;
import com.github.rodmotta.order_service.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final KafkaTemplate<String, Order> kafkaTemplate;

    public void create() {
        Order order = orderRepository.save(new Order());
        kafkaTemplate.send("order.created", order);
    }

    public List<Order> getOrders() {
        return orderRepository.findAll();
    }
}
