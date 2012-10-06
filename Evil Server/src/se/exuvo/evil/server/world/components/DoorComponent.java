package se.exuvo.evil.server.world.components;

import com.artemis.Component;

public class DoorComponent extends Component {
	private State lockLevel;
	public enum State {OPEN, CLOSED, LOCKED, GUARDED};

	public State getLockLevel() {
		return lockLevel;
	}

	public void setLockLevel(State lockLevel) {
		this.lockLevel = lockLevel;
	}

}
