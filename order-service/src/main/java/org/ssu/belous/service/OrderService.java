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
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OutboxEventRepository outboxRepository;
    private final ObjectMapper objectMapper;

    public Order createOrder(String product, int quantity) {
        Order order = Order.builder()
                .id(UUID.randomUUID())
                .product(product)
                .quantity(quantity)
                .status("CREATED")
                .createdAt(Instant.now())
                .build();

        orderRepository.save(order);

        try {
            Map<String, Object> event = new HashMap<>();
            event.put("type", "OrderCreated");

            Map<String, Object> payload = new HashMap<>();
            payload.put("id", order.getId().toString());
            payload.put("product", product);
            payload.put("quantity", quantity);
            event.put("payload", payload);

            OutboxEvent outboxEvent = OutboxEvent.builder()
                    .id(UUID.randomUUID())
                    .aggregateType("Order")
                    .aggregateId(order.getId().toString())
                    .type("OrderCreated")
                    .payload(objectMapper.writeValueAsString(event))
                    .createdAt(Instant.now())
                    .published(false)
                    .build();

            outboxRepository.save(outboxEvent);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return order;
    }
}