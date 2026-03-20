package ru.yandex.practicum.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class UnauthorizedException extends RuntimeException {
    private final String userMessage;
    private final HttpStatus httpStatus;
    public UnauthorizedException(String message, String userMessage) {
        super(message);
        this.userMessage = userMessage;
        this.httpStatus = HttpStatus.UNAUTHORIZED;
    }
}
