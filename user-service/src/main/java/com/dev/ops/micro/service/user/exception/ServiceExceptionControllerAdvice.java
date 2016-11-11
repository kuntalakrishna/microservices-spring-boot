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

@ControllerAdvice
public class ServiceExceptionControllerAdvice extends ResponseEntityExceptionHandler {

	private static final Logger LOGGER = LogManager.getLogger(ServiceExceptionControllerAdvice.class);

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
		return this.handleExceptionInternal(e, validationErrors.toString(), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
	}

	/**
	 * Handle all the service exceptions.
	 * @param ex the exception
	 * @param request the request
	 * @return the response entity
	 */
	@ExceptionHandler(Exception.class)
	protected ResponseEntity<Object> handleServiceException(Exception ex, WebRequest request) {
		LOGGER.error("Exception", ex);
		return this.handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
	}
}