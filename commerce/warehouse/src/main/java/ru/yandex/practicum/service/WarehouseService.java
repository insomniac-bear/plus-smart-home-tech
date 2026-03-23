package ru.yandex.practicum.service;

import ru.yandex.practicum.dto.cart.CartDto;
import ru.yandex.practicum.dto.warehouse.AddNewItemInWarehouseRequest;
import ru.yandex.practicum.dto.warehouse.AddProductToWarehouseRequestDto;
import ru.yandex.practicum.dto.warehouse.AddressDto;
import ru.yandex.practicum.dto.warehouse.ReservedItemDto;

import java.util.UUID;

public interface WarehouseService {
    void addItem(AddNewItemInWarehouseRequest request);

    ReservedItemDto checkProductQuantity(CartDto cart);

    void addItemQuantity(AddProductToWarehouseRequestDto request);

    AddressDto getAddress();

    void cancelReserve(UUID cartId);
}
