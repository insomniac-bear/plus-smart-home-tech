package ru.yandex.practicum.dto.exceptions;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@Builder
public class ErrorResponse {
    private HttpStatus httpStatus;
    private String userMessage;
    private String message;
    private String ex;
    private StackTraceElementDto[] stackTrace;
    private Object cause;
    private Object[] suppressed;
    private String localizedMessage;
}
