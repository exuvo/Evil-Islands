package se.exuvo.evil.server.world.o.events;

import java.util.EventObject;

import se.exuvo.evil.server.world.Movable;

public class MoveEvent extends EventObject {
	private static final long serialVersionUID = -5485183878163086695L;
	public Movable mover;
	
	public MoveEvent(Object source, Movable mover){
		super(source);
		this.mover=mover;
	}
}