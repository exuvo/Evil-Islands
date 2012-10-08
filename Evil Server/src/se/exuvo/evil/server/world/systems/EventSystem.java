package se.exuvo.evil.server.world.systems;

import java.util.EventObject;
import java.util.Queue;

import se.exuvo.evil.server.world.components.EventComponent;
import se.exuvo.evil.server.world.events.EventReaction;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;

public class EventSystem extends EntityProcessingSystem {
	@Mapper ComponentMapper<EventComponent> em;
	private Queue<> eventQueue;

	@SuppressWarnings("unchecked")
	public EventSystem() {
		super(Aspect.getAspectForAll(EventComponent.class));
	}

	@Override
	protected void process(Entity e) {
		// TODO Auto-generated method stub
		
	}
	
	public void registerReaction(Entity e, Class<? extends EventObject> o, EventReaction r){
		
	}
	
	public void fireEvent(Entity e, EventObject o){
		
	}

}
