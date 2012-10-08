package se.exuvo.mmo.client.world.units.buildings;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import se.exuvo.mmo.client.world.Collidable;
import se.exuvo.mmo.shared.world.NetBlueprint;


public class Blueprint extends Collidable {
	private Building building;
	private boolean paused;
	
	public Blueprint(NetBlueprint b){
		super(b);
		building = new Building(b.getBuilding());
		setName(getName() + ": " + building.getName()); 
		paused = b.isPaused();
	}
	
	public Building getBuilding(){
		return building;
	}

	public boolean isPaused() {
		return paused;
	}
	
	public void draw(Graphics g) {
		g.setColor(new Color(0,150,200, 0.6f));
		building.setPosition(getPosition());
		building.draw(g);
		g.setColor(new Color(100,0,255));
		g.drawString(paused ? "P":"b", getPosition().x - building.getShape().getWidth()/2,  getPosition().y - building.getShape().getHeight()/2);
	}

}
