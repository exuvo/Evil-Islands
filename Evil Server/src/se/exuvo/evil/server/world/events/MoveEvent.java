package se.exuvo.evil.server.world.events;

import java.util.EventObject;

import se.exuvo.evil.server.world.Movable;

public class MoveEvent extends EventObject {
	public Movable mover;
	
	public MoveEvent(Object source, Movable mover){
		super(source);
		this.mover=mover;
	}
}