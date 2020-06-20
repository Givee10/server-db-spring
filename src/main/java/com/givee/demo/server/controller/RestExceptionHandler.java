package com.givee.demo.server.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static com.givee.demo.server.controller.PGController.APPLICATION_JSON;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
	@ExceptionHandler(value = EntityNotFoundException.class)
	protected ResponseEntity<RestErrorMessage> handleNotFoundException(EntityNotFoundException ex) {
		HttpStatus status = HttpStatus.NOT_FOUND;
		return ResponseEntity.status(status).contentType(MediaType.parseMediaType(APPLICATION_JSON)).body(new RestErrorMessage(status, ex.getMessage()));
	}

	@ExceptionHandler(value = ForbiddenException.class)
	protected ResponseEntity<RestErrorMessage> handleForbiddenException(ForbiddenException ex) {
		HttpStatus status = HttpStatus.FORBIDDEN;
		return ResponseEntity.status(status).contentType(MediaType.parseMediaType(APPLICATION_JSON)).body(new RestErrorMessage(status, ex.getMessage()));
	}

	@ExceptionHandler(value = UnauthorizedException.class)
	protected ResponseEntity<RestErrorMessage> handleUnauthorizedException(UnauthorizedException ex) {
		HttpStatus status = HttpStatus.UNAUTHORIZED;
		return ResponseEntity.status(status).contentType(MediaType.parseMediaType(APPLICATION_JSON)).body(new RestErrorMessage(status, ex.getMessage()));
	}

	@ExceptionHandler(value = RequestParameterNotFoundException.class)
	protected ResponseEntity<RestErrorMessage> handleBadRequestException(RequestParameterNotFoundException ex) {
		HttpStatus status = HttpStatus.BAD_REQUEST;
		return ResponseEntity.status(status).contentType(MediaType.parseMediaType(APPLICATION_JSON)).body(new RestErrorMessage(status, ex.getMessage()));
	}

	@ExceptionHandler(value = InvalidParameterValueException.class)
	protected ResponseEntity<RestErrorMessage> handleBadRequestException(InvalidParameterValueException ex) {
		HttpStatus status = HttpStatus.BAD_REQUEST;
		return ResponseEntity.status(status).contentType(MediaType.parseMediaType(APPLICATION_JSON)).body(new RestErrorMessage(status, ex.getMessage()));
	}

	@ExceptionHandler(value = InvalidRequestFormatException.class)
	protected ResponseEntity<RestErrorMessage> handleBadRequestException(InvalidRequestFormatException ex) {
		HttpStatus status = HttpStatus.BAD_REQUEST;
		return ResponseEntity.status(status).contentType(MediaType.parseMediaType(APPLICATION_JSON)).body(new RestErrorMessage(status, ex.getMessage()));
	}

	@Override
	protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		return ResponseEntity.status(status).contentType(MediaType.parseMediaType(APPLICATION_JSON)).body(new RestErrorMessage(status, ex.getMessage()));
	}
}
