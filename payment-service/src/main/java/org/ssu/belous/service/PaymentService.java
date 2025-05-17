package org.ssu.belous.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.ssu.belous.model.Payment;
import org.ssu.belous.repository.PaymentRepository;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "order-events", groupId = "payment-group")
    public void handleOrderCreated(String message) {
        try {
            JsonNode json = objectMapper.readTree(message);

            String eventType = json.get("type").asText();
            if (!eventType.equals("OrderCreated")) {
                return;
            }

            JsonNode payload = json.get("payload");
            UUID orderId = UUID.fromString(payload.get("id").asText());

            Payment payment = Payment.builder()
                    .id(UUID.randomUUID())
                    .orderId(orderId)
                    .success(true)
                    .paidAt(Instant.now())
                    .build();

            paymentRepository.save(payment);
            System.out.println("Платеж проведен: " + orderId);

        } catch (Exception e) {
            System.err.println("Ошибка платежа: " + e.getMessage());
        }
    }
}