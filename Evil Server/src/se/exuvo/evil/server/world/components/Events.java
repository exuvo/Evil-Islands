package se.exuvo.evil.server.world.components;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

import se.exuvo.evil.server.world.events.EventQueue;


import com.artemis.Component;

public class Events extends Component {
	private List<EventQueue> eventQueues = new ArrayList<EventQueue>();
	
	public void addEventQueue(EventQueue e) {
		if(getEventQueue(e.getType()) == null){//Check if already exists
			eventQueues.add(e);
		}
	}
	
	public void removeEventQueue(EventQueue e) {
		for(EventQueue ee : eventQueues){
			if(ee == e){
				eventQueues.remove(ee);
				break;
			}
		}
	}
	
	public void fireEvent(EventObject o){
		World.fireEvent(o);
		getZone().fireEvent(o);
		List<EventQueue> q = new ArrayList<EventQueue>();
		q.addAll(getEventQueues());
		for(EventQueue e : q){
			e.fireEvent(o);
		}
	}
	
	/**
	 * Returns an EventQueue which accepts EventObjects of type t.
	 * @param t
	 * @return null if none exists.
	 */
	public EventQueue getEventQueue(Class<? extends EventObject> t) {
		for(EventQueue q : eventQueues){
			if(q.getType().equals(t)){
				return q;
			}
		}
		EventQueue q = new EventQueue(t);
		eventQueues.add(q);
		return q;
	}
	
	/**
	 * 
	 * @param t The type of EventObject to listen for.
	 * @return
	 */
	public void addEventListener(EventReaction l, Class<? extends EventObject> o){
		getEventQueue(o).addEventListener(l);
	}
	
	public List<EventQueue> getEventQueues() {
		return eventQueues;
	}

}
