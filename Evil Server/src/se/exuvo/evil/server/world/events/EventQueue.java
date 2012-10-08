package se.exuvo.evil.server.world.events;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

import org.apache.log4j.Logger;

import com.artemis.utils.Bag;


public class EventQueue {
	protected static final Logger log = Logger.getLogger(EventQueue.class);
	private Bag<EventReaction> listeners = new Bag<EventReaction>();
	private Class<? extends EventObject> type;
	
	public EventQueue(Class<? extends EventObject> type){
		this.type = type;
	}
	
	public void fireEvent(EventObject e){
		if(e.getClass().equals(type)){
			List<EventReaction> q = new ArrayList<EventReaction>();
			synchronized (listeners) {
				q.addAll(listeners);
			}
			for(EventReaction t : q){
				t.event(e);
			}
		}
	}
	
	public void addEventListener(EventReaction l){
		synchronized (listeners) {
			listeners.add(l);
		}
	}
	
	public void removeEventListener(EventReaction l){
		synchronized (listeners) {
			listeners.remove(l);
		}
	}
	
	public Class<? extends EventObject> getType(){
		return type;
	}
	
}
