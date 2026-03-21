package ru.yandex.practicum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.yandex.practicum.dto.cart.CartDto;
import ru.yandex.practicum.entity.Cart;

@Mapper(componentModel = "spring")
public interface CartMapper {

    @Mapping(target = "shoppingCartId", source = "id")
    CartDto toDto(Cart cart);
}
