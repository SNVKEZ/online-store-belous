package org.ssu.belous.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.ssu.belous.model.Order;
import org.ssu.belous.model.OutboxEvent;
import org.ssu.belous.repository.OrderRepository;
import org.ssu.belous.repository.OutboxEventRepository;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OutboxEventRepository outboxRepository;
    private final ObjectMapper objectMapper;

    public Order createOrder(String product, String quantity) {
        Order order = Order.builder()
                .id(UUID.randomUUID())
                .product(product)
                .quantity(quantity)
                .status("CREATED")
                .createdAt(Instant.now())
                .build();

        orderRepository.save(order);

        try {
            String payload = objectMapper.writeValueAsString(order);

            OutboxEvent event = OutboxEvent.builder()
                    .id(UUID.randomUUID())
                    .aggregateType("Order")
                    .aggregateId(order.getId().toString())
                    .type("OrderCreated")
                    .payload(payload)
                    .createdAt(Instant.now())
                    .published(false)
                    .build();

            outboxRepository.save(event);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize order", e);
        }

        return order;
    }

    // Можно добавить методы для обновления статуса заказа и генерации событий аналогично
}