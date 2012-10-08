package se.exuvo.mmo.client.world;

import org.apache.log4j.Logger;

import se.exuvo.mmo.client.world.Unit;
import se.exuvo.mmo.client.world.UnitTypeException;
import se.exuvo.mmo.client.world.units.buildings.Blueprint;
import se.exuvo.mmo.shared.world.NetBlueprint;
import se.exuvo.mmo.shared.world.NetCollidable;
import se.exuvo.mmo.shared.world.NetEntity;
import se.exuvo.mmo.shared.world.NetHero;
import se.exuvo.mmo.shared.world.NetMovable;
import se.exuvo.mmo.shared.world.NetUnit;
import se.exuvo.mmo.shared.world.NetZone;
import se.exuvo.mmo.shared.world.Snapshot;


public class World{
	private static final Logger log = Logger.getLogger(World.class);
	private static Zone world = null;
	
	public static Zone getZone(){
		return world;
	}
	
	public static void setWorld(NetZone a){
		if(world!=null){
			synchronized(world.getLock()){
				if(a == null){
					world = null;
					return;
				}
				world = new Zone(a);
			}
		}else{
			if(a == null){
				world = null;
				return;
			}
			world= new Zone(a);
			log.info("Got initial world from server");
		}
	}
	
	public static Entity getEntity(NetEntity e){
		Entity i;
		if(e instanceof NetHero){
			i = new Hero((NetHero) e);
		}else if(e instanceof NetUnit){
			try {
				i = Unit.get((NetUnit) e);
			} catch (UnitTypeException e1) {
				//e1.printStackTrace();
				i = new Unit((NetUnit) e);
			}
		}else if(e instanceof NetMovable){
			i = new Movable((NetMovable) e);
		}else if(e instanceof NetBlueprint){
			i = new Blueprint((NetBlueprint) e);
		}else if(e instanceof NetCollidable){
			i = new Collidable((NetCollidable) e);
		}else{
			i = new Entity(e);
		}
		return i;
	}
	
	public static void changeWorld(Snapshot s){
		if(world != null){
			synchronized(world.getLock()){
				boolean found=false;
				for(NetEntity e : s.getEntities()){
					for(Entity f : world.getEntities()){
						if(e.getId() == f.getId()){
							//TODO make it Update f instead of Replacing with a new Object
							world.getEntities().remove(f);
							Entity i = getEntity(e);
							i.setZone(world);
							f = i;
							world.getEntities().add(f) ;
							found=true;
							break;
						}
					}
					if(!found){
						Entity i = getEntity(e);
						i.setZone(world);
						world.getEntities().add(i);
					}
					found=false;
				}
				
				for(long e : s.getDeletedEntites()){
					for(Entity f : world.getEntities()){
						if(e == f.getId()){
							world.getEntities().remove(f);
							break;
						}
					}
				}
			}
		}
	}
}
