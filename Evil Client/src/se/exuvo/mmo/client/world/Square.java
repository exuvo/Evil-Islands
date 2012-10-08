package se.exuvo.mmo.client.world;

import se.exuvo.mmo.shared.world.NetSquare;

public class Square{
	private static final long serialVersionUID = -32390366518044848L;
	public static int size = 20;
	private boolean pathable = true;
	private int cost = 1;
	
	/**
	 * En ruta �r typ 20x20 i x och y
	 */
	public Square(NetSquare n){
		setPathable(n.isPathable());
		setCost(n.getCost());
		size = n.getSize();
	}
	
	public void setPathable(boolean pathable) {
		this.pathable = pathable;
	}

	public boolean isPathable() {
		return pathable;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}

	public int getCost() {
		return cost;
	}

}
