package ru.yandex.practicum.service.handler.sensor;

import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.telemetry.event.MotionSensorAvro;
import ru.yandex.practicum.service.ProducerService;

public class MotionSensorEventHandler extends BaseSensorEventHandler<MotionSensorAvro> {
    public MotionSensorEventHandler(ProducerService producer, String sensorTopic) {
        super(producer, sensorTopic);
    }

    @Override
    public SensorEventProto.PayloadCase getMessageType() {
        return SensorEventProto.PayloadCase.MOTION_SENSOR;
    }

    @Override
    protected MotionSensorAvro mapToAvro(SensorEventProto event) {
        return MotionSensorAvro.newBuilder()
                .setMotion(event.getMotionSensor().getMotion())
                .setVoltage(event.getMotionSensor().getVoltage())
                .setLinkQuality(event.getMotionSensor().getLinkQuality())
                .build();
    }
}
