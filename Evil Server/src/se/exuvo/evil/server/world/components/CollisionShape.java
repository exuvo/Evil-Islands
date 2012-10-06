package se.exuvo.evil.server.world.components;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.newdawn.slick.geom.Shape;

import com.artemis.Component;

public class CollisionShape extends Component {
	private Shape shape;

	public enum CollisionLayer {
		Surface(1), Air(2), Water(3), Dirt(4), Stone(5), Excavated(6), Floor(7);
		private int id;

		CollisionLayer(int i) {
			id = i;
		}

		int get() {
			return id;
		}

		CollisionLayer get(int id) {
			for (CollisionLayer l : CollisionLayer.values()) {
				if (l.get() == id) {
					return l;
				}
			}
			return null;
		}
	}

	private Set<LayerCheck> layers = new HashSet<LayerCheck>();

	public Set<LayerCheck> getLayers() {
		return layers;
	}

	public void setLayers(Set<LayerCheck> layers) {
		this.layers = layers;
	}

	public void addLayer(LayerCheck l) {
		layers.add(l);
	}

	public Shape getShape() {
		return shape;
	}

	public void setShape(Shape shape) {
		this.shape = shape;
	}

	public class LayerCheck {
		private Set<CollisionLayer> layers = new HashSet<CollisionLayer>();
		private boolean requireAll, exclusion;

		/**
		 * @param requireAll If true and one layer check fails then the whole test fails. Otherwise if at least one check passes the whole
		 *            test passes.
		 * @param exclusion Invert the check.
		 */
		public LayerCheck(boolean requireAll, boolean exclusion) {
			this.requireAll = requireAll;
			this.exclusion = exclusion;
		}

		public Set<CollisionLayer> getLayers() {
			return layers;
		}

		public void setLayers(Set<CollisionLayer> layers) {
			this.layers = layers;
		}

		public LayerCheck addLayer(CollisionLayer l) {
			layers.add(l);
			return this;
		}

		public boolean isRequireAll() {
			return requireAll;
		}

		public boolean isExclusion() {
			return exclusion;
		}

	}

}
