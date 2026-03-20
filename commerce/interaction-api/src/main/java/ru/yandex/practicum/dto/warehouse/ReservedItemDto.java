package ru.yandex.practicum.dto.warehouse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReservedItemDto {
    Double deliveryWeight;
    Double deliveryVolume;
    Boolean fragile;
}
