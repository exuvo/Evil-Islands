package se.exuvo.evil.server.world.components;

import java.util.List;

import org.newdawn.slick.geom.Shape;

import com.artemis.Component;

public class CollisionShape extends Component{
	private Shape shape;
	public enum CollisionLayer {Surface(1), Air(2), Water(3), Dirt(4), Stone(5), Excavated(6), Floor(7);
		private int id;
		
		CollisionLayer(int i){
			id = i;
		}
		
		int get(){
			return id;
		}
		
		CollisionLayer get(int id){
			for(CollisionLayer l : CollisionLayer.values()){
				if(l.get() == id){
					return l;
				}
			}
			return null;
		}
	}
	private List<CollisionLayer> layers;

	public List<CollisionLayer> getLayers() {
		return layers;
	}

	public void setLayers(List<CollisionLayer> layers) {
		this.layers = layers;
	}
	
	
	
}
