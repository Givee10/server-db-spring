package com.givee.demo.server.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Класс для обработки ошибок, если запрос неверно оформлен
 */
@SuppressWarnings("serial")
@ResponseStatus(HttpStatus.BAD_REQUEST)
class InvalidRequestFormatException extends RuntimeException {
    public InvalidRequestFormatException() {
        this(null);
    }

    public InvalidRequestFormatException(String msg) {
        super(String.format("Invalid request format. %s", msg));
    }
}
