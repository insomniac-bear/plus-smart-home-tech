package ru.yandex.practicum.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class AggregatorService {
    private final Map<String, SensorsSnapshotAvro> snapshots = new HashMap<>();

    public Optional<SensorsSnapshotAvro> saveEvent(SensorEventAvro event) {
        log.info("Получено событие: {}", event);
        SensorsSnapshotAvro snapshot = snapshots.getOrDefault(event.getHubId(), new SensorsSnapshotAvro());

        if (snapshot.getSensorsState() == null) {
            snapshot.setHubId(event.getHubId());
            snapshot.setSensorsState(new HashMap<>());
        }
        SensorStateAvro prevState = snapshot.getSensorsState().get(event.getId());

        if (prevState != null && (prevState.getTimestamp().isAfter(event.getTimestamp()) ||
            prevState.getData().equals(event.getPayload()))) {
            log.info("Событие {} уже было обработано", event);
            return Optional.empty();
        }

        SensorStateAvro savingState = SensorStateAvro.newBuilder()
                .setTimestamp(event.getTimestamp())
                .setData(event.getPayload())
                .build();

        snapshot.setTimstamp(event.getTimestamp());
        snapshot.getSensorsState().put(event.getId(), savingState);
        snapshots.put(event.getId(), snapshot);

        log.info("Событие {} сохранено", event);
        return Optional.of(snapshot);
    }
}
