package com.db.services.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.db.services.beans.ErrorResponse;
import com.db.services.beans.ResonseCode;

@ControllerAdvice
public class TradeProcessorExceptionController {

	private static final Logger log = LoggerFactory.getLogger(TradeProcessorExceptionController.class);

	@ExceptionHandler(value = TradeInfoNotFoundException.class)
	public ResponseEntity<ErrorResponse> notFoundException(TradeInfoNotFoundException exception) {
		log.error("An error occurred while processing the request {}", exception);
		ErrorResponse response = new ErrorResponse(ResonseCode.NOT_FOUND,
				"Trade Info not found. " + exception.getMessage());
		return new ResponseEntity<ErrorResponse>(response, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(value = InvalidTradeInfoException.class)
	public ResponseEntity<ErrorResponse> InvalidRequestExcpeiton(InvalidTradeInfoException exception) {
		log.error("An error occurred while processing the request {}", exception);
		ErrorResponse response = new ErrorResponse(ResonseCode.BAD_REQUEST,
				"InValid request. " + exception.getMessage());
		return new ResponseEntity<ErrorResponse>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = Exception.class)
	public ResponseEntity<ErrorResponse> exception(Exception exception) {
		log.error("An error occurred while processing the request {}", exception);
		ErrorResponse response = new ErrorResponse(ResonseCode.INTERNAL_ERROR,
				"An error occurred while processing the request. Please contact with the concern team for more details on the same");
		return new ResponseEntity<ErrorResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
