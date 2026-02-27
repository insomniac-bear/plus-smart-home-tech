package ru.yandex.practicum;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;
import ru.yandex.practicum.service.AggregatorService;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class AggregatorStarter {

    private static final Map<TopicPartition, OffsetAndMetadata> currentOffsets = new HashMap<>();
    private final KafkaConsumer<String, SensorEventAvro> consumer;
    private final KafkaProducer<String, SpecificRecordBase> producer;
    private final AggregatorService service;
    private final String snapshotsTopic;
    private final String sensorsTopic;

    public void start() {
        Runtime.getRuntime().addShutdownHook(new Thread(consumer::wakeup));
        try {
            consumer.subscribe(List.of(sensorsTopic));
            while (true) {
                ConsumerRecords<String, SensorEventAvro> records = consumer.poll(Duration.ofSeconds(4));

                int count = 0;

                for (ConsumerRecord<String, SensorEventAvro> record: records) {
                    handleRecord(record);
                    manageOffsets(record, count, consumer);
                    count++;
                }
                consumer.commitAsync();
            }
        } catch (WakeupException ignored) {}
            catch (Exception e) {
                log.error("Ошибка во время обработки событий от сенсоров");
            } finally {
                try {
                    producer.flush();
                    consumer.commitSync(currentOffsets);

                } finally {
                    log.info("Закрываем консьюмер");
                    consumer.close();
                    log.info("Закрываем продюсер");
                    producer.close();
            }
        }
    }

    private static void manageOffsets(ConsumerRecord<String, SensorEventAvro> record,
                                      int count, KafkaConsumer<String, SensorEventAvro> consumer) {
        currentOffsets.put(
                new TopicPartition(record.topic(), record.partition()),
                new OffsetAndMetadata(record.offset() + 1)
        );

        if (count % 10 == 0) {
            consumer.commitAsync(currentOffsets, (offsets, exception) -> {
                if (exception != null) {
                    log.warn("Ошибка во время фиксации оффсетов: {}", offsets, exception);
                }
            });
        }
    }

    private void handleRecord(ConsumerRecord<String, SensorEventAvro> record) {
        log.info("топик = {}, партиция = {}, смещение = {}, значение: {}\n",
                record.topic(), record.partition(), record.offset(), record.value());
        Optional<SensorsSnapshotAvro> snapshot = service.saveEvent(record.value());
        if (snapshot.isPresent()) {
            SensorsSnapshotAvro snapshotAvro = snapshot.get();
            ProducerRecord<String, SpecificRecordBase> message = new ProducerRecord<>(snapshotsTopic,
                    snapshotAvro.getHubId(), snapshotAvro);
            producer.send(message, (metadata, exception) -> {
                if (exception != null) {
                    log.info("Возникла ошибка при отправки сообщения в топик {}: {}", snapshotsTopic,
                            exception.getMessage());
                } else {
                    log.info("Сообщение успешно отправлено в топик {}", snapshotsTopic);
                }
            });
        }
    }
}
