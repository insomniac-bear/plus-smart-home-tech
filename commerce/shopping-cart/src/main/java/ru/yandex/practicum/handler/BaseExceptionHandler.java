package ru.yandex.practicum.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.dto.exceptions.ErrorResponse;
import ru.yandex.practicum.dto.exceptions.StackTraceElementDto;
import ru.yandex.practicum.exceptions.ProductNotFoundInCartException;
import ru.yandex.practicum.exceptions.UnauthorizedException;

import java.util.Arrays;

@Slf4j
@RestControllerAdvice
public class BaseExceptionHandler {
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedException(UnauthorizedException ex) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .httpStatus(HttpStatus.UNAUTHORIZED)
                .userMessage(ex.getUserMessage())
                .message(ex.getMessage())
                .stackTrace(getStackTrace(ex))
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

//    @ExceptionHandler(ProductInShoppingCartLowQuantityInWarehouse.class)
//    public ResponseEntity<ErrorResponseForWarehouseChek> handleProductInShoppingCartLowQuantityInWarehouse
//            (ProductInShoppingCartLowQuantityInWarehouse ex) {
//        ErrorResponseForWarehouseChek errorResponse = new ErrorResponseForWarehouseChek();
//        errorResponse.setHttpStatus("400 BAD_REQUEST");
//        errorResponse.setUserMessage(ex.getUserMessage());
//        errorResponse.setMessage(ex.getMessage());
//        errorResponse.setEx("ProductInShoppingCartLowQuantityInWarehouse");
//        errorResponse.setStackTrace(getStackTrace(ex));
//        errorResponse.setMissingProducts(ex.getMissingProducts());
//        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
//    }

    @ExceptionHandler(ProductNotFoundInCartException.class)
    public ResponseEntity<ErrorResponse> handleNoProductsInShoppingCartException(ProductNotFoundInCartException ex) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .httpStatus(HttpStatus.BAD_REQUEST)
                .userMessage(ex.getUserMessage())
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
