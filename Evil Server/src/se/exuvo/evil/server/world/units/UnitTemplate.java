package se.exuvo.evil.server.world.units;

import com.artemis.Entity;
import com.artemis.World;

public abstract class UnitTemplate {

	public abstract void apply(Entity e);
	
	public String getName(){
		return getClass().getName();
	}
	
	public Entity create(World world){
		Entity e = world.createEntity();
		apply(e);
		return e;
	}
	
}
