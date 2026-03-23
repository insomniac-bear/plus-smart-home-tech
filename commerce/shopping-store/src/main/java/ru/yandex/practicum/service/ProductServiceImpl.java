package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.dto.store.ProductCategory;
import ru.yandex.practicum.dto.store.ProductDto;
import ru.yandex.practicum.dto.store.ProductState;
import ru.yandex.practicum.dto.store.QuantityState;
import ru.yandex.practicum.dto.store.UpdateProductDto;
import ru.yandex.practicum.entity.Product;
import ru.yandex.practicum.exceptions.ProductNotFoundException;
import ru.yandex.practicum.mapper.ProductMapper;
import ru.yandex.practicum.repository.ProductRepository;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService {
    private final ProductRepository repository;
    private final ProductMapper mapper;

    @Override
    public Page<ProductDto> getProducts(ProductCategory category, Pageable pageable) {
        log.info("Запрос на получение списка товаров с сатегорией {}", category);
        Page<Product> rawProducts = repository.findByProductCategory(category, pageable);
        Page<ProductDto> res = rawProducts.map(mapper::toDto);
        log.info("Получен список товаров: {}", res);
        return res;
    }

    @Override
    @Transactional
    public ProductDto create(ProductDto productDto) {
        log.info("Запрос на создание товара: {}", productDto);
        Product savedProduct = repository.save(mapper.toEntity(productDto));
        log.info("Создан товар: {}", savedProduct);
        return mapper.toDto(savedProduct);
    }

    @Override
    @Transactional
    public ProductDto update(UpdateProductDto productDto) {
        log.info("Запрос на обновление товара: {}", productDto);
        Product product = repository.findById(productDto.getProductId())
                .orElseThrow(() -> {
                    log.info("Товар с id: {} не найден", productDto.getProductId());
                    return new ProductNotFoundException("Товар с id: " + productDto.getProductId() + " не найден", "");
                });
        Product updatedProduct = mapper.updateDtoToEntity(productDto, product);
        log.info("Товар обновлен: {}", updatedProduct);
        return mapper.toDto(updatedProduct);
    }

    @Override
    @Transactional
    public Boolean delete(UUID id) {
        log.info("Запрос на удаление товара с id: {}", id);
        Product product = repository.findById(id)
                .orElse(null);
        if (product == null) {
            log.info("Товар с id: {} не найден", id);
            return false;
        }

        product.setProductState(ProductState.DEACTIVATE);
        log.info("Товар с id: {} удален", id);
        return true;
    }

    @Override
    @Transactional
    public Boolean updateQuantityState(UUID id, QuantityState state) {
        log.info("Запрос на обновление количества товара с id: {}", id);

        Product product = repository.findById(id)
                .orElseThrow(() -> {
                    log.info("Товар с id: {} не найден", id);
                    return new ProductNotFoundException("Товар с id: " + id + " не найден", "Товар с id: " + id + " не найден");
                });

        product.setQuantityState(state);
        log.info("Количество товара с id: {} обновлено", id);
        repository.save(product);

        return true;
    }

    @Override
    public ProductDto getProduct(UUID id) {
        log.info("Запрос на получение товара с id: {}", id);

        Product product = repository.findById(id)
                .orElseThrow(() -> {
                    log.info("Товар с id: {} не найден", id);
                    return new ProductNotFoundException("Товар с id: " + id + " не найден", "Товар с id: " + id + " не найден");
                });
        log.info("Получен товар: {}", product);
        return mapper.toDto(product);
    }
}
