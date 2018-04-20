package com.example.demo.exception;

public class MurdererException extends Exception {
	
	public MurdererException() {
		super("If you're going to bother a murderer, be prepared to watch your back for the rest of your life.");
	}
	
	public MurdererException(String message) {
		super(message);
	}
}