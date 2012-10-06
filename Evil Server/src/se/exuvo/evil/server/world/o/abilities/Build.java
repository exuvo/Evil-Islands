package se.exuvo.evil.server.world.o.abilities;

import se.exuvo.evil.server.world.Entity;
import se.exuvo.evil.server.world.o.units.buildings.Blueprint;
import se.exuvo.evil.server.world.o.units.buildings.Building;
import se.exuvo.evil.server.world.o.units.minions.Worker;
import se.exuvo.evil.shared.connection.InvalidOrderException;
import se.exuvo.evil.shared.world.Position;
import se.exuvo.evil.shared.world.NetAbility.validTarget;

public class Build extends Ability {
	
	public Build(){
		super("Build", new validTarget[]{validTarget.entity});
		setRange(10);
		this.getValidators().add(new Validator() {
			
			@Override
			public void validate(Entity caster, Position target) throws InvalidOrderException{
			}
			
			@Override
			public void validate(Entity caster, Entity target) throws InvalidOrderException{
				if(!(caster instanceof Worker)){
					throw new InvalidOrderException("Caster is not a worker");
				}
				if(!(target instanceof Blueprint)){
					throw new InvalidOrderException("Target is not a PlannedBuild");
				}
				Blueprint b = (Blueprint) target;
				if(!b.getBuilding().isBuildableAt(b.getPosition())){
					throw new InvalidOrderException("Unable to build that there");
				}
				if(b.isPaused()){
					throw new InvalidOrderException("That blueprint is paused");
				}
			}
		});
	}

	@Override
	public void cast(Entity caster, Entity target) {
		Blueprint p = (Blueprint) target;
		Building b = (Building) p.getBuilding().clone();
		b.newId();
		b.setOwner(caster.getOwnerID());
		b.setPosition(p.getPosition());
		b.setZone(p.getZone());
		p.getZone().addEntity(b);
		p.getZone().removeEntity(p);
		setCasted();
		log.debug("Built \"" + b.getName() + "\"");
	}

	@Override
	public void cast(Entity caster, Position target) {
		// TODO Auto-generated method stub

	}

}
