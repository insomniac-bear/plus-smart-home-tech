package ru.yandex.practicum.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import ru.yandex.practicum.config.ShoppingStoreFeignConfig;
import ru.yandex.practicum.dto.common.PageResponseDto;
import ru.yandex.practicum.dto.store.ProductCategory;
import ru.yandex.practicum.dto.store.ProductDto;
import ru.yandex.practicum.dto.store.QuantityState;

import java.util.UUID;

@FeignClient(name = "shopping-store", configuration = ShoppingStoreFeignConfig.class)
public interface ShoppingStoreClient {

    @GetMapping("/api/v1/shopping-store")
    PageResponseDto<ProductDto> getProducts(
            @RequestParam("category") ProductCategory category,
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam(value = "sort", required = false) String sort);

    @PutMapping("/api/v1/shopping-store")
    ProductDto createNewProduct(@RequestBody ProductDto productDto);

    @PostMapping("/api/v1/shopping-store")
    ProductDto updateProduct(@RequestBody ProductDto productDto);

    @PostMapping("/api/v1/shopping-store/removeProductFromStore")
    Boolean deleteProduct(@RequestBody UUID productId);

    @PostMapping("/api/v1/shopping-store/quantityState")
    Boolean updateQuantityState(
            @RequestParam("productId") UUID productId,
            @RequestParam("quantityState") QuantityState quantityState);

    @GetMapping("/api/v1/shopping-store/{productId}")
    ProductDto getProduct(@PathVariable("productId") UUID productId);
}