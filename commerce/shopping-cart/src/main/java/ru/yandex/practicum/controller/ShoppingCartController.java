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
    public CartDto getShoppingCart(@RequestParam String userName) {
        log.info("Запрос на получение корзины пользователя {}", userName);
        return service.getCart(userName);
    }

    @PutMapping()
    public CartDto addToShoppingCart(@RequestParam String userName, @RequestBody Map<UUID, Integer> products) {
        log.info("Запрос на добавление товара {} в корзину пользователя {}", products, userName);
        return service.addToCart(userName, products);
    }

    @DeleteMapping()
    public void deleteShoppingCart(@RequestParam String userName) {
        log.info("Запрос на удаление корзины пользователя {}", userName);
        service.deleteCart(userName);
    }

    @PostMapping("/remove")
    public CartDto removeFromShoppingCart(@RequestParam String userName, @RequestBody List<UUID> productIds) {
        log.info("Запрос на удаление товаров {} из корзины пользователя {}", productIds, userName);
        return service.removeFromCart(userName, productIds);
    }

    @PostMapping("/change-quantity")
    public CartDto changeQuantity(@RequestParam String userName, @RequestBody ChangeProductQuantityRequestDto products) {
        log.info("Запрос на изменение количества товаров {} в корзине пользователя {}", products, userName);
        return service.changeQuantity(userName, products);
    }
}
