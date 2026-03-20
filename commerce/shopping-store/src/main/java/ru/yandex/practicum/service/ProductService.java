package ru.yandex.practicum.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.yandex.practicum.dto.store.ProductCategory;
import ru.yandex.practicum.dto.store.ProductDto;
import ru.yandex.practicum.dto.store.QuantityState;
import ru.yandex.practicum.dto.store.UpdateProductDto;

import java.util.UUID;

public interface ProductService {
    Page<ProductDto> getProducts(ProductCategory category, Pageable pageable);

    ProductDto create(ProductDto productDto);

    ProductDto update(UpdateProductDto productDto);

    Boolean delete(UUID id);

    Boolean updateQuantityState(UUID id, QuantityState state);

    ProductDto getProduct(UUID id);
}
