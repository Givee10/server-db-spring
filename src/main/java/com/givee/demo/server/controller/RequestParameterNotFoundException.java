package com.givee.demo.server.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Класс для обработки ошибок, если не указан параметр
 */
@SuppressWarnings("serial")
@ResponseStatus(HttpStatus.BAD_REQUEST)
class RequestParameterNotFoundException extends RuntimeException {
    public RequestParameterNotFoundException(Object paramClass, Object objectId) {
        super(String.format("Parameter of type '%s' with name '%s' is absent.", String.valueOf(paramClass), String.valueOf(objectId)));
    }
}
