package ru.yandex.practicum.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ProductNotFoundInCartException extends RuntimeException {
    private final String userMessage;
    private final HttpStatus httpStatus;

    public ProductNotFoundInCartException(String message, String userMessage) {
        super(message);
        this.userMessage = userMessage;
        this.httpStatus = HttpStatus.BAD_REQUEST;
    }
}
