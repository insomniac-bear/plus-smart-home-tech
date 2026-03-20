package ru.yandex.practicum.dto.warehouse;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DimensionDto {
    @NotNull
    @Min(1)
    Double width;

    @NotNull
    @Min(1)
    Double height;

    @NotNull
    @Min(1)
    Double depth;
}
