package se.exuvo.evil.server.world;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;

import se.exuvo.evil.server.world.o.abilities.Ability;
import se.exuvo.evil.server.world.o.events.ConnectEvent;
import se.exuvo.evil.shared.world.EventListenerr;
import se.exuvo.evil.shared.world.EventQueue;

import com.artemis.Entity;

public class Planet {
	private static List<Island> zones = new ArrayList<Island>();
	private static final Logger log = Logger.getLogger(Planet.class);
	private static List<EventQueue> eventQueues = new ArrayList<EventQueue>();

	public static void addEventQueue(EventQueue e) {
		if (getEventQueue(e.getType()) == null) {// Check if already exists
			eventQueues.add(e);
		}
	}

	public static void removeEventQueue(EventQueue e) {
		for (EventQueue ee : eventQueues) {
			if (ee == e) {
				eventQueues.remove(ee);
				break;
			}
		}
	}

	public static Entity getEntityByUUID(UUID id) {
		// for(Island z : zones){
		// synchronized (z.getLock()) {
		// Entity e = z.getEntityById(id);
		// if(e != null){
		// return e;
		// }
		//
		// }
		// }
		return null;
	}

	public static void fireEvent(EventObject o) {
		List<EventQueue> q = new ArrayList<EventQueue>();
		q.addAll(getEventQueues());
		for (EventQueue e : q) {
			e.fireEvent(o);
		}
	}

	public static EventQueue getEventQueue(Class<? extends EventObject> t) {
		for (EventQueue q : eventQueues) {
			if (q.getType().equals(t)) {
				return q;
			}
		}
		EventQueue q = new EventQueue(t);
		eventQueues.add(q);
		return q;
	}

	/**
	 * @param t The type of EventObject to listen for.
	 * @return
	 */
	public static boolean addEventListener(EventListenerr l, Class<? extends EventObject> o) {
		EventQueue q = getEventQueue(o);
		if (q != null) {
			q.addEventListener(l);
			return true;
		}
		return false;
	}

	public static List<EventQueue> getEventQueues() {
		return eventQueues;
	}

	public static void init() {
		Ability.init();
		log.info("Creating initial world");

		Island z = new Island(50, 50);

		addEventListener(new EventListenerr() {
			@Override
			public void event(EventObject e) {
				log.info("A Player has joined");
				ConnectEvent c = (ConnectEvent) e;
			}
		}, ConnectEvent.class);

		addZone(z);
	}

	public static void addZone(Island z) {
		zones.add(z);
		z.setSchedule(ThreadHandler.add(z));
	}

	public static List<Island> getZones() {
		return zones;
	}

	public static Island getHub() {
		return zones.get(0);
	}

	public static Island getZone(int i) {
		return zones.get(i);
	}
}