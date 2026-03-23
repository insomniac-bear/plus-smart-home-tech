package ru.yandex.practicum.dto.warehouse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReservedItemDto {
    private Double deliveryWeight;
    private Double deliveryVolume;
    private Boolean fragile;

    @Override
    public String toString() {
        return "ReservedItemDto{" +
                "deliveryWeight=" + deliveryWeight +
                ", deliveryVolume=" + deliveryVolume +
                ", fragile=" + fragile +
                '}';
    }
}
