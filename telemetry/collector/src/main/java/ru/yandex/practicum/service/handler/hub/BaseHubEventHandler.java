package ru.yandex.practicum.service.handler.hub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.service.ProducerService;

import java.time.Instant;

@Slf4j
//@RequiredArgsConstructor
public abstract class BaseHubEventHandler<T extends SpecificRecordBase> implements HubEventHandler {
    protected final ProducerService producer;

    private final String hubTopic;

    public BaseHubEventHandler(ProducerService producer, String hubTopic) {
        this.producer = producer;
        this.hubTopic = hubTopic;
    }

    protected abstract T mapToAvro(HubEventProto event);

    public void handle(HubEventProto event) {
        if (!event.getPayloadCase().equals(getMessageType())) {
            throw new IllegalArgumentException("Invalid payload type");
        }

        T payload = mapToAvro(event);

        Instant timestamp = Instant.ofEpochSecond(
                event.getTimestamp().getSeconds(),
                event.getTimestamp().getNanos()
        );

        HubEventAvro eventAvro = HubEventAvro.newBuilder()
                .setHubId(event.getHubId())
                .setTimestamp(timestamp)
                .setPayload(payload)
                .build();

        log.info("Отправляю событе сенсора {} в топик {}", event, hubTopic);
        producer.send(hubTopic, event.getHubId(), eventAvro);
    }
}
