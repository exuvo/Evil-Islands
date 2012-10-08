package se.exuvo.mmo.client.world.abilities;

import se.exuvo.mmo.client.world.Entity;
import se.exuvo.mmo.client.world.Movable;
import se.exuvo.mmo.shared.connection.InvalidOrderException;
import se.exuvo.mmo.shared.world.NetAbility;
import se.exuvo.mmo.shared.world.Position;
 
public class Move extends Ability {
	
	public Move(NetAbility a){
		super(a);
		
		this.getValidators().add(new Validator() {
			
			@Override
			public void validate(Entity caster, Position target) throws InvalidOrderException {
				if(!(caster instanceof Movable)){
					 throw new InvalidOrderException("Caster is not a Movable");
				 }
			}
			
			@Override
			public void validate(Entity caster, Entity target) throws InvalidOrderException {
				if(!(caster instanceof Movable)){
					 throw new InvalidOrderException("Caster is not a Movable");
				 }
			}
		});
		
	}

	@Override
	public String getName() {
		return "Move";
	}

	@Override
	public String getDescription() {
		return "Orders a unit to change its location";
	}

}
