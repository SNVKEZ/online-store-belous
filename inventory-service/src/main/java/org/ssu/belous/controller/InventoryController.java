package org.ssu.belous.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.ssu.belous.dto.AddInventoryCost;
import org.ssu.belous.dto.AddInventoryRequestDto;
import org.ssu.belous.model.Inventory;
import org.ssu.belous.repository.InventoryRepository;
import org.ssu.belous.service.InventoryService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;
    private final InventoryRepository inventoryRepository;

    // Получить все товары
    @GetMapping("/products")
    public ResponseEntity<List<Inventory>> getAllProducts() {
        List<Inventory> inventories = inventoryRepository.findAll();
        return ResponseEntity.ok(inventories);
    }

    // Получить товар по ID
    @GetMapping("/products/{id}")
    public ResponseEntity<Inventory> getProductById(@PathVariable("id") UUID id) {
        Optional<Inventory> inventory = inventoryRepository.findById(id);
        return inventory.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Добавить/обновить количество товара
    @PutMapping("/add_inventory")
    public ResponseEntity<Inventory> createOrUpdate(@RequestBody AddInventoryRequestDto addInventoryRequestDto) {
        Inventory inventory = inventoryService.addInventory(addInventoryRequestDto);
        return ResponseEntity.ok(inventory);
    }

    // Изменить стоимость товара по названию продукта
    @PutMapping("/update_cost")
    public ResponseEntity<Inventory> updateCost(@RequestBody AddInventoryCost addInventoryCost) {
        Inventory updatedInventory = inventoryService.updateCost(addInventoryCost);
        return ResponseEntity.ok(updatedInventory);
    }
}
