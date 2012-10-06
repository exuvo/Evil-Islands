package se.exuvo.mmo.server.world.o.collision;

import se.exuvo.mmo.server.world.Collidable;
import se.exuvo.mmo.server.world.o.units.buildings.Building;
import se.exuvo.mmo.server.world.o.units.buildings.Door;

public class BuildingCollision implements CollisionType{

	@Override
	public boolean collides(Collidable self, Collidable other) {
		if(other instanceof Building){
			return true;
		}
		return false;
	}

}
