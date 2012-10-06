package se.exuvo.evil.server.world.components;

import org.newdawn.slick.geom.Vector2f;

import com.artemis.Component;

public class Rotation extends Component {
	private double angle;

	public double getAngle() {
		return angle;
	}

	public void setAngle(double angle) {
		if ((angle < -360) || (angle > 360)) {
			angle = angle % 360;
		}
		if (angle < 0) {
			angle = 360 + angle;
		}
		
		this.angle = angle;
	}
	
	public void setAngle(Position source, Position target){
		setAngle(source.sub(target).get().getTheta());
	}
	
	public void setAngle(Vector2f v){
		setAngle(v.getTheta());
	}

}
