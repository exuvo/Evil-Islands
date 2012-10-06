package se.exuvo.mmo.server.world.o.collision;

import se.exuvo.mmo.server.world.Collidable;

public interface CollisionType {
	
	public boolean collides(Collidable self, Collidable other);

}
