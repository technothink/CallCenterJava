package com.technothink.speech.exception;

public class TranscribeException extends Throwable {



	/**
	 * 
	 */
	private static final long serialVersionUID = -2415670610943253962L;

	public TranscribeException(String message) {
		super(message);
	}

	

	public TranscribeException(String message,Throwable e) {
		super(message, e);
	}



	public TranscribeException(Exception e) {
		// TODO Auto-generated constructor stub
	}

}
