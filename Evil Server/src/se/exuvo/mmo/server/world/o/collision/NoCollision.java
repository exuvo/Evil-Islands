package se.exuvo.mmo.server.world.o.collision;

import se.exuvo.mmo.server.world.Collidable;
import se.exuvo.mmo.server.world.Unit;
import se.exuvo.mmo.server.world.o.units.buildings.Building;

public class NoCollision implements CollisionType{

	@Override
	public boolean collides(Collidable self, Collidable other) {
		return false;
	}

}
