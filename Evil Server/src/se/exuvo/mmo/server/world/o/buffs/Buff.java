package se.exuvo.mmo.server.world.o.buffs;

import se.exuvo.mmo.server.world.Entity;

public interface Buff {
	
	public void activate(Entity e);
	public void remove(Entity e);
	/**
	 * -1 = forever
	 * 0 = remove Me
	 * @return
	 */
	public long getDuration();

}
