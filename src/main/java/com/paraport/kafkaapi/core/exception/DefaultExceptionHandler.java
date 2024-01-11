package com.paraport.kafkaapi.core.exception;

import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import com.paraport.kafkaapi.core.model.Error;
//import lombok.extern.slf4j.slf4j;

//@s1f41
@ControllerAdvice 

public class DefaultExceptionHandler extends ResponseEntityExceptionHandler
{

private static final String DEFAULT_ERROR_MSG = "Internal Server Error";
private static final Integer DEFAULT_ERROR_CODE = 500;

@Override
protected ResponseEntity<Object> handleHttpMediaTypeNotSupported (HttpMediaTypeNotSupportedException ex,
		HttpHeaders headers, HttpStatus status, WebRequest request)
 {
	
return errorResponse(status.value(), ex.getLocalizedMessage(), status);
 }

@Override
protected ResponseEntity<Object> handleHttpRequestMethodNotSupported (HttpRequestMethodNotSupportedException ex,
		HttpHeaders headers, HttpStatus status, WebRequest request)

	 {
return errorResponse(status.value(), ex.getLocalizedMessage(), status);
	 }
@ExceptionHandler (value = { APIException.class})
public ResponseEntity<Object> handlekafkaApiException(APIException ex, WebRequest request) {

	
Error errorResponse = ex.getError();
return errorResponse(errorResponse);
	
}

@ExceptionHandler(value = { Exception.class})
public ResponseEntity<Object> handleException (Exception ex) {
//log.error(ex.getLocalizedMessage(), ex);
return errorResponse();
}

private ResponseEntity <Object> errorResponse (Error errorResponse) {
if(errorResponse == null) {
return errorResponse();
}
Integer code= errorResponse.getCode() != null? errorResponse.getCode(): DEFAULT_ERROR_CODE;
String message= errorResponse.getMessage() != null? errorResponse.getMessage(): DEFAULT_ERROR_MSG;
HttpStatus status=HttpStatus.valueOf(code);

return errorResponse(code, message, status);
}
private ResponseEntity<Object> errorResponse() {
return errorResponse(DEFAULT_ERROR_CODE, DEFAULT_ERROR_MSG, HttpStatus.INTERNAL_SERVER_ERROR);
}
private ResponseEntity<Object> errorResponse(Integer code, String message, HttpStatus status) {
Error errorResponse =new Error (code, message);
return new ResponseEntity<Object> (errorResponse, status);
}



	}
