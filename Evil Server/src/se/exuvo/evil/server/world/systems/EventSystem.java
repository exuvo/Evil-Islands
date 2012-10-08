package se.exuvo.evil.server.world.systems;

import java.util.Queue;

import se.exuvo.evil.server.world.components.Events;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;

public class EventSystem extends EntityProcessingSystem {
	@Mapper ComponentMapper<Events> em;
	private Queue<E>

	@SuppressWarnings("unchecked")
	public EventSystem() {
		super(Aspect.getAspectForAll(Events.class));
	}

	@Override
	protected void process(Entity e) {
		// TODO Auto-generated method stub
		
	}
	
	

}
