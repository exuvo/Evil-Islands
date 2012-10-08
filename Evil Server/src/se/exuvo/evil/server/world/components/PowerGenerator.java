package se.exuvo.evil.server.world.components;

import com.artemis.Component;

public class PowerGenerator extends Component {
	private int generation;

	public int getGeneration() {
		return generation;
	}

	public void setGeneration(int generation) {
		this.generation = generation;
	}
}
