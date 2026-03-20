package ru.yandex.practicum.dto.store;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProductDto {
    private UUID productId;

    private String productName;

    private String description;

    private String imageSrc;

    private QuantityState quantityState;

    private ProductState productState;

    private ProductCategory productCategory;

    @Min(1)
    private double price;
}
