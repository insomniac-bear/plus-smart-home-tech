package ru.yandex.practicum.config;

import deserializer.HubEventDeserializer;
import deserializer.SensorsSnapshotDeserializer;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.util.Properties;

@Slf4j
@Configuration
public class KafkaConfig {

    @Bean
    public KafkaConsumer<String, HubEventAvro> hubEventKafkaConsumer(@Value("${kafka.bootstrap-servers}") String bootstrapServers) {
        Properties props = new Properties();
        props.put(ConsumerConfig.CLIENT_ID_CONFIG, "analyzer-hub-event-consumer");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "analyzer-hub-group");
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, HubEventDeserializer.class.getName());

        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 100);
        props.put(ConsumerConfig.FETCH_MAX_BYTES_CONFIG, 3072000);
        props.put(ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG, 307200);

        KafkaConsumer<String, HubEventAvro> consumer = new KafkaConsumer<>(props);
        log.info("Создан HubEventConsumer: {}", bootstrapServers);

        return consumer;
    }

    @Bean
    public KafkaConsumer<String, SensorsSnapshotAvro> sensorsSnapshotKafkaConsumer(@Value("${kafka.bootstrap-servers}") String bootstrapServers) {
        Properties props = new Properties();
        props.put(ConsumerConfig.CLIENT_ID_CONFIG, "analyzer-sensors-snapshot-consumer");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "analyzer-snapshot-group");
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, SensorsSnapshotDeserializer.class.getName());

        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 100);
        props.put(ConsumerConfig.FETCH_MAX_BYTES_CONFIG, 3072000);
        props.put(ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG, 307200);

        KafkaConsumer<String, SensorsSnapshotAvro> consumer = new KafkaConsumer<>(props);
        log.info("Создан SensorSnapshotConsumer: {}", bootstrapServers);

        return consumer;
    }

    @Bean
    public String snapshotTopic(@Value("${kafka.topic.snapshots}") String snapshotTopic) {
        log.info("Настроен топик для хабов: {}", snapshotTopic);
        return snapshotTopic;
    }

    @Bean
    public String hubTopic(@Value("${kafka.topic.hubs}") String hubTopic) {
        log.info("Настроен топик для сенсоров: {}", hubTopic);
        return hubTopic;
    }
}
