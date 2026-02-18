package ru.yandex.practicum.service;

import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

public interface ProducerService {
    void sendSensorEvent(SensorEventAvro eventAvro);

    void sendHubEvent(HubEventAvro eventAvro);
}
