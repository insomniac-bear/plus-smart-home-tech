package ru.yandex.practicum.service;

import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProducerService {
    private final KafkaProducer<String, SpecificRecordBase> kafkaProducer;

    public void send(String topic, String key, SpecificRecordBase event) {
        ProducerRecord<String, SpecificRecordBase> record = new ProducerRecord<>(topic, key, event);
        kafkaProducer.send(record, (metadata, exception) -> {
            if (exception != null) {
                log.info("Возникла ошибка при отправки сообщения топик {}: {}", topic, exception.getMessage());
            } else {
                log.info("Сообщение успешно отправлено в топик {}", topic);
            }
        });
    }

    @PreDestroy
    public void close() {
        kafkaProducer.flush();
        kafkaProducer.close();
    }}