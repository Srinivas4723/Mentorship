package com.pcs.controller;

import java.util.HashMap;
import java.util.Map;

import javax.validation.ConstraintViolationException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

public class BaseController {
	/**
	 * Handling Field Validations
	 * @param ex
	 * @return
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	Map<String, String> handleException(MethodArgumentNotValidException ex) {
		Map<String, String> errors = new HashMap<>();
		
		ex.getBindingResult().getAllErrors().forEach((error) -> {
			String fieldname = ((FieldError) error).getField();
			String message = ((FieldError) error).getDefaultMessage();
			errors.put(fieldname, message);
		});
		return errors;
	}
	/**
	 * Handling Request Body
	 * @param ex
	 * @return
	 */
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	@ExceptionHandler(HttpMessageNotReadableException.class)
	ResponseEntity<String> requestParamNotFound(HttpMessageNotReadableException ex) {
		return new ResponseEntity<>("Error : Request Body is missing ",HttpStatus.NOT_ACCEPTABLE);
	}
//	@ExceptionHandler(IllegalArgumentException.class)
//	ResponseEntity<String> requestParamNotFound(IllegalArgumentException ex) {
//		return new ResponseEntity<>("Invalid Search Item ",HttpStatus.NOT_ACCEPTABLE);
//	}
	@ExceptionHandler(IndexOutOfBoundsException.class)
	ResponseEntity<String> arrayOutofBounds(IndexOutOfBoundsException ex) {
		return new ResponseEntity<>("Invalid Index ",HttpStatus.NOT_ACCEPTABLE);
	}

}