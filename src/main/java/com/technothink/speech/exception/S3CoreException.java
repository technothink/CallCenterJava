package com.technothink.speech.exception;

public class S3CoreException extends Exception {



	/**
	 * 
	 */
	private static final long serialVersionUID = -2415670610943253962L;

	public S3CoreException(String message) {
		super(message);
	}

	

	public S3CoreException(String message,Exception e) {
		super(message, e);
	}

}
