package se.exuvo.evil.server.world.systems;

import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Transform;

import se.exuvo.evil.server.managers.PlayerManager;
import se.exuvo.evil.server.world.Groups;
import se.exuvo.evil.server.world.Island;
import se.exuvo.evil.server.world.Square;
import se.exuvo.evil.server.world.components.CollisionShape;
import se.exuvo.evil.server.world.components.CollisionShape.CollisionLayer;
import se.exuvo.evil.server.world.components.CollisionShape.LayerCheck;
import se.exuvo.evil.server.world.components.Position;
import se.exuvo.evil.server.world.components.Rotation;
import se.exuvo.evil.server.world.components.Velocity;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.managers.GroupManager;
import com.artemis.systems.IntervalEntitySystem;
import com.artemis.utils.Bag;
import com.artemis.utils.ImmutableBag;

public class CollisionSystem extends IntervalEntitySystem {
	@Mapper private ComponentMapper<Velocity> vm;
	@Mapper ComponentMapper<CollisionShape> cm;
	@Mapper ComponentMapper<Rotation> rm;
	private GroupManager gm;
	private PlayerManager pm;

	@SuppressWarnings("unchecked")
	public CollisionSystem() {
		super(Aspect.getAspectForAll(Velocity.class, CollisionShape.class, Rotation.class), 0.1f);
		gm = world.getManager(GroupManager.class);
		pm = world.getManager(PlayerManager.class);
	}

	@Override
	protected void processEntities(ImmutableBag<Entity> entities) {
		Island island = (Island) world;
		Bag<Collision> collisions = new Bag<CollisionSystem.Collision>();

		for (int i = 0; i < entities.size(); i++) {
			Entity e1 = entities.get(i);
			Position p1 = vm.get(e1).getNext();
			CollisionShape c1 = cm.get(e1);
			Rotation r1 = rm.get(e1);

			{// Terrain collision
				boolean terrainCheckPassed = true;

				for (LayerCheck lc : c1.getLayers()) {
					boolean layerCheckPassed = false;

					for (CollisionLayer cl : lc.getLayers()) {
						// TODO check all terrain occupied by Entity
						if (island.getTerrain().getSquares()[(int) p1.getX() / Square.size][(int) p1.getY() / Square.size].getLayers()
								.contains(cl)) {
							layerCheckPassed = true;
							if (!lc.isRequireAll()) {// Only 1 is necessary
								break;
							}
						} else {
							if (lc.isRequireAll()) {
								layerCheckPassed = false;
								break;
							}
						}
					}

					if (lc.isExclusion()) {
						layerCheckPassed = !layerCheckPassed;
					}

					if (!layerCheckPassed) {
						terrainCheckPassed = false;
						break;
					}
				}

				if (!terrainCheckPassed) {
					collisions.add(new Collision(e1));
				}
			}

			Shape s1 = getTranslatedCollisionShape(c1.getShape(), p1, r1.getAngle());

			// Map edge collision
			if (s1.getMinX() < 0 || s1.getMinY() < 0 || s1.getMaxY() > island.getTerrain().getSquares().length * Square.size
					|| s1.getMaxX() > island.getTerrain().getSquares()[0].length * Square.size) {

				collisions.add(new Collision(e1));// Out of bounds
			}

			// Entity collision
			for (int j = 0; j < entities.size(); j++) {
				Entity e2 = entities.get(j);
				if (e1 == e2)
					continue;

				Position p2 = vm.get(e2).getNext();
				CollisionShape c2 = cm.get(e2);
				Rotation r2 = rm.get(e2);

				// Collision check
				if (canCollide(e1, e2)) {
					Shape s2 = getTranslatedCollisionShape(c2.getShape(), p2, r2.getAngle());

					if (s1.getBoundingCircleRadius() + s2.getBoundingCircleRadius() < p1.distance(p2)) {// fast check
						// Not colliding
						// }else if(s2.getMaxX()<s1.getMinX() ||
						// s2.getMaxY()<s1.getMinY() ||
						// s2.getMinX()>s1.getMaxX() ||
						// s2.getMinY()>s1.getMaxY()){//fast check
						// //Not colliding
					} else {
						if (s1.intersects(s2) || s1.contains(s2)) {// careful check
							collisions.add(new Collision(e1, e2));
						}
						// TODO use SAT (Separating Axis Theorem)
						// http://www.codezealot.org/archives/55
						// to know how much to move already intersecting entities away from
						// each other.
					}
				}
			}
		}

		// Resolve collisions
		for (int i = 0; i < collisions.size(); i++) {
			Collision c = collisions.get(i);

			if (c.other == null) {
				// Stop planned movement due to collision there
				Velocity p = vm.get(c.self);
				p.setColliding(true);
			} else {
				// Move entities away from each other
			}

		}

	}

	public boolean canCollide(Entity e1, Entity e2) {
		if (gm.isInGroup(e1, Groups.BUILDINGS) || gm.isInGroup(e2, Groups.BUILDINGS)) {
			return true;
		}

		if (pm.getPlayer(e1).getID() == pm.getPlayer(e2).getID()) {
			return false;
		}

		return false;
	}

	public Shape getTranslatedCollisionShape(Shape s, Position p, float rotation) {
		return s.transform(Transform.createTranslateTransform(p.getX(), p.getY())).transform(Transform.createRotateTransform(rotation));
	}

	class Collision {
		public Entity self, other;// If other is null then collision was with

		// map edge.

		public Collision(Entity a, Entity b) {
			self = a;
			other = b;
		}

		public Collision(Entity a) {
			this(a, null);
		}
	}

}
