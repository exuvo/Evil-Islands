package se.exuvo.evil.server.world.components;

import org.newdawn.slick.geom.Vector2f;

import com.artemis.Component;

public class Velocity extends Component {
	private float x, y;

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}
	
	public Vector2f get(){
		return new Vector2f(getX(), getY());
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}
	
}
