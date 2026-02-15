package ru.yandex.practicum.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.yandex.practicum.util.SensorEventType;

@Getter
@Setter
@ToString
public class SwitchSensorEvent extends SensorEvent {
    @NotNull
    private boolean state;

    @Override
    public SensorEventType getType() {
        return SensorEventType.SWITCH_SENSOR_EVENT;
    }
}
