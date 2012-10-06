package se.exuvo.mmo.shared.world;


public class NetDoor extends NetBuilding{
	private int level;
	private boolean open;
	
	public NetDoor(){
		super();
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getLevel() {
		return level;
	}

	public void setOpen(boolean open) {
		this.open = open;
	}

	public boolean isOpen() {
		return open;
	}

	
}
