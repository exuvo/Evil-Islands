package se.exuvo.evil.shared.connection;

import com.esotericsoftware.kryo.NotNull;

public class Message {
	/**
	 * Player who wrote the message
	 */
	public @NotNull String source;
	public long sourceId = -1;
	public @NotNull String message;
	
	@SuppressWarnings("unused")
	private Message(){
		
	}
	
	public Message(String source, long sourceId, String message){
		this.source = source;
		this.sourceId = sourceId;
		this.message = message;
	}
	
	public Message(String source, String message){
		this.source = source;
		this.message = message;
	}

}
