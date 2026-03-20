package ru.yandex.practicum.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.dto.exceptions.ErrorResponse;
import ru.yandex.practicum.dto.exceptions.ErrorResponseForWarehouseCheck;
import ru.yandex.practicum.dto.exceptions.StackTraceElementDto;
import ru.yandex.practicum.exceptions.ItemAlreadyExistException;
import ru.yandex.practicum.exceptions.LowQuantityException;
import ru.yandex.practicum.exceptions.ProductNotFoundException;

import java.util.Arrays;

@Slf4j
@RestControllerAdvice
public class BaseExceptionHandler {

    @ExceptionHandler(ItemAlreadyExistException.class)
    public ResponseEntity<ErrorResponse> handleSpecifiedProductAlreadyInWarehouseException
            (ItemAlreadyExistException ex) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .httpStatus(HttpStatus.BAD_REQUEST)
                .userMessage(ex.getUserMessage())
                .ex("ItemAlreadyExistException")
                .message(ex.getMessage())
                .stackTrace(getStackTrace(ex))
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(LowQuantityException.class)
    public ResponseEntity<ErrorResponseForWarehouseCheck> handleProductInShoppingCartLowQuantityInWarehouse
            (LowQuantityException ex) {
        ErrorResponseForWarehouseCheck errorResponse = ErrorResponseForWarehouseCheck.builder()
                .missingProducts(ex.getMissingProducts())
                .build();
        errorResponse.setHttpStatus(HttpStatus.BAD_REQUEST);
        errorResponse.setUserMessage(ex.getUserMessage());
        errorResponse.setMessage(ex.getMessage());
        errorResponse.setEx("ProductInShoppingCartLowQuantityInWarehouse");
        errorResponse.setStackTrace(getStackTrace(ex));
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoSpecifiedProductInWarehouseException
            (ProductNotFoundException ex) {
        ErrorResponse errorResponse = ErrorResponse.builder()
            .httpStatus(HttpStatus.BAD_REQUEST)
                .userMessage(ex.getUserMessage())
                .ex("ProductNotFoundException")
                .message(ex.getMessage())
                .stackTrace(getStackTrace(ex))
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
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
