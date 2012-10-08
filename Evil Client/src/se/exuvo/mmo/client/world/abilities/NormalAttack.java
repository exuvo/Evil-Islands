package se.exuvo.mmo.client.world.abilities;

import se.exuvo.mmo.client.world.Entity;
import se.exuvo.mmo.client.world.Movable;
import se.exuvo.mmo.client.world.Unit;
import se.exuvo.mmo.shared.connection.InvalidOrderException;
import se.exuvo.mmo.shared.world.NetAbility;
import se.exuvo.mmo.shared.world.Position;
 
public class NormalAttack extends Ability {
	
	public NormalAttack(NetAbility a){
		super(a);
		
		this.getValidators().add(new Validator() {
			
			@Override
			public void validate(Entity caster, Position target) throws InvalidOrderException {
				throw new InvalidOrderException("Unable to use on Positions");
			}
			
			@Override
			public void validate(Entity caster, Entity target) throws InvalidOrderException {
				if(!(caster instanceof Unit)){
					 throw new InvalidOrderException("Caster is not a Unit");
				 }
				 if(!target.isAlive()){
					 throw new InvalidOrderException("Unable to attack, target is already dead");
				 }
			}
		});
		
	}

	@Override
	public String getName() {
		return "NormalAttack";
	}

	@Override
	public String getDescription() {
		return "Orders a unit to attack another Entity";
	}

}
