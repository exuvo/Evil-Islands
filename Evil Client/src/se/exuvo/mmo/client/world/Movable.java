package se.exuvo.mmo.client.world;

import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Transform;
import org.newdawn.slick.geom.Vector2f;

import se.exuvo.mmo.shared.world.NetMovable;
import se.exuvo.mmo.shared.world.Position;
import se.exuvo.mmo.shared.world.Behavior;

public class Movable extends Collidable{
	private static final long serialVersionUID = 9071989715826503009L;
	private float speed;
	private float angle;
	
	public Movable(){
		super();
		new Updater(this);
	}
	
	public Movable(NetMovable n){
		super(n);
		setAngle(n.getAngle());
		setSpeed(n.getSpeed());
		new Updater(this);
	}
	
	public void setSpeed(float speed){
		this.speed = speed;
	}
	
	public float getSpeed(){
		return speed;
	}
	
	@Override
	public Shape getCollisionShape() {
		return getShape().transform(Transform.createRotateTransform((float) Math.toRadians(getAngle())))
			.transform(Transform.createTranslateTransform(getPosition().getX(), getPosition().getY()));
	}
	
	public void setAngle(float angle){
		this.angle = angle;
	}
	
	public float getAngle(){
		return angle;
	}
	
	class Updater implements Behavior{
		private Movable m;
		
		public Updater(Movable m){
			this.m = m;
			m.addRunnable(this);
		}

		@Override
		public void update(float delta) {
			if(m.getSpeed() == 0)
				return;
			
			Position movement = new Position();
			Vector2f v = new Vector2f(m.getSpeed()*delta,0);
			v.setTheta(m.getAngle());
			movement.set(v.x, v.y);
			if(isCollidingInZoneWithSomething(movement.add(m.getPosition()))){//collision detection
				movement = new Position(0,0);
			}
			
			m.setPosition(m.getPosition().add(movement));
		}
	}
}