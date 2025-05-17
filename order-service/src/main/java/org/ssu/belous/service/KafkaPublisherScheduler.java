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
@Slf4j
public class KafkaPublisherScheduler {
    private final OutboxEventRepository outboxRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Scheduled(fixedDelay = 2000)
    @Transactional
    public void publishEvents() {
        outboxRepository.findAllByPublishedFalse().forEach(event -> {
            try {
                kafkaTemplate.send("order-events", event.getType(), event.getPayload())
                        .whenComplete((result, ex) -> {
                            if (ex == null) {
                                event.setPublished(true);
                                outboxRepository.save(event);
                                log.info("Ивент отправлен {}", event.getId());
                            } else {
                                log.error("Ошибка отправления ивента {}", event.getId(), ex);
                            }
                        });
            } catch (Exception e) {
                log.error("Ошибка {}", event.getId(), e);
            }
        });
    }
}