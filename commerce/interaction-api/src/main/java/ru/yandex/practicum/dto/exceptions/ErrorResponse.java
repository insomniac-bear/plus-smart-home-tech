package ru.yandex.practicum.dto.exceptions;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ErrorResponse {
    private HttpStatus httpStatus;
    private String userMessage;
    private String message;
    private String ex;
    private StackTraceElementDto[] stackTrace;
    private Object cause;
    private Object[] suppressed;
    private String localizedMessage;
    private String timestamp;
}
