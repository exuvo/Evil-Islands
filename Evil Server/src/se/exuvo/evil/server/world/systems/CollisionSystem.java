package se.exuvo.evil.server.world.systems;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.systems.IntervalEntitySystem;
import com.artemis.utils.ImmutableBag;

//TODO use SAT (Separating Axis Theorem) http://www.codezealot.org/archives/55
public class CollisionSystem extends IntervalEntitySystem {

	public CollisionSystem(Aspect aspect, float interval) {
		super(aspect, interval);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void processEntities(ImmutableBag<Entity> entities) {
		// TODO Auto-generated method stub

	}

}
