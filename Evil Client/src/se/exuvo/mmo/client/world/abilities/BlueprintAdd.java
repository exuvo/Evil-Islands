package se.exuvo.mmo.client.world.abilities;

import org.newdawn.slick.geom.Shape;

import se.exuvo.mmo.shared.world.NetAbility;
import se.exuvo.mmo.shared.world.NetBlueprintAdd;

public class BlueprintAdd extends Ability {
	private int cost;
	private Shape shape;
	
	/**
	 * Create new() and change <builds> to suit needs.
	 */
	public BlueprintAdd(NetAbility n){
		super(n);
		NetBlueprintAdd a = (NetBlueprintAdd) n;
		cost = a.getCost();
		shape = a.getShape();
	}
	
	public int getCost(){
		return cost;
	}
	
	public Shape getShape(){
		return shape;
	}

	
}
