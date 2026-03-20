package ru.yandex.practicum.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.yandex.practicum.dto.store.ProductDto;
import ru.yandex.practicum.dto.store.UpdateProductDto;
import ru.yandex.practicum.entity.Product;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(target = "productId", source = "product.id")
    ProductDto toDto(Product product);

    @Mapping(target = "id", ignore = true)
    Product toEntity(ProductDto productDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    Product updateDtoToEntity(UpdateProductDto productDto, @MappingTarget Product product);
}
