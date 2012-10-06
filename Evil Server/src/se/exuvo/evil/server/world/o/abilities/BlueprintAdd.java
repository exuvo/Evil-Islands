package se.exuvo.evil.server.world.o.abilities;

import se.exuvo.evil.server.world.Entity;
import se.exuvo.evil.server.world.Movable;
import se.exuvo.evil.server.world.o.units.buildings.Blueprint;
import se.exuvo.evil.server.world.o.units.buildings.Building;
import se.exuvo.evil.server.world.o.units.minions.Worker;
import se.exuvo.evil.shared.connection.InvalidOrderException;
import se.exuvo.evil.shared.world.NetAbility;
import se.exuvo.evil.shared.world.NetBlueprintAdd;
import se.exuvo.evil.shared.world.Position;
import se.exuvo.evil.shared.world.NetAbility.validTarget;

public class BlueprintAdd extends Ability {
	public Building builds = null;
	
	
	/**
	 * Create new() and change <builds> to suit needs.
	 */
	public BlueprintAdd(Building b){
		super("Blueprint add " + b.getName(), new validTarget[]{validTarget.point,validTarget.entity});
		builds = b;
		this.getValidators().add(new Validator() {
			@Override
			public void validate(Entity caster, Position target) throws InvalidOrderException{
				if(builds == null){
					throw new InvalidOrderException("builds is null");
				}
				builds.setZone(caster.getZone());
				if(!builds.isBuildableAt(target)){
					throw new InvalidOrderException("Unable to build that there");
				}
				Blueprint p = new Blueprint(builds);
				p.setZone(caster.getZone());
				if(p.isCollidingWithCollidableAtPos(target) != null){
					throw new InvalidOrderException("Unable to build there, blueprint in the way");
				}
			}
			
			@Override
			public void validate(Entity caster, Entity target) throws InvalidOrderException{
				validate(caster, target.getPosition());
			}
		});
	}
	
	@Override
	public String getId() {
		return builds == null ?	this.getClass().getSimpleName():
								this.getClass().getSimpleName()+builds.getClass().getSimpleName();
	}

	@Override
	public void cast(Entity caster, Entity target) {
		cast(caster, target.getPosition());
	}

	@Override
	public void cast(Entity caster, Position target) {
		Blueprint b = new Blueprint(builds);
		b.setPosition(target);
		b.setOwner(caster.getOwnerID());
		caster.getZone().addEntity(b);
		setCasted();
	}
	
	@Override
	public NetBlueprintAdd getNet(){
		NetBlueprintAdd a = (NetBlueprintAdd) super.getNet(new NetBlueprintAdd());
		a.setCost(0);
		a.setShape(builds.getShape());
		a.setBuildingName(builds.getName());
		return a;
	}

}
