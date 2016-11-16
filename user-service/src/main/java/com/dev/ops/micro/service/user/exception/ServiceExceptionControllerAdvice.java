/*
 *
 * Copyright (C) 2016 Krishna Kuntala @ Mastek <krishna.kuntala@mastek.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.dev.ops.micro.service.user.exception;

import java.util.List;

import javax.validation.ValidationException;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * The class Service Exception Controller Advice will give the last opportunity to catch the exceptions arising from the service.
 * The exception logging is taken care in this class and this class is also responsible for deciding what kind of HTTP error to be thrown out of the service.
 */
@ControllerAdvice
public class ServiceExceptionControllerAdvice extends ResponseEntityExceptionHandler {

	/** The LOGGER. */
	private static final Logger LOGGER = LogManager.getLogger(ServiceExceptionControllerAdvice.class);

	/**
	 * This is an overriden method which is responsible to handle validation exceptions.
	 * The method iterates through the validation errors and constructs the message to be sent out of the services with validation errors.
	 * Also, logs the error message to the log files which could help in analysis.
	 *
	 * @param ex the MethodArgumentNotValid Exception
	 * @param headers the HTTP headers object
	 * @param status the HTTP status object
	 * @param request the WebRequest object containing the request details
	 * @return the response entity containing the error message and HttpStatus
	 */
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		BindingResult result = ex.getBindingResult();
		List<FieldError> fieldErrors = result.getFieldErrors();
		StringBuilder validationErrors = new StringBuilder();
		for(FieldError fieldError : fieldErrors) {
			validationErrors.append("Field error in object '" + fieldError.getObjectName() + "' on field '" + fieldError.getField() + "': rejected value [" + fieldError.getRejectedValue() + "] with cause: [" + fieldError.getDefaultMessage() + " ]");
		}
		Exception e = new ValidationException(validationErrors.toString());
		LOGGER.error("Validation exception", e);
		return this.handleExceptionInternal(e, validationErrors.toString(), headers, status, request);
	}

	/**
	 * Handle all the service exceptions and log the error message to the log files which could help in analysis.
	 * @param ex the exception object
	 * @param request the WebRequest object containing the request details
	 * @return the response entity containing the error message and HttpStatus
	 */
	@ExceptionHandler(Exception.class)
	protected ResponseEntity<Object> handleServiceException(Exception ex, WebRequest request) {
		LOGGER.error("Exception", ex);
		return this.handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
	}
}