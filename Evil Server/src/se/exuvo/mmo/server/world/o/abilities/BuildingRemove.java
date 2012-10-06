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

public class BuildingRemove extends Ability {
	
	public BuildingRemove(){
		super("Blueprint Remove", new validTarget[]{validTarget.self});
		this.getValidators().add(new Validator() {
			
			@Override
			public void validate(Entity caster, Position target) throws InvalidOrderException{
			}
			
			@Override
			public void validate(Entity caster, Entity target) throws InvalidOrderException{
				if(!(target instanceof Building)){
					throw new InvalidOrderException("Target is not a Building");
				}
				Building p = (Building) target;
				if(!p.isAlive()){
					throw new InvalidOrderException("Building is dead");
				}
			}
		});
	}

	@Override
	public void cast(Entity caster, Entity target) {
		target.getZone().removeEntity(target);
		setCasted();
		
		Blueprint b = (Blueprint) target;
		b.setPaused(false);
		Genius g = (Genius) caster;
		g.addTask(new Order(new Build(), target, null));
		setCasted();
	}

	@Override
	public void cast(Entity caster, Position target) {
		// TODO Auto-generated method stub

	}

}
