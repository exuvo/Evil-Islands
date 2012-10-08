package se.exuvo.mmo.client.world;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import se.exuvo.mmo.shared.world.NetEntity;
import se.exuvo.mmo.shared.world.NetZone;

public class Zone implements Runnable{
	private static final long serialVersionUID = 1560076047726385078L;
	private static final Logger log = Logger.getLogger(Zone.class);
	private Square[][] squares;
	private List<Entity> entities = new ArrayList<Entity>();
	private transient Object lock = new Object();
	private transient long oldTime = System.currentTimeMillis();
		
	public Zone(int x, int y){
		squares = new Square[x][y];
	}
	
	public Zone(NetZone n){
		squares = new Square[n.getSquares().length][n.getSquares()[0].length];
		for(int i=0;i<n.getSquares().length;i++){
			for(int o=0;o<n.getSquares()[0].length;o++){
				squares[i][o] = new Square(n.getSquares()[i][o]);
			}
		}
		
		for(NetEntity e : n.getEntities()){
			Entity i = World.getEntity(e);
			i.setZone(this);
			entities.add(i);
		}
	}

	public Square[][] getSquares() {
		return squares;
	}
	
	@Override
	public void run() {
		synchronized (getLock()) {
			log.trace("Updating zone");
			long deltaL = System.currentTimeMillis() - oldTime;
			oldTime += deltaL;
			float delta = ((float)deltaL)/1000 ;
			
			for(Entity e : getEntities()){
				e.update(delta);
			}
		}
	}

	public List<Entity> getEntities() {
		return entities;
	}
	
	public Entity getEntity(long id){
		for(Entity e : entities){
			if(e.getId() == id){
				return e;
			}
		}
		return null;
	}
	
	public Object getLock(){
		return lock;
	}


}
