package se.exuvo.evil.server.world.events;

import java.util.EventObject;

import se.exuvo.evil.server.world.Collidable;
import se.exuvo.evil.server.world.Movable;
import se.exuvo.evil.server.world.Unit;

public class AttackEvent extends EventObject {
	public Unit attacker;
	public Unit attacked;
	
	public AttackEvent(Object source, Unit attacker, Unit attacked){
		super(source);
		this.attacker=attacker;
		this.attacked=attacked;
	}
}