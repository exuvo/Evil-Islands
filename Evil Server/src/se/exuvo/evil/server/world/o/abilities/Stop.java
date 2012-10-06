package se.exuvo.evil.server.world.o.abilities;

import se.exuvo.evil.server.world.Entity;
import se.exuvo.evil.server.world.Movable;
import se.exuvo.evil.server.world.Unit;
import se.exuvo.evil.shared.connection.InvalidOrderException;
import se.exuvo.evil.shared.world.Position;
import se.exuvo.evil.shared.world.NetAbility.validTarget;

public class Stop extends Ability {
	public Stop(){
		super("Stop", new validTarget[]{validTarget.self});
		this.getValidators().add(new Validator() {
			
			@Override
			public void validate(Entity caster, Position Target) throws InvalidOrderException{
			}
			
			@Override
			public void validate(Entity caster, Entity Target)  throws InvalidOrderException{
				if(!(caster instanceof Movable)){
					throw new InvalidOrderException("Caster is not a Movable");
				}
			}
		});
	}
	
	@Override
	public void cast(Entity caster, Entity target) {
		Movable mov = (Movable) caster;
		mov.setSpeed(0);
		mov.setTarget(null);
		mov.setPath(null);
		mov.setTargetEntity(null);
		if(caster instanceof Unit){
			Unit u = (Unit) caster;
			u.getQueue().clear();
		}
		
		setCasted();
	}

	@Override
	public void cast(Entity caster, Position target) {
	}
}