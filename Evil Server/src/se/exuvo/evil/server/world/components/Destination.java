package se.exuvo.evil.server.world.components;



import com.artemis.Component;
import com.artemis.Entity;

public class Destination extends Component {
	private Position target = null;
	private Entity targetE = null;
	
	public void setTargetEntity(Entity newTargetEntity){
		if(getTargetEntity() != null){
			getTargetEntity().getEventQueue(DeathEvent.class).removeEventListener(this);
		}
		targetE = newTargetEntity;
	}
	
	public Entity getTargetEntity(){
		return targetE;
	}
}
