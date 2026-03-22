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

    @NotNull(message = "Shopping cart id cannot be null")
    private UUID shoppingCartId;

    @NotEmpty(message = "Products cannot be empty")
    private Map<UUID, Integer> products;

    @Override
    public String toString() {
        return "CartDto{" +
                "shoppingCartId=" + shoppingCartId +
                ", products=" + products +
                '}';
    }
}
