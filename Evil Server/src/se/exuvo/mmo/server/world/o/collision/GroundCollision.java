package se.exuvo.mmo.server.world.o.collision;

import se.exuvo.mmo.server.world.Collidable;
import se.exuvo.mmo.server.world.o.units.buildings.Building;
import se.exuvo.mmo.server.world.o.units.buildings.Door;

public class GroundCollision implements CollisionType{

	@Override
	public boolean collides(Collidable self, Collidable other) {
		if(other instanceof Building && !(other instanceof Door && other.getCollisionType().getClass() == NoCollision.class)){
			return true;
		}
		return false;
	}

}
