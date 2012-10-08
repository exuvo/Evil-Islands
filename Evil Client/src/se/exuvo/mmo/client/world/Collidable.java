package se.exuvo.mmo.client.world;

import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Transform;

import se.exuvo.mmo.shared.world.NetCollidable;
import se.exuvo.mmo.shared.world.Position;

public class Collidable extends Entity{
	private Shape shape;
	
	public Collidable(){
		super();
	}
	
	public Collidable(NetCollidable n){
		super(n);
		setShape(n.getNormalCollisionShape());
	}
 
	public Shape getCollisionShape() {
		return shape.transform(Transform.createTranslateTransform(getPosition().getX(), getPosition().getY()));
	}
  
	public boolean isCollidingWith(Collidable collidable){
		return this.getCollisionShape().intersects(collidable.getCollisionShape());
	}
	
	public boolean isCollidingInZoneWithSomething(){
		return isCollidingInZoneWithSomething(this.getPosition());
	}

	public boolean isCollidingInZoneWithSomething(Position pos){
		Collidable c;
		for(Entity e : this.getZone().getEntities()){
			if(e instanceof Collidable){
				c=(Collidable)e;
				if(c==this)
					continue;
				if(c.getShape().getMaxX()<this.getShape().getMinX() || c.getShape().getMaxY()<this.getShape().getMinY() ||
						c.getShape().getMinX()>this.getShape().getMaxX() || c.getShape().getMinY()>this.getShape().getMaxY()){//check sloppy
					//do nothing
				}else{
					if(c.isCollidingWith(this)){//check carefully
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public void setShape(Shape shape) {
		this.shape = shape;
	}

	public Shape getShape() {
		return shape;
	}
}
