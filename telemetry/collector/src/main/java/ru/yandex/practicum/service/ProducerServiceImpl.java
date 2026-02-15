package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.serializer.AvroSerializer;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProducerServiceImpl implements ProducerService {
    private final KafkaTemplate<String, byte[]> kafkaTemplate;

    @Value("${collector.topics.sensors}")
    private String sensorsTopic;

    @Value("${collector.topics.hubs}")
    private String hubsTopic;

    @Override
    public void sendSensorEvent(SensorEventAvro eventAvro) {
        log.info("Отправка SensorEvent в kafka");
        byte[] payload = AvroSerializer.serialize(eventAvro);
        kafkaTemplate.send(sensorsTopic, eventAvro.getId(), payload);
    }

    @Override
    public void sendHubEvent(HubEventAvro eventAvro) {
        log.info("Отправка HubEvent в kafka");
        byte[] payload = AvroSerializer.serialize(eventAvro);
        kafkaTemplate.send(hubsTopic, eventAvro.getHubId(), payload);
    }
}
