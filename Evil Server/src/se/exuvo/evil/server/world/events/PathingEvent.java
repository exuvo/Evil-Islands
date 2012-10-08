package se.exuvo.evil.server.world.events;

import java.util.EventObject;

import se.exuvo.evil.server.world.Movable;

public class PathingEvent extends EventObject {
	public Movable mover;
	public boolean success;
	
	public PathingEvent(Object source, Movable mover, boolean success){
		super(source);
		this.mover = mover;
		this.success = success;
	}
}