package se.exuvo.evil.shared.world;

import org.newdawn.slick.geom.Shape;

import com.esotericsoftware.kryo.NotNull;

public class NetBlueprintAdd extends NetAbility{
	private int cost = 0;
	private @NotNull Shape shape;
	private @NotNull String buildingName;
	
	public NetBlueprintAdd(){
		super();
	}
	

	public void setCost(int cost) {
		this.cost = cost;
	}

	public int getCost() {
		return cost;
	}

	public void setShape(Shape shape) {
		this.shape = shape;
	}

	public Shape getShape() {
		return shape;
	}


	public void setBuildingName(String buildingName) {
		this.buildingName = buildingName;
	}


	public String getBuildingName() {
		return buildingName;
	}

}
