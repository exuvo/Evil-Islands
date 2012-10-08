package se.exuvo.evil.server.world.systems;

import se.exuvo.evil.server.world.components.Position;
import se.exuvo.evil.server.world.components.Velocity;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;

public class MovementSystem extends EntityProcessingSystem {
	@Mapper ComponentMapper<Position> pm;
	@Mapper ComponentMapper<Velocity> vm;

	@SuppressWarnings("unchecked")
	public MovementSystem() {
		super(Aspect.getAspectForAll(Position.class, Velocity.class));
	}

	protected void process(Entity e) {
		// Get the components from the entity using component mappers.
		Position p = pm.get(e);
		Velocity v = vm.get(e);

		// Update the position to the previous calculated position.
		if (!v.isColliding()) {
			p.set(v.getNext());
		}

		// Calculate next position.
		Position next = v.getNext();
		next.setX(p.getX() + v.getX() * world.getDelta());
		next.setY(p.getY() + v.getY() * world.getDelta());
		v.setNext(next);
	}

}
