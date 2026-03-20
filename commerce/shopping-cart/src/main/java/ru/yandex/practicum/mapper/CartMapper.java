package ru.yandex.practicum.mapper;

import org.mapstruct.Mapper;
import ru.yandex.practicum.dto.cart.CartDto;
import ru.yandex.practicum.entity.Cart;

@Mapper(componentModel = "spring")
public interface CartMapper {

    CartDto toDto(Cart cart);
}
