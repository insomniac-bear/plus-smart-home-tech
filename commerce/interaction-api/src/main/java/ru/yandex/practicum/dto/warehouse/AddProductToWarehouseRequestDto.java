package ru.yandex.practicum.dto.warehouse;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddProductToWarehouseRequestDto {
    @NotNull
    private UUID productId;

    @NotNull
    @Min(1)
    private int quantity;

    @Override
    public String toString() {
        return "AddProductToWarehouseRequestDto{" +
                "productId=" + productId +
                ", quantity=" + quantity +
                '}';
    }
}
