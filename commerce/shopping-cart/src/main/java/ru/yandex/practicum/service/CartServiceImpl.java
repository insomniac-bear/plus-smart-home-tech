package ru.yandex.practicum.service;

import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.client.WarehouseClient;
import ru.yandex.practicum.dto.cart.CartDto;
import ru.yandex.practicum.dto.cart.ChangeProductQuantityRequestDto;
import ru.yandex.practicum.entity.Cart;
import ru.yandex.practicum.exceptions.LowQuantityException;
import ru.yandex.practicum.exceptions.ProductNotFoundInCartException;
import ru.yandex.practicum.mapper.CartMapper;
import ru.yandex.practicum.repository.CartRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CartServiceImpl implements CartService {
    private final CartRepository repository;
    private final CartMapper mapper;
    private final WarehouseClient warehouseClient;

    @Override
    public CartDto getCart(String userId) {
        Cart cart = getOrCreateCart(userId);
        log.info("Получена корзина {} для пользователя {}", cart, userId);
        return mapper.toDto(cart);
    }

    @Override
    @Transactional
    public CartDto addToCart(String userName, Map<UUID, Integer> products) {
        Cart cart = getOrCreateCart(userName);
        log.info("Получена корзина {} для пользователя {}", cart, userName);
        cart.getProducts().putAll(products);

        try {
            log.info("Проверка доступности товаров на складе для корзины {}", cart);
            CartDto cartDto = mapper.toDto(cart);
            warehouseClient.checkProductQuantity(cartDto);
        } catch (LowQuantityException e) {
            log.error("Недостаточно товаров на складе для корзины {}", cart.getId());
            throw e;
        }

        repository.save(cart);
        log.info("Товары {} добавлены в корзину для пользователя {}", products, userName);
        return mapper.toDto(cart);
    }

    @Override
    @Transactional
    public void deleteCart(String userName) {
        Optional<Cart> cart = repository.findByUserName(userName);
        if (cart.isPresent()) {
            repository.deleteById(cart.get().getId());
            log.info("Корзина {} удалена для пользователя {}", cart.get(), userName);
        }
    }

    @Override
    @Transactional
    public CartDto removeFromCart(String userName, List<UUID> productIds) {
        Cart cart = findCart(userName);

        productIds.forEach(productId -> {
            validateProductInCart(cart, productId);
            cart.getProducts().remove(productId);
            log.info("Товар {} удален из корзины для пользователя {}", productId, userName);
        });
        repository.save(cart);
        return mapper.toDto(cart);
    }

    @Override
    @Transactional
    public CartDto changeQuantity(String userName, ChangeProductQuantityRequestDto changeProductQuantityRequestDto) {
        Cart cart = findCart(userName);

        UUID productId = changeProductQuantityRequestDto.getProductId();
        validateProductInCart(cart, productId);

        cart.getProducts().put(productId, changeProductQuantityRequestDto.getNewQuantity());
        repository.save(cart);
        log.info("Количество товара {} в корзине для пользователя {} изменено на {}", productId, userName,
                changeProductQuantityRequestDto.getNewQuantity());
        return mapper.toDto(cart);
    }

    private Cart getOrCreateCart(String userName) {
        return repository.findByUserName(userName)
                .orElseGet(() -> {
                    Cart newCart = repository.save(new Cart(UUID.randomUUID(), userName, new HashMap<>()));
                    log.info("Создана новая корзина для пользователя {}", userName);
                    return newCart;
                });
    }

    private Cart findCart(String userName) {
        return repository.findByUserName(userName)
                .orElseThrow(() -> {
                    log.info("Корзина не найдена для пользователя {}", userName);
                    return new NotFoundException("Корзина не найдена");
                });
    }

    private void validateProductInCart(Cart cart, UUID productId) {
        log.info("Проверка наличия товара {} в корзине {}", productId, cart);
        if (cart.getProducts().containsKey(productId)) {
            log.info("Товар {} не найден в корзине {}", productId, cart);
            throw new ProductNotFoundInCartException("Bad request", "Товар с id " + productId + " не найден в корзине");
        }
    }
}
