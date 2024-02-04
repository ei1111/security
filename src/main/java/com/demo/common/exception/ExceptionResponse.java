package com.demo.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class ExceptionResponse {
    private String resultCode;
    private LocalDateTime timestamp;
    private String message;
    private String details;
}
