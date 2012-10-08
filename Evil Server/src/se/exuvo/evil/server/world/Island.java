package se.exuvo.evil.server.world;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

import org.apache.log4j.Logger;

import se.exuvo.evil.server.Settings;
import se.exuvo.evil.server.clients.Client;
import se.exuvo.evil.server.world.systems.CollisionSystem;
import se.exuvo.evil.server.world.systems.MovementSystem;
import se.exuvo.evil.server.world.systems.RotationSystem;
import se.exuvo.evil.server.world.systems.TerrainSystem;
import se.exuvo.evil.shared.world.NetEntity;
import se.exuvo.evil.shared.world.NetZone;
import se.exuvo.evil.shared.world.Snapshot;

import com.artemis.Entity;
import com.artemis.World;
import com.artemis.managers.GroupManager;
import com.artemis.managers.PlayerManager;
import com.artemis.managers.TagManager;

public class Island extends World implements Runnable {
	private static final Logger log = Logger.getLogger(Island.class);

	private Terrain terrain;

	private Object lock = new Object();

	private float tickDelay = 1000 / Settings.getInt("tickrate");
	private long oldTime = System.currentTimeMillis();
	private ScheduledFuture<?> schedule;

	private List<Entity> oldEntities = new ArrayList<Entity>();

	public Island(int width, int height) {
		terrain = new Terrain(width, height, this);

		setManager(new GroupManager());
		setManager(new PlayerManager());
		setManager(new TagManager());
//		setManager(new TeamManager());
		
		setSystem(new MovementSystem());
		setSystem(new RotationSystem());
		setSystem(new CollisionSystem());
		
		setSystem(new TerrainSystem());//Do this after movement

		initialize();
	}

	@Override
	public void run() {
		try {
			synchronized (getLock()) {
				long deltaL = System.currentTimeMillis() - oldTime;
				oldTime += deltaL;
				log.trace("Updating zone:" + deltaL / 1000f);

				for (int deltaSum = 0; deltaSum < deltaL;) {
					float delta = Math.min(deltaL - deltaSum, tickDelay * 3);// Max update jump is 3 ticks
					deltaSum += delta;

					setDelta(1000f / delta);
					process();

					// Find updated ones
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

					// Find removed ones
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
						for (int i = 0; i < Client.getClients().size(); i++) {
							Client c = Client.getClients().get(i);
							synchronized (c.getLock()) {
								if (c.getZone() == this) {
									c.sendSnapshot(new Snapshot(empty, d));

									List<NetEntity> e = new ArrayList<NetEntity>();
									for (int o = 0; o < n.size(); o++) {
										e.add(n.get(o));
										if (o % 32 == 31 || o + 1 == n.size()) {// Send 32 at a time
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
		} catch (Throwable en) {
			log.fatal("", en);
		}
	}

	public Object getLock() {
		return lock;
	}

	public NetZone getNet() {
//			NetZone n = new NetZone(squares.length, squares[0].length);
//			for(int i=0;i<squares.length;i++){
//				for(int o=0;o<squares[i].length;o++){
//					n.getSquares()[i][o] = getSquares()[i][o].getNet();
//				}
//			}
//			
//			for(Entity e : entities){
//				n.getEntities().add(e.getNet());
//			}
//			
//			return n;
	}

	public void setSchedule(ScheduledFuture<?> scheduler) {
		this.schedule = scheduler;
		this.oldTime = System.currentTimeMillis();
	}

	public ScheduledFuture<?> getSchedule() {
		return schedule;
	}

	public Terrain getTerrain() {
		return terrain;
	}

}
