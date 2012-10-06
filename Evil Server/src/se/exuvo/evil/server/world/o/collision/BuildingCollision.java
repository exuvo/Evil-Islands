package se.exuvo.evil.server.world.o.collision;

import se.exuvo.evil.server.world.Collidable;
import se.exuvo.evil.server.world.o.units.buildings.Building;
import se.exuvo.evil.server.world.o.units.buildings.Door;

public class BuildingCollision implements CollisionType{

	@Override
	public boolean collides(Collidable self, Collidable other) {
		if(other instanceof Building){
			return true;
		}
		return false;
	}

}
