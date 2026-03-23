package ru.yandex.practicum.dto.store;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SetProductQuantityStateRequestDto {

    @NotNull
    private UUID productId;

    @NotNull
    private QuantityState quantity;
}
