package se.exuvo.mmo.shared.world;

import org.newdawn.slick.geom.Vector2f;

public class Position{
	public float x = 0;
	public float y = 0;
	
	public Position(){
	}
	
	public Position(float x, float y){
		set(x,y);
	}
	
	public Position(Position other){
		set(other.getX(), other.getY());
	}
	
	public Vector2f get(){
		return new Vector2f(getX(), getY());
	}
	
	public float getX(){
		return x;
	}
	
	public float getY(){
		return y;
	}
	
	public void set(float x, float y){
		this.x = x;
		this.y = y;
	}
	
	public Position add(Position other){
		return new Position(getX() + other.getX(),getY() + other.getY());
	}
	
	public Position sub(Position other){
		return new Position(getX() - other.getX(),getY() - other.getY());
	}
	
	public Position set(Position other){
		set(other.getX(),other.getY());
		return this;
	}
	
	@Override
	public String toString(){
		return "X:" + getX() + " Y:" + getY();
	}
	
	@Override
	public boolean equals(Object other){
		if(other instanceof Position){
			Position p = (Position) other;
			if(getX() == p.getX() && getY() == p.getY()){
				return true;
			}
		}
		return false;
	}
	
	public float distance(Position other){
		return get().distance(other.get());
	}
	
}
