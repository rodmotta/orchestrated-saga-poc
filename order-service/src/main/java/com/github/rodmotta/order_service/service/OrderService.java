package com.github.rodmotta.order_service.service;

import com.github.rodmotta.order_service.entity.InventoryStatus;
import com.github.rodmotta.order_service.entity.Order;
import com.github.rodmotta.order_service.entity.PaymentStatus;
import com.github.rodmotta.order_service.entity.Status;
import com.github.rodmotta.order_service.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final KafkaTemplate<String, Order> kafkaTemplate;
    private final KafkaTemplate<String, String> kafkaTemplateString;

    public void create() {
        Order order = orderRepository.save(new Order());
        kafkaTemplate.send("order.created", order);
    }

    public List<Order> getOrders() {
        return orderRepository.findAll();
    }

    @KafkaListener(topics = "payment.approved", groupId = "order-service")
    public void paymentApproved(String orderId) {
        orderId = orderId.replace("\"", "");
        Order order = orderRepository.findById(UUID.fromString(orderId))
                .orElseThrow();
        order.setPaymentStatus(PaymentStatus.APPROVED);
        orderRepository.save(order);
    }

    @KafkaListener(topics = "payment.failed", groupId = "order-service")
    public void paymentFailed(String orderId) {
        orderId = orderId.replace("\"", "");
        Order order = orderRepository.findById(UUID.fromString(orderId))
                .orElseThrow();
        order.setPaymentStatus(PaymentStatus.FAILED);
        order.setStatus(Status.CANCELLED);
        orderRepository.save(order);
    }

    @KafkaListener(topics = "inventory.reserved", groupId = "order-service")
    public void inventoryReserved(String orderId) {
        orderId = orderId.replace("\"", "");
        Order order = orderRepository.findById(UUID.fromString(orderId))
                .orElseThrow();
        order.setInventoryStatus(InventoryStatus.RESERVED);
        order.setStatus(Status.COMPLETED);
        orderRepository.save(order);
    }

    @KafkaListener(topics = "inventory.failed", groupId = "order-service")
    public void inventoryFailed(String orderId) {
        orderId = orderId.replace("\"", "");
        Order order = orderRepository.findById(UUID.fromString(orderId))
                .orElseThrow();
        order.setInventoryStatus(InventoryStatus.FAILED);
        order.setPaymentStatus(PaymentStatus.FAILED); //rollback
        order.setStatus(Status.CANCELLED);
        orderRepository.save(order);
    }
}
