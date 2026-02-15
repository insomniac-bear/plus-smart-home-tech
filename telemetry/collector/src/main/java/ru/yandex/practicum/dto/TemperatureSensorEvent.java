package ru.yandex.practicum.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.yandex.practicum.util.SensorEventType;

@Getter
@Setter
@ToString(callSuper = true)
public  class TemperatureSensorEvent extends SensorEvent {
    @NotNull
    private int temperatureC;

    @NotNull
    private int temperatureF;

    @Override
    public SensorEventType getType() {
        return SensorEventType.TEMPERATURE_SENSOR_EVENT;
    }

}
