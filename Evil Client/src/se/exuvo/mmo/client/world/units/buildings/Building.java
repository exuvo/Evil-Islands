package se.exuvo.mmo.client.world.units.buildings;


import se.exuvo.mmo.client.world.Unit;
import se.exuvo.mmo.shared.world.NetBuilding;

public class Building extends Unit {
	
	public Building(){
		super();
	}
	
	public Building(NetBuilding n){
		super(n);
	}
	
	@Override
	public boolean equals(Object other){
		if(other instanceof Building){
			Building c = (Building) other;
			if(super.equals(c)){
				return true;
			}
		}
		return false;
	}
	
}
