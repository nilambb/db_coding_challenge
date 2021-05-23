package com.db.services.exceptions;

public class TradeInfoNotFoundException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TradeInfoNotFoundException() {
		
	}
	
	public TradeInfoNotFoundException(String error) {
		super(error);
	}
}
