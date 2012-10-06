package se.exuvo.evil.server.world.components;

import org.newdawn.slick.geom.Vector2f;

import com.artemis.Component;

public class Velocity extends Component {
	private float x, y;
	private Position next;// Next position
	private boolean colliding;

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public Vector2f get() {
		return new Vector2f(getX(), getY());
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public Position getNext() {
		return next;
	}

	public void setNext(Position next) {
		this.next = next;
	}

	public boolean isColliding() {
		return colliding;
	}

	public void setColliding(boolean collidingNext) {
		this.colliding = collidingNext;
	}

}
