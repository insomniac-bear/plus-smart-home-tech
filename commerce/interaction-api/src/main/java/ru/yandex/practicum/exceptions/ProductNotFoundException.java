package ru.yandex.practicum.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ProductNotFoundException extends RuntimeException {
        private final String userMessage;
        private final HttpStatus httpStatus;

        public ProductNotFoundException(String message, String userMessage) {
            super(message);
            this.userMessage = userMessage;
            this.httpStatus = HttpStatus.NOT_FOUND;
        }
}
