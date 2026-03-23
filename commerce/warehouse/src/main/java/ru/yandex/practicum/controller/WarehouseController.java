package ru.yandex.practicum.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.dto.cart.CartDto;
import ru.yandex.practicum.dto.warehouse.AddNewItemInWarehouseRequest;
import ru.yandex.practicum.dto.warehouse.AddProductToWarehouseRequestDto;
import ru.yandex.practicum.dto.warehouse.AddressDto;
import ru.yandex.practicum.dto.warehouse.ReservedItemDto;
import ru.yandex.practicum.service.WarehouseService;

import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/warehouse")
public class WarehouseController {

    private final WarehouseService service;

    @PutMapping
    public void newProductInWarehouse(@RequestBody @Valid AddNewItemInWarehouseRequest request) {
        log.info("PUT / - запрос на добавление нового товара на склад: {}", request);
        service.addItem(request);
    }

    @PostMapping("/check")
    public ReservedItemDto checkProductQuantityEnoughForShoppingCart(@RequestBody @Valid CartDto cart) {
        log.info("POST /check - проверка доступности товаров для корзины: {}", cart);
        return service.checkProductQuantity(cart);
    }

    @PostMapping("/add")
    public void addProductToWarehouse(@RequestBody @Valid AddProductToWarehouseRequestDto request) {
        log.info("POST /add - пополнение товара на складе: {}", request);
        service.addItemQuantity(request);
    }

    @GetMapping(value = "/address", produces = MediaType.APPLICATION_JSON_VALUE)
    public AddressDto getWarehouseAddress() {
        log.info("GET /address - получение адреса склада");
        AddressDto address = service.getAddress();
        log.info("GET /address - адрес склада: {}", address);
        return address;
    }

    @DeleteMapping("/reservation")
    public void cancelReservation(@RequestParam UUID shoppingCartId) {
        log.info("DELETE /reservation - отмена резерва для корзины: {}", shoppingCartId);
        service.cancelReserve(shoppingCartId);
    }
}
