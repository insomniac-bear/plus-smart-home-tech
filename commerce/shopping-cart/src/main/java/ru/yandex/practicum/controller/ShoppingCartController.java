package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.dto.cart.CartDto;
import ru.yandex.practicum.dto.cart.ChangeProductQuantityRequestDto;
import ru.yandex.practicum.service.CartService;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/shopping-cart")
@RequiredArgsConstructor
public class ShoppingCartController {
    private final CartService service;

    @GetMapping()
    public CartDto getShoppingCart(@RequestParam String username) {
        log.info("GET /shopping-cart - Запрос на получение корзины пользователя {}", username);
        return service.getCart(username);
    }

    @PutMapping()
    public CartDto addToShoppingCart(@RequestParam String username, @RequestBody Map<UUID, Integer> products) {
        log.info("PUT /shopping-cart - Запрос на добавление товаров {} в корзину пользователя {}", products, username);
        return service.addToCart(username, products);
    }

    @DeleteMapping()
    public void deleteShoppingCart(@RequestParam String username) {
        log.info("DELETE /shopping-cart - Запрос на удаление корзины пользователя {}", username);
        service.deleteCart(username);
    }

    @PostMapping("/remove")
    public CartDto removeFromShoppingCart(@RequestParam String username, @RequestBody List<UUID> productIds) {
        log.info("POST /remove - Запрос на удаление товаров {} из корзины пользователя {}", productIds, username);
        return service.removeFromCart(username, productIds);
    }

    @PostMapping("/change-quantity")
    public CartDto changeQuantity(@RequestParam String username, @RequestBody ChangeProductQuantityRequestDto products) {
        log.info("POST /change-quantity - Запрос на изменение количества товаров {} в корзине пользователя {}", products, username);
        return service.changeQuantity(username, products);
    }
}
