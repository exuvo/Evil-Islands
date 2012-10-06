package se.exuvo.evil.server.world.o.events;

import java.util.EventObject;

import se.exuvo.evil.server.clients.Client;
import se.exuvo.evil.server.world.Movable;

public class ConnectEvent extends EventObject {
	private static final long serialVersionUID = -5485183878163086695L;
	public Client client;
	
	public ConnectEvent(Object source, Client client){
		super(source);
		this.client=client;
	}
}