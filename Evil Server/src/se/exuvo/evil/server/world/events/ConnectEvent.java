package se.exuvo.evil.server.world.events;

import java.util.EventObject;

import se.exuvo.evil.server.clients.Client;
import se.exuvo.evil.server.world.Movable;

public class ConnectEvent extends EventObject {
	public Client client;
	
	public ConnectEvent(Object source, Client client){
		super(source);
		this.client=client;
	}
}