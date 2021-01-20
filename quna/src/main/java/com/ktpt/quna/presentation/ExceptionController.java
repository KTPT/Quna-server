package com.ktpt.quna.presentation;

import com.ktpt.quna.application.exception.ErrorResponse;
import com.ktpt.quna.application.exception.InvalidTokenException;
import com.ktpt.quna.application.exception.NotFoundException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.ktpt.quna.application.exception.ErrorResponse;
import com.ktpt.quna.application.exception.NotFoundException;
import com.ktpt.quna.presentation.verifier.InvalidRequestException;

@ControllerAdvice
public class ExceptionController {
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handle(MethodArgumentNotValidException e) {
		ErrorResponse response = new ErrorResponse(e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
		return ResponseEntity.badRequest().body(response);
	}

	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<ErrorResponse> handle(NotFoundException e) {
		ErrorResponse response = new ErrorResponse(e.getMessage());
		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(InvalidTokenException.class)
	public ResponseEntity<ErrorResponse> handle(InvalidTokenException e) {
		ErrorResponse response = new ErrorResponse(e.getMessage());
		return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler({IllegalArgumentException.class, InvalidRequestException.class})
	public ResponseEntity<ErrorResponse> handle(IllegalArgumentException e) {
		ErrorResponse response = new ErrorResponse(e.getMessage());
		return ResponseEntity.badRequest().body(response);
	}

	@ExceptionHandler(EmptyResultDataAccessException.class)
	public ResponseEntity<ErrorResponse> handle(EmptyResultDataAccessException e) {
		ErrorResponse response = new ErrorResponse(e.getMessage());
		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}
}
