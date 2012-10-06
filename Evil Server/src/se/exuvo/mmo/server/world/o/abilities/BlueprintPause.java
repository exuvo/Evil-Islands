package se.exuvo.mmo.server.world.o.abilities;

import se.exuvo.mmo.server.world.Entity;
import se.exuvo.mmo.server.world.Movable;
import se.exuvo.mmo.shared.connection.InvalidOrderException;
import se.exuvo.mmo.shared.world.Position;
import se.exuvo.mmo.shared.world.NetAbility.validTarget;
import se.exuvo.mmo.server.world.o.Order;
import se.exuvo.mmo.server.world.o.units.Genius;
import se.exuvo.mmo.server.world.o.units.buildings.Blueprint;
import se.exuvo.mmo.server.world.o.units.buildings.Building;
import se.exuvo.mmo.server.world.o.units.minions.Worker;

public class BlueprintPause extends Ability {
	
	public BlueprintPause(){
		super("Blueprint Pause", new validTarget[]{validTarget.self});
		this.getValidators().add(new Validator() {
			
			@Override
			public void validate(Entity caster, Position target) throws InvalidOrderException{
			}
			
			@Override
			public void validate(Entity caster, Entity target) throws InvalidOrderException{
				if(!(target instanceof Blueprint)){
					throw new InvalidOrderException("Target is not a Blueprint");
				}
			}
		});
	}

	@Override
	public void cast(Entity caster, Entity target) {
		Blueprint b = (Blueprint) target;
		b.setPaused(true);
		Genius g = caster.getOwner().getGenius();
		g.removeTask(new Order(new Build(), target, null));
		setCasted();
	}

	@Override
	public void cast(Entity caster, Position target) {
		// TODO Auto-generated method stub

	}

}
