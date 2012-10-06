package se.exuvo.evil.server.world.o.abilities;

import se.exuvo.evil.server.world.Entity;
import se.exuvo.evil.server.world.Movable;
import se.exuvo.evil.server.world.Unit;
import se.exuvo.evil.server.world.o.Order;
import se.exuvo.evil.shared.connection.InvalidOrderException;
import se.exuvo.evil.shared.world.Position;
import se.exuvo.evil.shared.world.NetAbility.validTarget;

public class NormalAttack extends Ability {
	public NormalAttack(){
		super("Normal Attack", new validTarget[]{validTarget.entity});
		this.getValidators().add(new Validator() {
			
			@Override
			public void validate(Entity caster, Position target) throws InvalidOrderException{
			}
			
			@Override
			public void validate(Entity caster, Entity target) throws InvalidOrderException{
				if(! (caster instanceof Unit)){
					throw new InvalidOrderException("Caster is not a Unit");
				}
				if(!target.isAlive()){
					throw new InvalidOrderException("Unable to attack, target is already dead");
				}
			}
		});
		
		setRange(100);
	}

	@Override
	public void cast(Entity caster, Entity target) {
		target.damage(1, caster); //TODO check invulnerability
		if(target.isAlive()){//Attack until dead
			Unit u = (Unit) caster;
			u.insertOrder(0, new Order(u.getAttack(), target, caster));
		}
		setCasted();
	}

	@Override
	public void cast(Entity caster, Position target) {
		//TODO attack move
	}
}