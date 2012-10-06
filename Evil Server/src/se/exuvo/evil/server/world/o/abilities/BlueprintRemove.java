package se.exuvo.evil.server.world.o.abilities;

import se.exuvo.evil.server.world.Entity;
import se.exuvo.evil.server.world.Movable;
import se.exuvo.evil.server.world.o.units.buildings.Blueprint;
import se.exuvo.evil.server.world.o.units.buildings.Building;
import se.exuvo.evil.server.world.o.units.minions.Worker;
import se.exuvo.evil.shared.connection.InvalidOrderException;
import se.exuvo.evil.shared.world.Position;
import se.exuvo.evil.shared.world.NetAbility.validTarget;

public class BlueprintRemove extends Ability {
	
	public BlueprintRemove(){
		super("Blueprint Remove", new validTarget[]{validTarget.self});
		this.getValidators().add(new Validator() {
			
			@Override
			public void validate(Entity caster, Position target) throws InvalidOrderException{
			}
			
			@Override
			public void validate(Entity caster, Entity target) throws InvalidOrderException{
				if(!(target instanceof Blueprint)){
					throw new InvalidOrderException("Target is not a Blueprint");
				}
				Blueprint p = (Blueprint) target;
				if(!p.isPaused()){
					throw new InvalidOrderException("Blueprint is not paused");
				}
			}
		});
	}

	@Override
	public void cast(Entity caster, Entity target) {
		target.getZone().removeEntity(target);
		setCasted();
	}

	@Override
	public void cast(Entity caster, Position target) {
		// TODO Auto-generated method stub

	}

}
