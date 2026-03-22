package ru.yandex.practicum.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import ru.yandex.practicum.config.WarehouseFeignConfig;
import ru.yandex.practicum.dto.cart.CartDto;
import ru.yandex.practicum.dto.cart.ChangeProductQuantityRequestDto;
import ru.yandex.practicum.dto.warehouse.AddNewItemInWarehouseRequest;
import ru.yandex.practicum.dto.warehouse.AddressDto;
import ru.yandex.practicum.dto.warehouse.ReservedItemDto;

import java.util.UUID;

@FeignClient(name = "warehouse", configuration = WarehouseFeignConfig.class)
public interface WarehouseClient {

    @PutMapping("/api/v1/warehouse")
    void addNewProduct(@RequestBody AddNewItemInWarehouseRequest request);

    @PostMapping("/api/v1/warehouse/check")
    ReservedItemDto checkProductQuantity(@RequestBody CartDto cart);

    @PostMapping("/api/v1/warehouse/add")
    void addProductToWarehouse(@RequestBody ChangeProductQuantityRequestDto request);

    @GetMapping("/api/v1/warehouse/address")
    AddressDto getWarehouseAddress();

    @DeleteMapping("/api/v1/warehouse/reservation")
    void cancelReservation(@RequestParam UUID shoppingCartId);
}
