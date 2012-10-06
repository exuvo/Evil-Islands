package se.exuvo.evil.shared.connection;

import com.esotericsoftware.kryo.NotNull;

public class FilePart {
	@NotNull public byte[] bytes;

	@SuppressWarnings("unused")
	private FilePart(){
	}
	
	public FilePart(byte[] b){
		bytes = b;
	}
}
