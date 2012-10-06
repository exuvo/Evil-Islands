package se.exuvo.mmo.shared.connection;

import java.io.Serializable;

public class InvalidOrderException extends Exception implements Serializable{
	private static final long serialVersionUID = 3539537492838628134L;

	public InvalidOrderException(){
		super();
	}
	
	public InvalidOrderException(Throwable t){
		super(t);
	}
	
	public InvalidOrderException(String s){
		super(s);
	}
	
	public InvalidOrderException(String s, Throwable t){
		super(s,t);
	}

}
