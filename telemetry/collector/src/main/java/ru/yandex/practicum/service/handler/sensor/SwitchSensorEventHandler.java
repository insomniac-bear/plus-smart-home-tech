package ru.yandex.practicum.service.handler.sensor;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.telemetry.event.SwitchSensorAvro;
import ru.yandex.practicum.service.ProducerService;

@Component
public class SwitchSensorEventHandler extends BaseSensorEventHandler<SwitchSensorAvro> {

    public SwitchSensorEventHandler(ProducerService producer, String sensorTopic) {
        super(producer, sensorTopic);
    }

    @Override
    public SensorEventProto.PayloadCase getMessageType() {
        return SensorEventProto.PayloadCase.SWITCH_SENSOR;
    }

    @Override
    protected SwitchSensorAvro mapToAvro(SensorEventProto event) {
        return SwitchSensorAvro.newBuilder()
                .setState(event.getSwitchSensor().getState())
                .build();
    }
}
