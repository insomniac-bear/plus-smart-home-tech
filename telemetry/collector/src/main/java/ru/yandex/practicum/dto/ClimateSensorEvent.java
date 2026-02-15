package ru.yandex.practicum.dto;

import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.yandex.practicum.util.SensorEventType;

@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@ToString
public class ClimateSensorEvent extends SensorEvent {
    @NotNull
    private int temperatureC;

    @NotNull
    private int humidity;

    @NotNull
    int co2Level;

    @Override
    public SensorEventType getType() {
        return SensorEventType.TEMPERATURE_SENSOR_EVENT;
    }
}
