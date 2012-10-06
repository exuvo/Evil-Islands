package se.exuvo.mmo.shared.connection;

public class LoginResponse {
	public static enum Cause {Credentials, Version, Banned};
	public Cause error = null;
	public String text = null;
	public int serverRMIObjectSpaceID;
	
	public LoginResponse(){
		
	}
	
	public LoginResponse(int id){
		serverRMIObjectSpaceID = id;
	}
	
	public LoginResponse(String s, Cause c){
		error = c;
		text = s;
	}
}
