package se.exuvo.evil.server.world.components;

import org.newdawn.slick.geom.Vector2f;

import com.artemis.Component;

public class Position extends Component {
	private float x, y;

	public Position() {}

	public Position(float x, float y) {
		set(x, y);
	}

	public Position(Position other) {
		set(other.getX(), other.getY());
	}

	public Vector2f get() {
		return new Vector2f(getX(), getY());
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public void set(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public void setX(float x) {
		this.x = x;
	}
	
	public void setY(float y) {
		this.y = y;
	}

	public Position add(Position other) {
		return new Position(getX() + other.getX(), getY() + other.getY());
	}

	public Position sub(Position other) {
		return new Position(getX() - other.getX(), getY() - other.getY());
	}

	public Position set(Position other) {
		set(other.getX(), other.getY());
		return this;
	}

	@Override
	public String toString() {
		return "X:" + getX() + " Y:" + getY();
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof Position) {
			Position p = (Position) other;
			return (getX() == p.getX()) && (getY() == p.getY());
		}
		return false;
	}

	public float distance(Position other) {
		float dx = other.getX() - getX();
		float dy = other.getY() - getY();

		return (float) Math.sqrt((dx * dx) + (dy * dy));
	}

}
