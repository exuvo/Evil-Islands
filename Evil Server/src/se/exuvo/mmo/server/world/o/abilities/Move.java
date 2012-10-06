package se.exuvo.mmo.server.world.o.abilities;

import java.util.EventObject;

import se.exuvo.mmo.server.world.Entity;
import se.exuvo.mmo.server.world.Movable;
import se.exuvo.mmo.shared.connection.InvalidOrderException;
import se.exuvo.mmo.shared.world.EventListenerr;
import se.exuvo.mmo.shared.world.Position;
import se.exuvo.mmo.shared.world.NetAbility.validTarget;
import se.exuvo.mmo.server.world.o.events.*;

public class Move extends Ability implements EventListenerr{
	
	public Move(){
		super("Move", new validTarget[]{validTarget.point,validTarget.entity});
		
		this.getValidators().add(new Validator() {
			
			@Override
			public void validate(Entity caster, Position target) throws InvalidOrderException{
				 if(! (caster instanceof Movable)){
					 throw new InvalidOrderException("Caster is not Movable");
				 }
				 if(!(((Movable) caster).findPath(caster.getPosition(), target, false) != null)){
					 throw new InvalidOrderException("Caster cannot find a path to the target point");
				 }
			}
			
			@Override
			public void validate(Entity caster, Entity target) throws InvalidOrderException{
				if(! (caster instanceof Movable)){
					 throw new InvalidOrderException("Caster is not Movable");
				 }
				 if(!(((Movable) caster).findPath(caster.getPosition(), target.getPosition(), false) != null)){
					 throw new InvalidOrderException("Caster cannot find a path to the target");
				 }
			}
		});
	}

	@Override
	public void cast(final Entity caster, Entity target) {
		//TODO follow
		Movable caste = (Movable) caster;
		caste.addEventListener(this, PathingEvent.class);
		caste.resetFail();
		boolean worked = caste.goTo(target);
		if(!worked)setCasted();
		log.trace("gotoE " + worked);
	}

	@Override
	public void cast(final Entity caster, Position target) {
		Movable caste = (Movable) caster;
		caste.addEventListener(this, PathingEvent.class);
		caste.resetFail();
		boolean worked = caste.goTo(target);
		if(!worked)setCasted();
		log.trace("gotoP " + worked);
	}
	
	@Override
	public void event(EventObject e) {
		PathingEvent p = (PathingEvent) e;
		p.mover.getEventQueue(PathingEvent.class).removeEventListener(this);
		setCasted();
	}
	
}