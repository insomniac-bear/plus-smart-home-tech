package ru.yandex.practicum.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import ru.yandex.practicum.config.ShoppingCartFeignConfig;
import ru.yandex.practicum.dto.cart.CartDto;
import ru.yandex.practicum.dto.cart.ChangeProductQuantityRequestDto;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@FeignClient(name = "shopping-cart", configuration = ShoppingCartFeignConfig.class)
public interface ShoppingCartClient {

    @GetMapping("/api/v1/shopping-cart")
    CartDto getShoppingCart(@RequestParam("username") String username);

    @PutMapping("/api/v1/shopping-cart")
    CartDto putItemInCart(
            @RequestParam("username") String username,
            @RequestBody Map<UUID, Integer> items);

    @DeleteMapping("/api/v1/shopping-cart")
    void deleteCart(@RequestParam("username") String username);

    @PostMapping("/api/v1/shopping-cart/remove")
    CartDto removeItemFromCart(
            @RequestParam("username") String username,
            @RequestBody List<UUID> items);

    @PostMapping("/api/v1/shopping-cart/change-quantity")
    CartDto changeItemQuantity(
            @RequestParam("username") String username,
            @RequestBody ChangeProductQuantityRequestDto quantityRequest);
}