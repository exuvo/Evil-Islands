package se.exuvo.mmo.client.world.abilities;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import se.exuvo.mmo.client.Init;
import se.exuvo.mmo.client.world.Entity;
import se.exuvo.mmo.shared.ClassFinder;
import se.exuvo.mmo.shared.connection.InvalidOrderException;
import se.exuvo.mmo.shared.world.NetAbility;
import se.exuvo.mmo.shared.world.NetAbility.validTarget;
import se.exuvo.mmo.shared.world.NetOrder;
import se.exuvo.mmo.shared.world.Position;

public abstract class Ability implements Cloneable{
	protected static final Logger log = Logger.getLogger(Ability.class);
	private List<Validator> validators = new ArrayList<Validator>();
	private static List<Class<? extends Ability>> abilities = new ArrayList<Class<? extends Ability>>();
	private int range = -1;
	private String name, description;
	private String id = "";
	private List<validTarget> targets = new ArrayList<validTarget>();
	
	public static List<Class<? extends Ability>> getAbilities(){
		return abilities;
	}
	
	public String getId() {
		return id.equals("") ? this.getClass().getSimpleName(): id;
	}
	
	public void setId(String s){
		id = s;
	}
	
	public static void init(){
		abilities.clear();
		log.debug("Loading abilities");
		try {
			List<Class<?>> l = ClassFinder.getClasses("se.exuvo.mmo.client.world.abilities");
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
			log.warn("Failed to load ability",e);
		} catch (IOException e) {
			log.warn("Failed to load ability",e);
		}
	}
	
	public Ability(String name){
		this.name = name;
	}
	
	public Ability(NetAbility a){
		setRange(a.getRange());
		setName(a.getName());
		setDescription("");
		setId(a.getId());
		setTargets(a.getTargets());
	}
	
	public static Ability get(final NetAbility a){
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
		for(Class<? extends Ability> b : abilities){
			if(a.getId().startsWith(b.getSimpleName())){
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
		Ability c = new Ability(a){};
		c.setDescription("NO LOCAL VERSION");
		return c;
		//throw new InvalidAbilityException("Ability not found");
	}
	
	@Override
	public Ability clone(){
		try {
			return (Ability) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void setRange(int range) {
		this.range = range;
	}

	public int getRange() {
		return range;
	}
	
	public List<Validator> getValidators() {
		return validators;
	}
	
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
	
	public NetAbility getNet(){
		NetAbility n = new NetAbility(getId(), getName());
		n.setRange(getRange());
		n.setTargets(getTargets());
		return n;
	}
	 
	public void cast(Entity caster, Entity target){
		NetOrder o = new NetOrder(caster.getId(), getNet(), target.getId());
		Init.connection.getServer().issueOrder(o, true);
	}
	
	public void cast(Entity caster, Position target){
		NetOrder o = new NetOrder(caster.getId(), getNet(), target);
		Init.connection.getServer().issueOrder(o, true);
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public abstract interface Validator{
		abstract public void validate(Entity caster, Entity target) throws InvalidOrderException;
		abstract public void validate(Entity caster, Position target) throws InvalidOrderException;
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
	
	public void setTargets(List<validTarget> t) {
		targets = t;
	}

	public List<validTarget> getTargets() {
		return targets;
	}
	
	
}

class InvalidAbilityException extends Exception implements Serializable{
	private static final long serialVersionUID = 3539537492838628134L;

	public InvalidAbilityException(){
		super();
	}
	
	public InvalidAbilityException(Throwable t){
		super(t);
	}
	
	public InvalidAbilityException(String s){
		super(s);
	}
	
	public InvalidAbilityException(String s, Throwable t){
		super(s,t);
	}

}
