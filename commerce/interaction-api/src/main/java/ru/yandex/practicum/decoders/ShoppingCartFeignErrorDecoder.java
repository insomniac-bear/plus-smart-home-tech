package ru.yandex.practicum.decoders;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.http.HttpStatus;
import ru.yandex.practicum.dto.exceptions.ErrorResponse;
import ru.yandex.practicum.dto.exceptions.ErrorResponseForWarehouseCheck;
import ru.yandex.practicum.exceptions.LowQuantityException;
import ru.yandex.practicum.exceptions.ProductNotFoundException;
import ru.yandex.practicum.exceptions.UnauthorizedException;

import java.io.IOException;

public class ShoppingCartFeignErrorDecoder implements ErrorDecoder {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ErrorDecoder defaultDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        try {
            ErrorResponse errorResponse = objectMapper.readValue(
                    response.body().asInputStream(), ErrorResponse.class);
            HttpStatus httpStatus = errorResponse.getHttpStatus();
            String message = errorResponse.getMessage();
            String userMessage = errorResponse.getUserMessage();
            String ex = errorResponse.getEx();

            switch (response.status()) {
                case 401:
                    if (HttpStatus.UNAUTHORIZED.equals(httpStatus)) {
                        return new UnauthorizedException(message, userMessage);
                    }
                    break;
                case 400:
                    if (HttpStatus.BAD_REQUEST.equals(httpStatus)) {
                        if (ex.equals("ProductNotFoundException")) {
                            return new ProductNotFoundException(message, userMessage);
                        } else if (ex.equals("LowQuantityException")) {
                            ErrorResponseForWarehouseCheck errorResponseChek = (ErrorResponseForWarehouseCheck) errorResponse;
                            return new LowQuantityException(message, errorResponseChek.getMissingProducts());
                        }
                        return new IllegalArgumentException(message);
                    }
                    break;
            }
        } catch (IOException e) {
            return new FeignException.InternalServerError(
                    "Failed to parse error response: " + e.getMessage(),
                    response.request(), null, null);
        }

        return defaultDecoder.decode(methodKey, response);
    }
}