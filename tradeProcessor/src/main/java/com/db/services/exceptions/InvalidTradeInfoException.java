package com.db.services.exceptions;

public class InvalidTradeInfoException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidTradeInfoException() {
		
	}

	public InvalidTradeInfoException(String errorMessage) {
		super(errorMessage);
	}

}
