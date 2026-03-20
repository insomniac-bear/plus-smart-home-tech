package ru.yandex.practicum.dto.warehouse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddressDto {
    String country;
    String city;
    String street;
    String house;
    String flat;
}
