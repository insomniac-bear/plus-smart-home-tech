package ru.yandex.practicum.service.handler.hub;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.kafka.telemetry.event.ActionTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.ConditionOperationAvro;
import ru.yandex.practicum.kafka.telemetry.event.ConditionTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.DeviceActionAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioConditionAvro;
import ru.yandex.practicum.service.ProducerService;

import java.util.stream.Collectors;

@Slf4j
@Component
public class ScenarioAddedEventHandler extends BaseHubEventHandler<ScenarioAddedEventAvro>{

    public ScenarioAddedEventHandler(ProducerService producer, String hubTopic) {
        super(producer, hubTopic);
    }

    @Override
    public HubEventProto.PayloadCase getMessageType() {
        return HubEventProto.PayloadCase.SCENARIO_ADDED;
    }

    @Override
    protected ScenarioAddedEventAvro mapToAvro(HubEventProto event) {
        return ScenarioAddedEventAvro.newBuilder()
                .setName(event.getScenarioAdded().getName())
                .setConditions(event.getScenarioAdded().getConditionsList().stream()
                        .map(scenario -> ScenarioConditionAvro.newBuilder()
                                .setOperation(ConditionOperationAvro.valueOf(scenario.getOperation().name()))
                                .setType(ConditionTypeAvro.valueOf(scenario.getType().name()))
                                .setSensorId(scenario.getSensorId())
                                .build()
                        )
                        .collect(Collectors.toList()))
                .setActions(event.getScenarioAdded().getActionList().stream()
                        .map(action -> DeviceActionAvro.newBuilder()
                                .setType(ActionTypeAvro.valueOf(action.getType().name()))
                                .setSensorId(action.getSensorId())
                                .setValue(action.getValue())
                                .build()
                        )
                        .collect(Collectors.toList()))
                .build();
    }
}
