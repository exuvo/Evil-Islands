package se.exuvo.evil.server.world.o.abilities;

import se.exuvo.evil.server.world.Entity;
import se.exuvo.evil.server.world.o.units.buildings.Door;
import se.exuvo.evil.shared.connection.InvalidOrderException;
import se.exuvo.evil.shared.world.Position;
import se.exuvo.evil.shared.world.NetAbility.validTarget;

public class Lock extends Ability {
	private final int level;
	
	public Lock(int lockLevel){
		super("Lock", new validTarget[]{validTarget.self});
		level = lockLevel;
		if(level == Door.OPEN){
			setName("*");
		}else if(level == Door.CLOSED){
			setName("**");
		}else if(level == Door.LOCKED){
			setName("***");
		}else if(level == Door.GUARDED){
			setName("****");
		}
		
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
		d.setLockLevel(level);
		setCasted();
	}

	@Override
	public void cast(Entity caster, Position target) {
	}
}