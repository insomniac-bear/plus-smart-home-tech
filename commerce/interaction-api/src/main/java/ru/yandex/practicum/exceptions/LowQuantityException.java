package ru.yandex.practicum.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.UUID;

@Getter
public class LowQuantityException extends RuntimeException {
    private final String userMessage;
    private final HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
    private final List<UUID> missingProducts;

    public LowQuantityException(String message, List<UUID> missingProducts) {
        super(message);
        this.userMessage = message;
        this.missingProducts = missingProducts;
    }

}
