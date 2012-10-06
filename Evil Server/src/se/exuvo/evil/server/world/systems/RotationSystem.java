package se.exuvo.evil.server.world.systems;

import se.exuvo.evil.server.world.components.Rotation;
import se.exuvo.evil.server.world.components.Velocity;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;

public class RotationSystem extends EntityProcessingSystem {
	@Mapper ComponentMapper<Rotation> rm;
	@Mapper ComponentMapper<Velocity> vm;

	@SuppressWarnings("unchecked")
	public RotationSystem() {
		super(Aspect.getAspectForAll(Rotation.class, Velocity.class));
	}

	protected void process(Entity e) {
		// Get the components from the entity using component mappers.
		Rotation r = rm.get(e);
		Velocity v = vm.get(e);
		
		r.setAngle(v.get());
	}

}
