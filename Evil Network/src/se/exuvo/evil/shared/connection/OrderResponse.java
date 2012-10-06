package se.exuvo.evil.shared.connection;

public class OrderResponse {
	public String error = null;
	
	public OrderResponse(){
		
	}
	
	public OrderResponse(String s){
		error = s;
	}
}
