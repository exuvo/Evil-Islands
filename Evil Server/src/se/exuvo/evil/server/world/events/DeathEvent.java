package se.exuvo.evil.server.world.events;

import java.util.EventObject;

import se.exuvo.evil.server.world.Entity;

public class DeathEvent extends EventObject {
	public Entity dying;
	public Entity killer; 
	
	public DeathEvent(Object source, Entity dying, Entity killer){
		super(source);
		this.dying=dying;
		this.killer=killer;
	}
}