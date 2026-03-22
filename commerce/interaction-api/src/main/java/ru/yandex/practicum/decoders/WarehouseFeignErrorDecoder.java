package ru.yandex.practicum.decoders;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.http.HttpStatus;
import ru.yandex.practicum.dto.exceptions.ErrorResponse;
import ru.yandex.practicum.dto.exceptions.ErrorResponseForWarehouseCheck;
import ru.yandex.practicum.exceptions.ItemAlreadyExistException;
import ru.yandex.practicum.exceptions.LowQuantityException;
import ru.yandex.practicum.exceptions.ProductNotFoundException;

import java.io.IOException;

public class WarehouseFeignErrorDecoder implements ErrorDecoder {
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

            if (response.status() == 400 && HttpStatus.BAD_REQUEST.equals(httpStatus)) {
                switch (ex) {
                    case "ItemAlreadyExistException" -> {
                        return new ItemAlreadyExistException(message, userMessage);
                    }
                    case "ProductNotFoundException" -> {
                        return new ProductNotFoundException(message, userMessage);
                    }
                    case "LowQuantityException" -> {
                        ErrorResponseForWarehouseCheck errorResponseChek = (ErrorResponseForWarehouseCheck) errorResponse;
                        return new LowQuantityException(message,
                                errorResponseChek.getMissingProducts());
                    }
                }
                return new IllegalArgumentException(message);
            }
        } catch (IOException e) {
            return new FeignException.InternalServerError(
                    "Failed to parse error response: " + e.getMessage(),
                    response.request(), null, null);
        }

        return defaultDecoder.decode(methodKey, response);
    }
}
