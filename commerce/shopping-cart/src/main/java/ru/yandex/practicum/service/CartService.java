package ru.yandex.practicum.service;

import ru.yandex.practicum.dto.cart.CartDto;
import ru.yandex.practicum.dto.cart.ChangeProductQuantityRequestDto;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface CartService {

    CartDto getCart(String userId);

    CartDto addToCart(String userName, Map<UUID, Integer> products);

    void deleteCart(String userName);

    CartDto removeFromCart(String userName, List<UUID> productIds);

    CartDto changeQuantity(String userName, ChangeProductQuantityRequestDto changeProductQuantityRequestDto);
}
