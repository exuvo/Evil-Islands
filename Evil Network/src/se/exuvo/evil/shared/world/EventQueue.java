package se.exuvo.evil.shared.world;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

import org.apache.log4j.Logger;


public class EventQueue {
	protected static final Logger log = Logger.getLogger(EventQueue.class);
	private List<EventListenerr> listeners = new ArrayList<EventListenerr>();
	private Class<? extends EventObject> type;
	
	public EventQueue(Class<? extends EventObject> type){
		this.type = type;
	}
	
	public void fireEvent(EventObject e){
		if(e.getClass().equals(type)){
			List<EventListenerr> q = new ArrayList<EventListenerr>();
			synchronized (listeners) {
				q.addAll(listeners);
			}
			for(EventListenerr t : q){
				t.event(e);
			}
		}
	}
	
	public void addEventListener(EventListenerr l){
		synchronized (listeners) {
			listeners.add(l);
		}
	}
	
	public void removeEventListener(EventListenerr l){
		synchronized (listeners) {
			listeners.remove(l);
		}
	}
	
	public Class<? extends EventObject> getType(){
		return type;
	}
	
}
