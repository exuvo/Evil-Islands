package se.exuvo.mmo.shared.world;

public interface Behavior {
	
	/**
	 * Beware of bug: if you put this in its own class and have it refer to the outside,
	 *  it will refer to the instance that created it, not the one it's currently in.
	 * @param delta - seconds since last run
	 */
	public void update(float delta);

}
