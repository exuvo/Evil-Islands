package se.exuvo.mmo.server.world.o.collision;

import se.exuvo.mmo.server.world.Collidable;
import se.exuvo.mmo.server.world.o.units.buildings.Blueprint;

public class BlueprintCollision implements CollisionType{

	@Override
	public boolean collides(Collidable self, Collidable other) {
		if(self instanceof Blueprint && other instanceof Blueprint){
			return true;
		}
		return false;
	}

}
