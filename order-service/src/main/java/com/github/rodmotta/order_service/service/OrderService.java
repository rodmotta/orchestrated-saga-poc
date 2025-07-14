package com.github.rodmotta.order_service.service;

import com.github.rodmotta.order_service.entity.InventoryStatus;
import com.github.rodmotta.order_service.entity.Order;
import com.github.rodmotta.order_service.entity.PaymentStatus;
import com.github.rodmotta.order_service.entity.Status;
import com.github.rodmotta.order_service.messaging.KafkaProducer;
import com.github.rodmotta.order_service.messaging.event.*;
import com.github.rodmotta.order_service.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final KafkaProducer kafkaProducer;

    public void create() {
        Order order = orderRepository.save(new Order());
        kafkaProducer.createOrderEvent(order.getId());
    }

    public List<Order> getOrders() {
        return orderRepository.findAll();
    }

    public void paymentApproved(PaymentApprovedEvent event) {
        Order order = orderRepository.findById(event.orderId())
                .orElseThrow();
        order.setPaymentStatus(PaymentStatus.APPROVED);
        orderRepository.save(order);
        kafkaProducer.inventoryReserveEvent(order.getId());
    }

    public void paymentFailed(PaymentFailedEvent event) {
        Order order = orderRepository.findById(event.orderId())
                .orElseThrow();
        order.setPaymentStatus(PaymentStatus.FAILED);
        order.setStatus(Status.CANCELLED);
        orderRepository.save(order);
    }

    public void inventoryReserved(InventoryReservedEvent event) {
        Order order = orderRepository.findById(event.orderId())
                .orElseThrow();
        order.setInventoryStatus(InventoryStatus.RESERVED);
        order.setStatus(Status.COMPLETED);
        orderRepository.save(order);
    }

    public void inventoryFailed(InventoryFailedEvent event) {
        Order order = orderRepository.findById(event.orderId())
                .orElseThrow();
        order.setInventoryStatus(InventoryStatus.OUT_OF_STOCK);
        order.setPaymentStatus(PaymentStatus.REFUNDING);
        orderRepository.save(order);
        kafkaProducer.paymentRefund(order.getId());
    }

    public void paymentRefunded(PaymentRefundedEvent event) {
        Order order = orderRepository.findById(event.orderId())
                .orElseThrow();
        order.setPaymentStatus(PaymentStatus.REFUNDED);
        order.setStatus(Status.CANCELLED);
        orderRepository.save(order);
    }
}
