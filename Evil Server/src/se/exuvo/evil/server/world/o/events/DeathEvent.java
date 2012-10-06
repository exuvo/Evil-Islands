package se.exuvo.evil.server.world.o.events;

import java.util.EventObject;

import se.exuvo.evil.server.world.Entity;

public class DeathEvent extends EventObject {
	private static final long serialVersionUID = -5385183878163086695L;
	public Entity dying;
	public Entity killer; 
	
	public DeathEvent(Object source, Entity dying, Entity killer){
		super(source);
		this.dying=dying;
		this.killer=killer;
	}
}