package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.dto.cart.CartDto;
import ru.yandex.practicum.dto.warehouse.AddNewItemInWarehouseRequest;
import ru.yandex.practicum.dto.warehouse.AddProductToWarehouseRequestDto;
import ru.yandex.practicum.dto.warehouse.AddressDto;
import ru.yandex.practicum.dto.warehouse.ReservedItemDto;
import ru.yandex.practicum.entity.Item;
import ru.yandex.practicum.entity.Reserve;
import ru.yandex.practicum.exceptions.ItemAlreadyExistException;
import ru.yandex.practicum.exceptions.LowQuantityException;
import ru.yandex.practicum.exceptions.ProductNotFoundException;
import ru.yandex.practicum.repository.ItemRepository;
import ru.yandex.practicum.repository.ReserveRepository;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WarehouseServiceImpl implements WarehouseService {
    private final ItemRepository itemRepository;
    private final ReserveRepository reserveRepository;

    private static final String[] ADDRESSES = new String[] {"ADDRESS_1", "ADDRESS_2"};

    private static final String CURRENT_ADDRESS =
            ADDRESSES[Random.from(new SecureRandom()).nextInt(0, ADDRESSES.length)];

    @Override
    @Transactional
    public void addItem(AddNewItemInWarehouseRequest request) {
        log.info("Добавление товара {} на склад", request);

        if (itemRepository.existsById(request.getProductId())) {
            log.warn("Товар уже существует");
            throw new ItemAlreadyExistException("Товар уже существует", "Товар с id " + request.getProductId() + " уже существует");
        }

        Item item = Item.builder()
                .id(request.getProductId())
                .fragile(request.getFragile())
                .width(request.getDimension().getWidth())
                .height(request.getDimension().getHeight())
                .depth(request.getDimension().getDepth())
                .weight(request.getWeight())
                .quantity(0)
                .build();

        itemRepository.save(item);
        log.info("Товар {} добавлен на склад", request.getProductId());
    }

    @Override
    @Transactional
    public ReservedItemDto checkProductQuantity(CartDto cart) {
        log.info("Резервирование товаров для корзины: {}", cart.getShoppingCartId());

        Map<UUID, Item> products = itemRepository.findAllById(cart.getProducts().keySet())
                .stream()
                .collect(HashMap::new,
                        (map, item) -> map.put(item.getId(), item),
                        HashMap::putAll);

        log.info("Товары на складе: {}", products);

        double totalWeight = 0;
        double totalVolume = 0;
        boolean hasFragile = false;
        List<UUID> missingProducts = new ArrayList<>();

        for (Map.Entry<UUID, Integer> entry : cart.getProducts().entrySet()) {
            UUID productId = entry.getKey();
            long requestedQuantity = entry.getValue();
            Item item = products.get(productId);

            if (item == null) {
                log.info("Товар с ID {} не найден", productId);
                throw new ProductNotFoundException("Товар не найден",
                        "Товар " + productId + " не найден");
            }

            if (item.getQuantity() < requestedQuantity) {
                log.info("Недостаточно товара на складе: {} (есть {}, нужно {})",
                        productId, item.getQuantity(), requestedQuantity);
                missingProducts.add(productId);
            } else {
                totalWeight += item.getWeight() * requestedQuantity;
                totalVolume += (item.getWidth() * item.getHeight() * item.getDepth()) * requestedQuantity;
                hasFragile = hasFragile || item.isFragile();
            }
        }

        if (!missingProducts.isEmpty()) {
            log.info("Недостаток товаров на складе для корзины {}: {}", cart.getShoppingCartId(), missingProducts);
            throw new LowQuantityException("Недостаточно товаров на складе",
                    missingProducts);
        }

        for (Map.Entry<UUID, Integer> entry : cart.getProducts().entrySet()) {
            UUID productId = entry.getKey();
            long requestedQuantity = entry.getValue();
            Item item = products.get(productId);

            item.setReservedQuantity(item.getReservedQuantity() + requestedQuantity);
            itemRepository.save(item);

            Reserve reserve = Reserve.builder()
                    .cartId(cart.getShoppingCartId())
                    .productId(productId)
                    .reservedQuantity(requestedQuantity)
                    .build();
            reserveRepository.save(reserve);

            log.info("Зарезервировано {} ед. товара {} для корзины {}", requestedQuantity, productId,
                    cart.getShoppingCartId());
        }

        ReservedItemDto result = ReservedItemDto.builder()
                .deliveryWeight(totalWeight)
                .deliveryVolume(totalVolume)
                .fragile(hasFragile)
                .build();
        log.info("Резерв для корзины {}. Вес: {}, Объем: {}, Хрупкий: {}",
                cart.getShoppingCartId(), totalWeight, totalVolume, hasFragile);
        return result;
    }

    @Override
    @Transactional
    public void addItemQuantity(AddProductToWarehouseRequestDto request) {
        log.info("Увеличение количества товара {} на складе на {} единиц", request.getProductId(), request.getQuantity());
        Item item = itemRepository.findById(request.getProductId())
                .orElseThrow(() -> {
                    log.error("Товар с id {} не найден на складе", request.getProductId());
                    return new ProductNotFoundException(
                            "Товар не найден на складе",
                            "Товар с id " + request.getProductId() + " не найден на складе");
                });
        item.setQuantity(item.getQuantity() + request.getQuantity());
        itemRepository.save(item);
        log.info("Товар {} успешно пополнен на {} единиц. Новое количество: {}",
                request.getProductId(), request.getQuantity(), item.getQuantity());
    }

    @Override
    public AddressDto getAddress() {
        String address = CURRENT_ADDRESS;
        log.info("Получение адреса склада: {}", address);
        return AddressDto.builder()
                .country(address)
                .city(address)
                .street(address)
                .house(address)
                .flat(address)
                .build();
    }

    @Override
    @Transactional
    public void cancelReserve(UUID cartId) {
        log.info("Отмена резерва для корзины {}", cartId);
        List<Reserve> reserves = reserveRepository.findByCartId(cartId);

        Map<UUID, Item> products = itemRepository.findAllById(
                reserves.stream().map(Reserve::getProductId).toList()
        ).stream().collect(HashMap::new,
                (map, item) -> map.put(item.getId(), item), HashMap::putAll);

        for (Reserve reserve : reserves) {
            Item item = products.get(reserve.getProductId());
            if (item == null) {
                log.info("Товар с ID {} не найден при отмене резерва", reserve.getProductId());
                throw new ProductNotFoundException(
                        "Товар не найден",
                        "Товар " + reserve.getProductId() + " не найден");
            }

            item.setQuantity(item.getQuantity() + reserve.getReservedQuantity());
            item.setReservedQuantity(item.getReservedQuantity() - reserve.getReservedQuantity());
            itemRepository.save(item);

            log.info("Резерв на {} ед. товара {} отменен для корзины {}",
                    reserve.getReservedQuantity(), reserve.getProductId(), cartId);

            reserveRepository.delete(reserve);
        }
        log.info("Резервы для корзины {} успешно отменены", cartId);
    }
}
