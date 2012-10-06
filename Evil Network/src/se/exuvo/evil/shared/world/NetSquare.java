package se.exuvo.evil.shared.world;

import com.esotericsoftware.kryo.NotNull;

public class NetSquare{
	private boolean pathable = true;
	private int size = 20;
	private int cost = 1;
	
	/**
	 * En ruta �r typ 20x20 i x och y
	 */
	public NetSquare(){
	}
	
	public void setPathable(boolean pathable) {
		this.pathable = pathable;
	}

	public boolean isPathable() {
		return pathable;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getSize() {
		return size;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}

	public int getCost() {
		return cost;
	}

}
