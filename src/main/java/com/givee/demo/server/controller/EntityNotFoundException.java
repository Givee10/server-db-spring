package com.givee.demo.server.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Класс для обработки ошибок, если запрашивается несуществующий элемент
 */
@SuppressWarnings("serial")
@ResponseStatus(HttpStatus.NOT_FOUND)
class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(Object entityClass, String parameter, String value) {
        super(String.format("Could not find %s with parameter %s = %s", entityClass, parameter, value));
    }

    public EntityNotFoundException(Object entityClass, Object objectId) {
        this(entityClass, "id", String.valueOf(objectId));
    }
}
