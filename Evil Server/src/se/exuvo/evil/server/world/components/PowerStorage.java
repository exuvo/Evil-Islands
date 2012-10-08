package se.exuvo.evil.server.world.components;

import com.artemis.Component;

public class PowerStorage extends Component {
	private int storedEnergy;

	public int getStoredEnergy() {
		return storedEnergy;
	}

	public void setStoredEnergy(int storedEnergy) {
		this.storedEnergy = storedEnergy;
	}
}
