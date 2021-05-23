package com.db.services.beans;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ErrorResponse {

	private ResonseCode resonseCode;

	private String responseDescription;

	public ErrorResponse(ResonseCode resonseCode, String responseDescription) {
		this.resonseCode = resonseCode;
		this.responseDescription = responseDescription;
	}
}
