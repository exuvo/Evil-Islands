package se.exuvo.mmo.shared.world;

import com.esotericsoftware.kryo.NotNull;

public class NetBlueprint extends NetCollidable{
	private @NotNull NetBuilding building;
	private boolean paused;
	
	public NetBlueprint(){
		super();
	}

	public void setBuilding(NetBuilding building) {
		this.building = building;
	}

	public NetBuilding getBuilding() {
		return building;
	}

	public void setPaused(boolean paused) {
		this.paused = paused;
	}

	public boolean isPaused() {
		return paused;
	}

	
}
