package se.exuvo.evil.server.world.events;

import java.util.EventObject;

import se.exuvo.evil.server.world.Collidable;

public class CollisionEvent extends EventObject {
	public Collidable collidedWith;
	public Collidable collider; 
	
	public CollisionEvent(Object source, Collidable collider, Collidable collidedWith){
		super(source);
		this.collidedWith=collidedWith;
		this.collider=collider;
	}
}