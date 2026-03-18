package ru.yandex.practicum.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.dto.common.PageResponseDto;
import ru.yandex.practicum.dto.store.ProductCategory;
import ru.yandex.practicum.dto.store.ProductDto;
import ru.yandex.practicum.dto.store.QuantityState;
import ru.yandex.practicum.dto.store.UpdateProductDto;
import ru.yandex.practicum.service.ProductService;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/shopping-store")
@RequiredArgsConstructor
public class ShoppingStoreController {
    private final ProductService service;

    @GetMapping()
    public PageResponseDto<ProductDto> getProducts(@RequestParam ProductCategory category, Pageable pageable) {
        log.info("Получение товаров по категории {} с пагинацией {}", category, pageable);
        Page<ProductDto> products = service.getProducts(category, pageable);
        return new PageResponseDto<>(products);
    }

    @PutMapping()
    public ProductDto createProduct(@Valid @RequestBody ProductDto productDto) {
        log.info("Создание товара {}", productDto);
        return service.create(productDto);
    }

    @PostMapping()
    public ProductDto updateProduct(@Valid @RequestBody UpdateProductDto productDto) {
        log.info("Обновление товара {}", productDto);
        return service.update(productDto);
    }

    @PostMapping("/removeProductFromStore")
    public boolean deleteProduct(@RequestBody UUID id) {
        log.info("Удаление товара с id {}", id);
        return service.delete(id);
    }

    @PostMapping("/quantityState")
    public boolean updateQuantityState(@RequestParam UUID productId, @RequestParam QuantityState quantityState) {
        log.info("Обновление количества товара {} на {}", productId, quantityState);
        return service.updateQuantityState(productId, quantityState);
    }

    @GetMapping("/{productId}")
    public ProductDto getProduct(@PathVariable UUID productId) {
        log.info("Получение товара с id {}", productId);
        return service.getProduct(productId);
    }
}
