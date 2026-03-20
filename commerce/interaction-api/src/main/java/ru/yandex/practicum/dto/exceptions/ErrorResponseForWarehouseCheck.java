package ru.yandex.practicum.dto.exceptions;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@SuperBuilder
public class ErrorResponseForWarehouseCheck extends ErrorResponse {
    private List<UUID> missingProducts;
}
