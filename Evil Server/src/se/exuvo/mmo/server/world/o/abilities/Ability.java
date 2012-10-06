package se.exuvo.mmo.server.world.o.abilities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import se.exuvo.mmo.server.world.Entity;
import se.exuvo.mmo.server.world.o.units.buildings.Door;
import se.exuvo.mmo.shared.ClassFinder;
import se.exuvo.mmo.shared.connection.InvalidOrderException;
import se.exuvo.mmo.shared.world.NetAbility;
import se.exuvo.mmo.shared.world.NetAbility.validTarget;
import se.exuvo.mmo.shared.world.Position;

public abstract class Ability implements Cloneable{
	protected static final Logger log = Logger.getLogger(Ability.class);
	private List<Validator> validators = new ArrayList<Validator>();
	private static List<Class<? extends Ability>> abilities = new ArrayList<Class<? extends Ability>>();
	private int range = -1;
	private boolean casting = false;
	private boolean casted = false;
	private String name = "?";
	private List<validTarget> targets = new ArrayList<validTarget>();
	
	public Ability(String name, validTarget[] t){
		this.name = name;
		getValidators().add(new Validator() {
			
			@Override
			public void validate(Entity caster, Position target) throws InvalidOrderException{
				if(!targets.contains(validTarget.point))
					throw new InvalidOrderException("Unable to target points");
			}
			
			@Override
			public void validate(Entity caster, Entity target)  throws InvalidOrderException{
				if(!targets.contains(validTarget.entity) && !targets.contains(validTarget.self)){
					throw new InvalidOrderException("Unable to target entities");
				}
				if(!targets.contains(validTarget.self) && caster == target){
					throw new InvalidOrderException("Unable to target self");
				}
				if(targets.contains(validTarget.self) && caster != target){
					throw new InvalidOrderException("Unable to target entities other than self");
				}
			}
		});
		for(validTarget v : t){
			targets.add(v);
		}
	}
	
	public static List<Class<? extends Ability>> getAbilities(){
		return abilities;
	}
	
	public static void init(){
		abilities.clear();
		log.debug("Loading abilities");
		try {
			List<Class<?>> l = ClassFinder.getClasses("se.exuvo.mmo.server.world.abilities");
			for(Class<?> c : l){
				if(Ability.class.isAssignableFrom(c) && !c.equals(Ability.class)){
					try {
						Class<? extends Ability> cc = c.asSubclass(Ability.class);
						abilities.add(cc);
						log.trace("Loaded ability: " + cc.getSimpleName());
					} catch (Throwable e) {
						log.warn("Failed to load ability: \"" + c.getSimpleName() + "\"", e);
					}
				}
			}
		} catch (ClassNotFoundException e) {
			log.warn("Failed to load abilities", e);
		} catch (IOException e) {
			log.warn("Failed to load abilities", e);
		}
	}
	
	/*public static Ability get(NetAbility a) throws InvalidOrderException{
		for(Class<? extends Ability> b : abilities){
			if(b.getSimpleName().equals(a.getId())){
				try {
					return Ability.class.cast(b.getConstructor(NetAbility.class).newInstance(a));
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}
		throw new InvalidOrderException("Ability not found: " + a.getId());
	}*/
	
	public NetAbility getNet(){
		return getNet(new NetAbility());
	}
	
	public NetAbility getNet(NetAbility a){
		a.setRange(range);
		a.setId(getId());
		a.setName(getName());
		a.setTargets(getTargets());
		return a;
	}
	
	@Override
	public Ability clone(){
		try {
			Ability a = (Ability) super.clone();
			a.casted = false;
			a.casting = false;
			return a;
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public List<Validator> getValidators() {
		return validators;
	}
	
	public abstract void cast(Entity caster, Entity target);
	public abstract void cast(Entity caster, Position target);
	
	public void validate(Entity caster, Entity target) throws InvalidOrderException{
		for(Validator v : validators){
			v.validate(caster, target);
		}
	}
	
	public void validate(Entity caster, Position target) throws InvalidOrderException{
		for(Validator v : validators){
			v.validate(caster, target);
		}
	}
	
	public String toString(){
		return getName();
	}
	
	public String getId() {
		return this.getClass().getSimpleName();
	}
	
	public String getName(){
		return name;
	}
	
	public void setName(String n){
		name = n;
	}

	public void setRange(int range) {
		this.range = range;
	}

	public int getRange() {
		return range;
	}

	public abstract interface Validator{
		abstract public void validate(Entity caster, Entity target) throws InvalidOrderException;
		abstract public void validate(Entity caster, Position target) throws InvalidOrderException;
	}
	
	public void setCasting(){
		casting = true;
		log.trace("Ability \"" + getName() + "\" casting");
	}
	
	public boolean isCasting(){
		return casting;
	}
	
	public void setCasted(){
		casted = true;
		log.trace("Ability " + getName() + " casted");
	}
	
	public boolean isCasted(){
		return casted;
	}
	
	@Override
	public boolean equals(Object other){
		if(other instanceof Ability){
			Ability o = (Ability) other;
			if(o.getId().equals(getId())){
				if(o.getName().equals(getName())){
					return true;
				}
			}
		}
		return false;
	}

	public void addTarget(validTarget target) {
		targets.add(target);
	}

	public List<validTarget> getTargets() {
		return targets;
	}

}
