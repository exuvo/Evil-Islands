package se.exuvo.evil.server.world.o.abilities;

import se.exuvo.evil.server.world.Entity;
import se.exuvo.evil.server.world.Movable;
import se.exuvo.evil.server.world.o.Order;
import se.exuvo.evil.server.world.o.units.Genius;
import se.exuvo.evil.server.world.o.units.buildings.Blueprint;
import se.exuvo.evil.server.world.o.units.buildings.Building;
import se.exuvo.evil.server.world.o.units.minions.Worker;
import se.exuvo.evil.shared.connection.InvalidOrderException;
import se.exuvo.evil.shared.world.Position;
import se.exuvo.evil.shared.world.NetAbility.validTarget;

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
