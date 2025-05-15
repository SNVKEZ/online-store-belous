package org.ssu.belous.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {
    @Id
    private UUID id;

    @Column(nullable = false)
    private String product;

    @Column(nullable = false)
    private String quantity;

    @Column(nullable = false)
    private String status; // CREATED, PAID, FAILED

    @Column(nullable = false)
    private Instant createdAt;
}