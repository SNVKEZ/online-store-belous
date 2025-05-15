package org.ssu.belous.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ssu.belous.repository.OutboxEventRepository;

@Service
@RequiredArgsConstructor
@Slf4j  // Убедитесь, что у вас есть аннотация @Slf4j для логгирования
public class KafkaPublisherScheduler {
    private final OutboxEventRepository outboxRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Scheduled(fixedDelay = 5000)
    @Transactional
    public void publishEvents() {
        outboxRepository.findAllByPublishedFalse().forEach(event -> {
            try {
                kafkaTemplate.send("order-events", event.getType(), event.getPayload())
                        .whenComplete((result, ex) -> {
                            if (ex == null) {
                                event.setPublished(true);
                                outboxRepository.save(event);
                                log.info("Successfully sent event {}", event.getId());
                            } else {
                                log.error("Failed to send event {}", event.getId(), ex);
                            }
                        });
            } catch (Exception e) {
                log.error("Error processing event {}", event.getId(), e);
            }
        });
    }
}