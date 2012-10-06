package se.exuvo.evil.shared.world;

public class NetZonePart {
	public byte[] bytes = null;//if size == null then last part
	

	public NetZonePart(){
	}
	
	public NetZonePart(byte[] b){
		bytes = b;
	}
}
