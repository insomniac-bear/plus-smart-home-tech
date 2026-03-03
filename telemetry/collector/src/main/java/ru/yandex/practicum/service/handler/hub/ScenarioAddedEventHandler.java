package ru.yandex.practicum.service.handler.hub;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.grpc.telemetry.event.ScenarioConditionProto;
import ru.yandex.practicum.kafka.telemetry.event.ActionTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.ConditionOperationAvro;
import ru.yandex.practicum.kafka.telemetry.event.ConditionTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.DeviceActionAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioConditionAvro;
import ru.yandex.practicum.service.ProducerService;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class ScenarioAddedEventHandler extends BaseHubEventHandler<ScenarioAddedEventAvro>{

    protected ScenarioAddedEventHandler(ProducerService producer, String hubTopic) {
        super(producer, hubTopic);
    }

    @Override
    public HubEventProto.PayloadCase getMessageType() {
        return HubEventProto.PayloadCase.SCENARIO_ADDED;
    }

    @Override
    protected ScenarioAddedEventAvro mapToAvro(HubEventProto event) {

        List<ScenarioConditionAvro> conditions = event.getScenarioAdded().getConditionsList().stream()
                .map(condition -> {
                    ScenarioConditionAvro.Builder builder = ScenarioConditionAvro.newBuilder()
                            .setSensorId(condition.getSensorId())
                            .setType(ConditionTypeAvro.valueOf(condition.getType().name()))
                            .setOperation(ConditionOperationAvro.valueOf(condition.getOperation().name()));

                    switch(condition.getValueCase()) {
                        case INT_VALUE ->builder.setValue(condition.getIntValue());
                        case BOOL_VALUE -> builder.setValue(condition.getBoolValue());
                        case VALUE_NOT_SET -> builder.setValue(null);
                    }

                    return builder.build();
                })
                .toList();

        List<DeviceActionAvro> actions = event.getScenarioAdded().getActionList().stream()
                .map(action -> DeviceActionAvro.newBuilder()
                        .setSensorId(action.getSensorId())
                        .setType(ActionTypeAvro.valueOf(action.getType().name()))
                        .setValue(action.getValue())
                        .build())
                .toList();


        return ScenarioAddedEventAvro.newBuilder()
                .setName(event.getScenarioAdded().getName())
                .setConditions(conditions)
                .setActions(actions)
                .build();
    }
}
