package se.exuvo.evil.server.world.components;

import com.artemis.Component;

public class PowerConsumer extends Component {
	private int energyConsumed;

	public int getEnergyConsumed() {
		return energyConsumed;
	}

	public void setEnergyConsumed(int energyConsumed) {
		this.energyConsumed = energyConsumed;
	}
}
