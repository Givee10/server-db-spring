package com.givee.demo.server.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Класс для обработки ошибок, если задан неправильный параметр запроса
 */
@SuppressWarnings("serial")
@ResponseStatus(HttpStatus.BAD_REQUEST)
class InvalidParameterValueException extends RuntimeException {
    public InvalidParameterValueException(String paramName) {
        super(String.format("Invalid parameter value: %s", paramName));
    }
}
