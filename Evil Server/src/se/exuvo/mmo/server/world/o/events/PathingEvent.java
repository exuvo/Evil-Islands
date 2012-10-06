package se.exuvo.mmo.server.world.o.events;

import java.util.EventObject;

import se.exuvo.mmo.server.world.Movable;

public class PathingEvent extends EventObject {
	private static final long serialVersionUID = -5485183878163086695L;
	public Movable mover;
	public boolean success;
	
	public PathingEvent(Object source, Movable mover, boolean success){
		super(source);
		this.mover = mover;
		this.success = success;
	}
}