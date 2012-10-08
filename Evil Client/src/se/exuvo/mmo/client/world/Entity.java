package se.exuvo.mmo.client.world;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

import org.apache.log4j.Logger;

import se.exuvo.mmo.client.world.abilities.Ability;
import se.exuvo.mmo.shared.world.EventListenerr;
import se.exuvo.mmo.shared.world.EventQueue;
import se.exuvo.mmo.shared.world.NetAbility;
import se.exuvo.mmo.shared.world.NetEntity;
import se.exuvo.mmo.shared.world.Position;
import se.exuvo.mmo.shared.world.Behavior;

public class Entity implements Behavior{
	protected static final Logger log = Logger.getLogger(Entity.class);
	private static final long serialVersionUID = -1846402420971124787L;
	private transient List<Behavior> runnables = new ArrayList<Behavior>();
	private List<Ability> abilities = new ArrayList<Ability>();
	private List<EventQueue> eventQueues = new ArrayList<EventQueue>();
	private String name;
	private long id;
	private long owner;
	private Position position;
	private Zone zone;
	private int hp=1, maxHP=1;
	/*
	 * Nån lista som innehåller behaviors?
	 */
	
	public boolean isAlive(){
		return hp > 0;
	}
	
	public void setHP(int hp) {
		this.hp = hp;
	}

	public int getHP() {
		return hp;
	}

	public void setMaxHP(int maxHP) {
		this.maxHP = maxHP;
	}

	public int getMaxHP() {
		return maxHP;
	}

	public static final long WORLD = 0;

	public Entity(){
		
	}
	
	public Entity(NetEntity n){
		name = n.getName();
		id = n.getId();
		owner = n.getOwner();
		position = n.getPosition();
		hp = n.getHP();
		maxHP = n.getMaxHP();
		for(NetAbility a : n.getAbilities()){
			addAbility(Ability.get(a));
		}
	}

	public void setOwner(long owner) {
		this.owner = owner;
	}

	public long getOwner() {
		return owner;
	}

	public void setZone(Zone zone) {
		this.zone = zone;
	}

	public Zone getZone() {
		return zone;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	public Position getPosition() {
		return position;
	}

	public long getId() {
		return id;
	}
	
	public void addRunnable(Behavior r) {
		runnables.add(r);
	}
	
	public List<Behavior> getRunnables() {
		return runnables;
	}
	
	public void update(float delta){
		for(Behavior r : runnables){
			r.update(delta);
		}
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	public void addAbility(Ability component) {
		abilities.add(component);
	}

	public List<Ability> getAbilities(Class<? extends Ability> type) {
		List<Ability> list = new ArrayList<Ability>();
		
		for(Ability comp : abilities){
			if(type.isAssignableFrom(comp.getClass()) ){
				list.add(comp);
			}
		}
		return list;
	}
	
	public Ability getAbility(String id) {
		for(Ability comp : abilities){
			if(comp.getId().equals(id)){
				return comp;
			}
		}
		return null;
	}
	
	public List<Ability> getAbilities() {
		return abilities;
	}
	
	public void addEventQueue(EventQueue e) {
		if(getEventQueue(e.getType()) == null){//Check if already exists
			eventQueues.add(e);
		}
	}
	
	public void removeEventQueue(EventQueue e) {
		for(EventQueue ee : eventQueues){
			if(ee == e){
				eventQueues.remove(ee);
			}
		}
	}
	
	public boolean fireEvent(EventObject o){
		for(EventQueue e : getEventQueues()){
			e.fireEvent(o);
			return true;
		}
		return false;
	}
	
	@Override
	public String toString(){
		return getId() + ":\"" + getName() + "\"";
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
		return null;
	}
	
	/**
	 * 
	 * @param t The type of EventObject to listen for.
	 * @return
	 */
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
	
}
