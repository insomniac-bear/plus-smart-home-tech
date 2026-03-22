package ru.yandex.practicum.dto.exceptions;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StackTraceElementDto {
    private String classLoaderName;
    private String moduleName;
    private String moduleVersion;
    private String methodName;
    private String fileName;
    private int lineNumber;
    private String className;
    private boolean nativeMethod;
}
