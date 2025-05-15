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
@Table(name = "outbox_event")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OutboxEvent {
    @Id
    private UUID id;

    @Column(nullable = false)
    private String aggregateType; // Например, "Order"

    @Column(nullable = false)
    private String aggregateId;   // ID агрегата (Order ID)

    @Column(nullable = false)
    private String type;         // Тип события, например "OrderCreated"

    @Column(nullable = false, columnDefinition = "TEXT")
    private String payload;      // JSON с деталями события

    @Column(nullable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private boolean published;   // Флаг публикации события
}