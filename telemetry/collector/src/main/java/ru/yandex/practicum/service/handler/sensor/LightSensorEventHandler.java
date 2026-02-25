package ru.yandex.practicum.service.handler.sensor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.telemetry.event.LightSensorAvro;

@Slf4j
@Component
public class LightSensorEventHandler extends BaseSensorEventHandler<LightSensorAvro> {

    @Override
    public SensorEventProto.PayloadCase getMessageType() {
        return SensorEventProto.PayloadCase.LIGHT_SENSOR;
    }

    @Override
    protected LightSensorAvro mapToAvro(SensorEventProto event) {
        return LightSensorAvro.newBuilder()
                .setLinkQuality(event.getLightSensor().getLinkQuality())
                .setLuminosity(event.getLightSensor().getLuminosity())
                .build();
    }
}
