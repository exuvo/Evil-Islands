package se.exuvo.mmo.client.world;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Renderable;

import se.exuvo.mmo.client.commands.Command;
import se.exuvo.mmo.client.world.abilities.Ability;
import se.exuvo.mmo.shared.ClassFinder;
import se.exuvo.mmo.shared.world.NetUnit;

public class Unit extends Movable{
	private Ability attack;
	private static List<Class<? extends Unit>> types = new ArrayList<Class<? extends Unit>>();
	private HashMap<String, Renderable> animations = new HashMap<String, Renderable>();
	private String animationState = "stand";
	
	public static void init(){
		types.clear();
		log.debug("Loading units");
		try {
			List<Class<?>> l = ClassFinder.getClasses("se.exuvo.mmo.client.world.units");
			for(Class<?> c : l){
				try{
					if(Unit.class.isAssignableFrom(c) && !c.equals(Unit.class)){
						Class<? extends Unit> cc = c.asSubclass(Unit.class);
						types.add(cc);
						log.trace("Loaded unit: " + cc.getSimpleName());
					}
				} catch (Throwable e) {
					log.warn("Failed to load unit: \"" + c.getSimpleName() + "\"", e);
				}
			}
		} catch (ClassNotFoundException e) {
			log.warn("Failed to load units",e);
		} catch (IOException e) {
			log.warn("Failed to load units",e);
		}
		
		for(Class<? extends Unit> u : types){
			try {
				Constructor<? extends Unit> c = u.getDeclaredConstructor();
				c.setAccessible(true);
				Unit uu = Unit.class.cast(c.newInstance());
				uu.load();
			} catch (Exception e) {
				log.warn("Failed to initialize unit: \"" + u.getSimpleName() + "\"", e);
			}
		}
	}
	
	public Unit(){
		super();
	}
	
	public Unit(NetUnit n){
		super(n);
		if(n.getAttack() != null){
			setAttack(Ability.get(n.getAttack()));
		}
	}
	
	public static Unit get(NetUnit u) throws UnitTypeException{
		//log.debug("woo:" + u.getType());
		for(Class<? extends Unit> c : types){
			//log.debug(" \"" + c.getSimpleName() + "\" vs \"" + u.getType()+ "\"");
			if(c.getSimpleName().equals(u.getType())){
				try {
					//log.debug("yay");
					return Unit.class.cast(c.getConstructor(NetUnit.class).newInstance(u));
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InstantiationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		throw new UnitTypeException("Unit type not found");
	}

	public void setAttack(Ability attack) {
		this.attack = attack;
	}

	public Ability getAttack() {
		return attack;
	}

	public void draw(Graphics g) {
		Image i = (Image) getAnimations().get(getAnimationState());
		if(i != null){
			float radius = getShape().getBoundingCircleRadius();
			i = i.getScaledCopyKeepAspectRatio((int)radius+5, (int)radius+5);
			i.setRotation(getAngle() - 90);
			g.drawImage(i, getPosition().getX() - i.getWidth()/2, getPosition().getY() - i.getHeight()/2);
		}else{
			g.draw(getCollisionShape());
		}
	}
	
	public void load(){
	}

	public void setAnimationState(String animationState) {
		this.animationState = animationState;
	}

	public String getAnimationState() {
		return animationState;
	}

	public HashMap<String, Renderable> getAnimations() {
		return animations;
	}
	
}

class UnitTypeException extends Exception{
	public UnitTypeException(String string) {
		super(string);
	}

	private static final long serialVersionUID = 6122599684021639362L;
	
}