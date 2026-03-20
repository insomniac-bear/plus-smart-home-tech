package ru.yandex.practicum.dto.cart;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.UUID;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CartDto {

    @NotNull
    private UUID shoppingCartId;

    @NotEmpty
    private Map<UUID, Integer> products;
}
