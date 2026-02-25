package ru.yandex.practicum.service.handler.sensor;

import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.telemetry.event.ClimateSensorAvro;
import ru.yandex.practicum.service.ProducerService;

public class ClimateSensorEvent extends BaseSensorEventHandler<ClimateSensorAvro> {

    public ClimateSensorEvent(ProducerService producer, String sensorTopic) {
        super(producer, sensorTopic);
    }

    @Override
    public SensorEventProto.PayloadCase getMessageType() {
        return SensorEventProto.PayloadCase.CLIMATE_SENSOR;
    }

    @Override
    protected ClimateSensorAvro mapToAvro(SensorEventProto event) {
        return ClimateSensorAvro.newBuilder()
                .setTemperatureC(event.getClimateSensor().getTemperatureC())
                .setHumidity(event.getClimateSensor().getHumidity())
                .setCo2Level(event.getClimateSensor().getCo2Level())
                .build();
    }
}
