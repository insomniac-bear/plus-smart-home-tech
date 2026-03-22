package ru.yandex.practicum.config;

import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.yandex.practicum.decoders.WarehouseFeignErrorDecoder;

@Configuration
public class WarehouseFeignConfig {
    @Bean
    public ErrorDecoder warehouseErrorDecoder() {
        return new WarehouseFeignErrorDecoder();
    }
}
