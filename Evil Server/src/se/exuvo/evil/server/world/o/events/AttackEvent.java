package se.exuvo.evil.server.world.o.events;

import java.util.EventObject;

import se.exuvo.evil.server.world.Collidable;
import se.exuvo.evil.server.world.Movable;
import se.exuvo.evil.server.world.Unit;

public class AttackEvent extends EventObject {
	private static final long serialVersionUID = -5485183878163086695L;
	public Unit attacker;
	public Unit attacked;
	
	public AttackEvent(Object source, Unit attacker, Unit attacked){
		super(source);
		this.attacker=attacker;
		this.attacked=attacked;
	}
}