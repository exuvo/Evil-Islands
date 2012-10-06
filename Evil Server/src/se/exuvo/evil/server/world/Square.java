package se.exuvo.evil.server.world;

import se.exuvo.evil.shared.world.NetSquare;

public class Square {
	public static final int size = 20;
	private boolean pathable = true;
	private int cost = 10;
	
	/**
	 * En ruta är 20x20
	 */
	public Square(){
	}
	
	public NetSquare getNet(){
		NetSquare n = new NetSquare();
		n.setPathable(isGroundPathable());
		n.setCost(getCost());
		n.setSize(size);
		return n;
	}
	
	public void setPathable(boolean pathable) {
		this.pathable = pathable;
	}

	public boolean isGroundPathable() {
		return pathable;
	}
	
	public void setCost(int cost){
		this.cost = cost;
	}
	
	public int getCost(){
		return cost;
	}

}
