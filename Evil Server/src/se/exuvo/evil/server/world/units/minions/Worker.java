package se.exuvo.evil.server.world.units.minions;

import java.util.Arrays;

import org.newdawn.slick.geom.Circle;

import se.exuvo.evil.server.world.components.CollisionComponent;
import se.exuvo.evil.server.world.components.Position;
import se.exuvo.evil.server.world.components.Rotation;
import se.exuvo.evil.server.world.units.UnitTemplate;

import com.artemis.Entity;

public class Worker extends UnitTemplate {

	@Override
	public void apply(Entity e) {
		//TODO make a base class for generic movable unit.
		e.addComponent(new Position());
		e.addComponent(new CollisionComponent(new Circle(0, 0, 10), Arrays.asList(
				new CollisionComponent.LayerCheck(false, false)
					.addLayer(CollisionComponent.CollisionLayer.Surface)
					.addLayer(CollisionComponent.CollisionLayer.Floor)
				)));
		e.addComponent(new Rotation());
	}

}
