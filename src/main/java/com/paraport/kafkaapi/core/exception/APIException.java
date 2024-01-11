package com.paraport.kafkaapi.core.exception;


import com.paraport.kafkaapi.core.model.Error;

public class APIException extends RuntimeException {
	private static final long serialVersionUID = 4377367741748674369L;
	private static final String INTERNAL_SERVER_ERROR = "Internal Server Error";
	private static final Integer INTERNAL_SERVER_ERROR_CODE = 500;
	private static final Integer BAD_REQUEST_CODE = 400;
	private static final Integer NOT_FOUND_CODE = 404;
	protected Error error;

	public APIException(String message) {
		super(message);
	}

	public static APIException badRequestException(String message) {
		APIException apiException = new APIException(message);
		apiException.setError(BAD_REQUEST_CODE);
		return apiException;
	}

	public static APIException notFoundException(String message) {
		APIException apiException = new APIException(message);
		apiException.setError(NOT_FOUND_CODE);
		return apiException;
	}


public static APIException serverException() {
APIException apiException = new APIException (INTERNAL_SERVER_ERROR);
apiException.setError (INTERNAL_SERVER_ERROR_CODE);
return apiException;
}

public static APIException serverException(String message) {
APIException apiException = new APIException (message);
apiException.setError (INTERNAL_SERVER_ERROR_CODE);
return apiException;
}

protected void setError(Integer code) {
error = new Error(code, getMessage());
}

public Error getError() {
return error;

}
}