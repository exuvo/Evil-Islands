package se.exuvo.evil.server.world.o.collision;

import se.exuvo.evil.server.world.Collidable;
import se.exuvo.evil.server.world.o.units.buildings.Blueprint;

public class BlueprintCollision implements CollisionType{

	@Override
	public boolean collides(Collidable self, Collidable other) {
		if(self instanceof Blueprint && other instanceof Blueprint){
			return true;
		}
		return false;
	}

}
