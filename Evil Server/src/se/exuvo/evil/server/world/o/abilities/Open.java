package se.exuvo.evil.server.world.o.abilities;

import se.exuvo.evil.server.world.Entity;
import se.exuvo.evil.server.world.o.collision.NoCollision;
import se.exuvo.evil.server.world.o.units.buildings.Door;
import se.exuvo.evil.shared.connection.InvalidOrderException;
import se.exuvo.evil.shared.world.Position;
import se.exuvo.evil.shared.world.NetAbility.validTarget;

public class Open extends Ability {
	public Open(){
		super("Open", new validTarget[]{validTarget.self});
		this.getValidators().add(new Validator() {
			
			@Override
			public void validate(Entity caster, Position Target) throws InvalidOrderException{
			}
			
			@Override
			public void validate(Entity caster, Entity Target)  throws InvalidOrderException{
				if(!(caster instanceof Door)){
					throw new InvalidOrderException("Caster is not a Door");
				}
			}
		});
	}
	
	@Override
	public void cast(Entity caster, Entity target) {
		Door d = (Door) caster;
		//d.setLockLevel(Door.OPEN);
		d.setCollisionType(new NoCollision());
		setCasted();
	}

	@Override
	public void cast(Entity caster, Position target) {
	}
}