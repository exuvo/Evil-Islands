package se.exuvo.evil.server.world;

import java.util.HashSet;
import java.util.Set;

import se.exuvo.evil.server.world.components.CollisionShape.CollisionLayer;
import se.exuvo.evil.shared.world.NetSquare;

import com.artemis.Entity;
import com.artemis.utils.Bag;

public class Square {
	public static final int size = 20;
	private int cost = 10;
	private Set<CollisionLayer> layers = new HashSet<CollisionLayer>();//Terrain properties
	private Bag<Entity> buildings = new Bag<Entity>();//Buildings that cover this tile
	
	public Square(){
	}
	
	public NetSquare getNet(){
		NetSquare n = new NetSquare();
		n.setCost(getCost());
		n.setSize(size);
		return n;
	}
	
	public void setCost(int cost){
		this.cost = cost;
	}
	
	public int getCost(){
		return cost;
	}

	public Set<CollisionLayer> getLayers() {
		return layers;
	}

	public void addLayer(CollisionLayer layer) {
		layers.add(layer);
	}
	
	public void removeLayer(CollisionLayer layer) {
		layers.remove(layer);
	}

	public Bag<Entity> getBuildings() {
		return buildings;
	}

	public void addBuilding(Entity building) {
		buildings.add(building);
	}
	
	public void removeBuilding(Entity building) {
		buildings.remove(building);
	}
	
	public void clearBuildings(){
		buildings.clear();
	}

}
