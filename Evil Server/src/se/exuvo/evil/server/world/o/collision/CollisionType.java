package se.exuvo.evil.server.world.o.collision;

import se.exuvo.evil.server.world.Collidable;

public interface CollisionType {
	
	public boolean collides(Collidable self, Collidable other);

}
