package org.ssu.belous.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ssu.belous.dto.AddInventoryRequestDto;
import org.ssu.belous.model.Inventory;
import org.ssu.belous.repository.InventoryRepository;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "order-events", groupId = "inventory-group")
    public void handleOrderCreated(String message) {
        try {
            JsonNode json = objectMapper.readTree(message);
            String eventType = json.get("type").asText();
            if (!"OrderCreated".equals(eventType)) {
                return;
            }

            JsonNode payload = json.get("payload");
            String product = payload.get("product").asText();
            int quantity = payload.get("quantity").asInt();

            Optional<Inventory> optionalInventory = inventoryRepository.findByProduct(product);

            if (optionalInventory.isEmpty()) {
                log.warn("Товар не найден на складе: " + product);
                return;
            }

            Inventory inventory = optionalInventory.get();

            if (inventory.getQuantity() < quantity) {
                log.warn("Недостаточно товара на складе: " + product);
                return;
            }

            inventory.setQuantity(inventory.getQuantity() - quantity);
            inventoryRepository.save(inventory);

            log.warn("Зарезервировано" + quantity + " единиц товара " + product);

        } catch (Exception e) {
            log.warn("Ошибка обработки обновления склада: " + e.getMessage());
        }
    }

    @Transactional
    public Inventory addInventory(AddInventoryRequestDto addInventoryRequestDto) {
        Inventory inventory;
        if (inventoryRepository.existsByProduct(addInventoryRequestDto.product())) {
            inventory = Inventory.builder()
                    .id(inventoryRepository.findByProduct(addInventoryRequestDto.product()).get().getId())
                    .product(addInventoryRequestDto.product())
                    .quantity(inventoryRepository.findByProduct(addInventoryRequestDto.product()).get().getQuantity() + addInventoryRequestDto.quantity())
                    .build();
        } else {
            inventory = Inventory.builder()
                    .id(UUID.randomUUID())
                    .product(addInventoryRequestDto.product())
                    .quantity(addInventoryRequestDto.quantity())
                    .build();
        }

        inventoryRepository.save(inventory);
        return inventory;
    }
}