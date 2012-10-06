package se.exuvo.evil.shared.world;

import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Transform;

import com.esotericsoftware.kryo.NotNull;

public class NetCollidable extends NetEntity{
	private @NotNull Shape shape;
	
	public NetCollidable(){
		super();
	}

	public Shape getNormalCollisionShape() {
		return shape;
	}
 
	public Shape getCollisionShape() {
		return shape.transform(Transform.createTranslateTransform(getPosition().getX(), getPosition().getY()));
	}
  	
	public void setShape(Shape shape) {
		this.shape = shape;
	}

	public Shape getShape() {
		return shape;
	}
	
}
