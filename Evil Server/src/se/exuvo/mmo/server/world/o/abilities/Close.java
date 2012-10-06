package se.exuvo.mmo.server.world.o.abilities;

import se.exuvo.mmo.server.world.Entity;
import se.exuvo.mmo.server.world.Movable;
import se.exuvo.mmo.server.world.o.collision.BuildingCollision;
import se.exuvo.mmo.server.world.o.collision.NoCollision;
import se.exuvo.mmo.server.world.o.units.buildings.Door;
import se.exuvo.mmo.shared.connection.InvalidOrderException;
import se.exuvo.mmo.shared.world.Position;
import se.exuvo.mmo.shared.world.NetAbility.validTarget;

public class Close extends Ability {
	public Close(){
		super("Close", new validTarget[]{validTarget.self});
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
		//d.setLockLevel(Door.CLOSED);
		d.setCollisionType(new BuildingCollision());
		setCasted();
	}

	@Override
	public void cast(Entity caster, Position target) {
	}
}