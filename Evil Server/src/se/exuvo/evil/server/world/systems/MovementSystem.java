package se.exuvo.evil.server.world.systems;

import se.exuvo.evil.server.world.components.Position;
import se.exuvo.evil.server.world.components.Velocity;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.IntervalEntityProcessingSystem;

public class MovementSystem extends IntervalEntityProcessingSystem {
	@Mapper ComponentMapper<Position> pm;
	@Mapper ComponentMapper<Velocity> vm;

	@SuppressWarnings("unchecked")
	public MovementSystem() {
		super(Aspect.getAspectForAll(Position.class, Velocity.class), 0.1f);
	}

	protected void process(Entity e) {
		// Get the components from the entity using component mappers.
		Position p = pm.get(e);
		Velocity v = vm.get(e);
		
		// Update the position.
		p.set(p.getX() + v.getX() * world.getDelta(), p.getY() + v.getY() * world.getDelta());
	}

}
