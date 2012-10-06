package se.exuvo.mmo.shared.connection;

public class OrderResponse {
	public String error = null;
	
	public OrderResponse(){
		
	}
	
	public OrderResponse(String s){
		error = s;
	}
}
