package com.microservices.service;


import lombok.RequiredArgsConstructor;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.microservices.client.InventoryClient;
import com.microservices.dto.OrderRequest;
import com.microservices.event.OrderPlacedEvent;
import com.microservices.model.Order;
import com.microservices.repository.OrderRepository;

import lombok.extern.slf4j.Slf4j;   

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final InventoryClient inventoryClient;
    private final KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;
    public void placeOrder(OrderRequest orderRequest) {
        boolean isProductInStock = inventoryClient.isInStock(orderRequest.skuCode(), orderRequest.quantity());
        if(!isProductInStock)
            throw new RuntimeException("No product in stock!");
        var order = mapToOrder(orderRequest);
        orderRepository.save(order);

        OrderPlacedEvent orderPlaceEvent = new OrderPlacedEvent();
        orderPlaceEvent.setOrderNumber(order.getOrderNumber());
        orderPlaceEvent.setEmail(orderRequest.userData().email());
        orderPlaceEvent.setFirstName(orderRequest.userData().fistName());
        orderPlaceEvent.setLastName(orderRequest.userData().lastName());
        log.info("Start sending order place event " + orderPlaceEvent);
        kafkaTemplate.send("order-placed", orderPlaceEvent);
        log.info("End sending order place event...");
    }

    private static Order mapToOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        order.setPrice(orderRequest.price());
        order.setQuantity(orderRequest.quantity());
        order.setSkuCode(orderRequest.skuCode());
        return order;
    }
}
