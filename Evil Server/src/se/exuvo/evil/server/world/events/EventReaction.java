package se.exuvo.evil.server.world.events;


public class EventReaction {
	private Runnable runnable;
	
	public EventReaction(Runnable code){
		runnable = code;
	}

	public Runnable getRunnable() {
		return runnable;
	}

}
