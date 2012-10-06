package se.exuvo.evil.server.world.o.collision;

import se.exuvo.evil.server.world.Collidable;
import se.exuvo.evil.server.world.Unit;
import se.exuvo.evil.server.world.o.units.buildings.Building;

public class NoCollision implements CollisionType{

	@Override
	public boolean collides(Collidable self, Collidable other) {
		return false;
	}

}
