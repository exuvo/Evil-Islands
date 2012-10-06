package se.exuvo.evil.server.world;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

import org.apache.log4j.Logger;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.util.pathfinding.PathFindingContext;
import org.newdawn.slick.util.pathfinding.TileBasedMap;

import se.exuvo.evil.server.Settings;
import se.exuvo.evil.server.clients.Client;
import se.exuvo.evil.shared.world.EventListenerr;
import se.exuvo.evil.shared.world.EventQueue;
import se.exuvo.evil.shared.world.NetEntity;
import se.exuvo.evil.shared.world.NetZone;
import se.exuvo.evil.shared.world.Position;
import se.exuvo.evil.shared.world.Snapshot;

import com.artemis.Entity;
import com.artemis.World;

public class Island implements Runnable {
	private static final Logger log = Logger.getLogger(Island.class);
	
	private World world = new World();
	private Terrain terrain;
	
	private Object lock = new Object();
	
	private float delay = Settings.getInt("updateDelay");
	private long oldTime = System.currentTimeMillis();
	private ScheduledFuture<?> schedule;
	
	private List<Entity> oldEntities = new ArrayList<Entity>();
	
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
		List<EventQueue> q = new ArrayList<EventQueue>();
		q.addAll(getEventQueues());
		for(EventQueue e : q){
			e.fireEvent(o);
		}
	}
	
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
	
	public boolean addEventListener(EventListenerr l, Class<? extends EventObject> o){
		EventQueue q = getEventQueue(o);
		if(q != null){
			q.addEventListener(l);
			return true;
		}
		return false;
	}
	
	public List<EventQueue> getEventQueues() {
		return eventQueues;
	}
	
	public World getWorld(){
		return world;
	}
	
	public Island(int width, int height){
		terrain = new Terrain(width, height);
		
		world.setSystem(new MovementSystem());
		world.setSystem(new RotationSystem());
	}

	@Override
	public void run() {
		try{
			synchronized (getLock()) {
				long deltaL = System.currentTimeMillis() - oldTime;
				oldTime += deltaL;
				float deltaTotal = ((float)deltaL)/1000 ;
				log.trace("Updating zone:" + deltaTotal);
				
				for(float deltaSum=0; deltaSum < deltaTotal;){
					float delta = Math.min(deltaTotal - deltaSum, delay*3);//Max update jump is 3 ticks
					deltaSum += delta;
					
					world.setDelta(delta);
					world.process();
					
					//Find updated ones
					List<NetEntity> n = new ArrayList<NetEntity>();
//					for(Entity e : getEntities()){
//						boolean found = false;
//						
//						for(Entity oldE : oldEntities){
//							if(e.getId() == oldE.getId()){
//								if(!e.equals(oldE)){
//									n.add(e.getNet());
//								}
//								found = true;
//								break;
//							}
//						}
//						
//						if(!found){
//							n.add(e.getNet());
//						}
//					}
					
					//Find removed ones
					List<Long> d = new ArrayList<Long>();
//					for(Entity oldE : oldEntities){
//						boolean found = false;
//						for(Entity e : getEntities()){
//							if(e.getId() == oldE.getId()){
//								found = true;
//								break;
//							}
//						}
//						if(!found){
//							d.add(oldE.getId());
//						}
//					}
					
					oldEntities.clear();
//					for(Entity e : getEntities()){
//						oldEntities.add(e.clone());
//					}
					
					synchronized (Client.getClients()) {
						List<NetEntity> empty = new ArrayList<NetEntity>();
						for(int i=0;i<Client.getClients().size();i++){
							Client c = Client.getClients().get(i);
							synchronized (c.getLock()) {
								if(c.getZone() == this){
									c.sendSnapshot(new Snapshot(empty,d));
									
									List<NetEntity> e = new ArrayList<NetEntity>();
									for(int o=0;o<n.size();o++){
										e.add(n.get(o));
										if(o % 32 == 31 || o+1 == n.size()){//Send 32 at a time
											c.sendSnapshot(new Snapshot(new ArrayList<NetEntity>(e)));
											e.clear();
										}
									}
								}
							}
						}
					}
				}
			}
		}catch(Throwable en){
			log.fatal("",en);
		}
	}
	
	public Object getLock(){
		return lock;
	}
	
	public NetZone getNet(){
			NetZone n = new NetZone(squares.length, squares[0].length);
			for(int i=0;i<squares.length;i++){
				for(int o=0;o<squares[i].length;o++){
					n.getSquares()[i][o] = getSquares()[i][o].getNet();
				}
			}
			
			for(Entity e : entities){
				n.getEntities().add(e.getNet());
			}
			
			return n;
	}
	
	public void setSchedule(ScheduledFuture<?> scheduler) {
		this.schedule = scheduler;
		this.oldTime = System.currentTimeMillis();
	}

	public ScheduledFuture<?> getSchedule() {
		return schedule;
	}

}
