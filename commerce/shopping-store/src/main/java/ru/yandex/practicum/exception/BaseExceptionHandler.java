package ru.yandex.practicum.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.dto.exceptions.ErrorResponse;
import ru.yandex.practicum.dto.exceptions.StackTraceElementDto;
import ru.yandex.practicum.exceptions.ProductNotFoundException;

import java.util.Arrays;

@Slf4j
@RestControllerAdvice
public class BaseExceptionHandler {
    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleProductNotFoundException(ProductNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setHttpStatus(HttpStatus.NOT_FOUND);
        errorResponse.setUserMessage(ex.getUserMessage());
        errorResponse.setMessage(ex.getMessage());
        errorResponse.setStackTrace(getStackTrace(ex));

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    private StackTraceElementDto[] getStackTrace(Throwable ex) {
        return Arrays.stream(ex.getStackTrace())
                .map(ste -> new StackTraceElementDto(
                        ste.getClassLoaderName(),
                        ste.getModuleName(),
                        ste.getModuleVersion(),
                        ste.getMethodName(),
                        ste.getFileName(),
                        ste.getLineNumber(),
                        ste.getClassName(),
                        ste.isNativeMethod()))
                .toArray(StackTraceElementDto[]::new);
    }
}
