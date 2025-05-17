package org.ssu.belous.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.ssu.belous.dto.AddInventoryRequestDto;
import org.ssu.belous.model.Inventory;
import org.ssu.belous.service.InventoryService;

@RestController
@RequestMapping("/inventory")
@RequiredArgsConstructor
public class InventoryController {
    private final InventoryService inventoryService;

    @PutMapping("/add_inventory")
    public ResponseEntity<Inventory> create(@RequestBody AddInventoryRequestDto addInventoryRequestDto) {
        Inventory order = inventoryService.addInventory(addInventoryRequestDto);
        return ResponseEntity.ok(order);
    }
}