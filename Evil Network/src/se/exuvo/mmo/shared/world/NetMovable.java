package se.exuvo.mmo.shared.world;


public class NetMovable extends NetCollidable{
	private float speed;
	private float angle;
	
	public NetMovable(){
		super();
	}
	
	public void setSpeed(float speed){
		this.speed = speed;
	}
	
	public float getSpeed(){
		return speed;
	}
	
	public void setAngle(float angle){
		this.angle = angle;
	}
	
	public float getAngle(){
		return angle;
	}
	
}