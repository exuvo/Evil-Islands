package se.exuvo.mmo.client.world.units.buildings;

import org.newdawn.slick.Graphics;

import se.exuvo.mmo.shared.world.NetDoor;
import se.exuvo.mmo.shared.world.NetUnit;

public class Door extends Building {
	private boolean open;
	private int level = OPEN;
	public final static int OPEN = 0;
	public final static int CLOSED = 1;
	public final static int LOCKED = 2;
	public final static int GUARDED = 3;
	
	public Door(){
		super();
	}
	
	public Door(NetUnit n){
		super((NetDoor) n);
		NetDoor b = (NetDoor) n;
		setLockLevel(b.getLevel());
		setOpen(b.isOpen());
	}
	
	public void setLockLevel(int i){
		level = i;
	}
	
	public int getLockLevel(){
		return level;
	}
	
	@Override
	public void draw(Graphics g) {
		super.draw(g);
		String s = "?";
		if(level == OPEN){
			s = "O";
		}else if(level == CLOSED){
			s = "C";
		}else if(level == LOCKED){
			s = "L";
		}else if(level == GUARDED){
			s = "G";
		}
		if(isOpen()){
			s = s.toLowerCase();
		}
		g.drawString(s, getPosition().x - getShape().getWidth()/2,  getPosition().y - getShape().getHeight()/2);
	}
	
	@Override
	public boolean equals(Object other){
		if(other instanceof Door){
			Door d = (Door) other;
			if(super.equals(d)){
				if(level == d.getLockLevel()){
					return true;
				}
			}
		}
		return false;
	}

	public void setOpen(boolean open) {
		this.open = open;
	}

	public boolean isOpen() {
		return open;
	}

}
