package se.exuvo.mmo.server.world.o.events;

import java.util.EventObject;

import se.exuvo.mmo.server.world.Collidable;

public class CollisionEvent extends EventObject {
	private static final long serialVersionUID = -5385183878163086695L;
	public Collidable collidedWith;
	public Collidable collider; 
	
	public CollisionEvent(Object source, Collidable collider, Collidable collidedWith){
		super(source);
		this.collidedWith=collidedWith;
		this.collider=collider;
	}
}