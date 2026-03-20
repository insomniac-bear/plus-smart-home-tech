package ru.yandex.practicum.decoders;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.http.HttpStatus;
import ru.yandex.practicum.dto.exceptions.ErrorResponse;
import ru.yandex.practicum.exceptions.ProductNotFoundException;

import java.io.IOException;

public class ShoppingStoreFeignErrorDecoder implements ErrorDecoder {
    private final ErrorDecoder defaultDecoder = new Default();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Exception decode(String methodKey, Response response) {
        try {
            ErrorResponse errorResponse = objectMapper.readValue(
                    response.body().asInputStream(), ErrorResponse.class);
            HttpStatus httpStatus = errorResponse.getHttpStatus();
            String message = errorResponse.getMessage();
            String userMessage = errorResponse.getUserMessage();

            if (response.status() == 404) {
                if (HttpStatus.NOT_FOUND.equals(httpStatus)) {
                    return new ProductNotFoundException(message, userMessage);
                }
            }
        } catch (IOException e) {
            return new FeignException.InternalServerError(
                    "Failed to parse error response: " + e.getMessage(),
                    response.request(), null, null);
        }

        return defaultDecoder.decode(methodKey, response);
    }
}
