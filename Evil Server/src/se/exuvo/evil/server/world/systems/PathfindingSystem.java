package se.exuvo.evil.server.world.systems;

import org.newdawn.slick.util.pathfinding.AStarPathFinder;
import org.newdawn.slick.util.pathfinding.Path;
import org.newdawn.slick.util.pathfinding.PathFinder;

import se.exuvo.evil.server.world.Island;
import se.exuvo.evil.server.world.Square;
import se.exuvo.evil.server.world.components.PathComponent;
import se.exuvo.evil.server.world.components.Position;
import se.exuvo.evil.server.world.systems.TerrainSystem.SquarePos;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.IntervalEntityProcessingSystem;

public class PathfindingSystem extends IntervalEntityProcessingSystem {
	@Mapper ComponentMapper<Position> pm;
	@Mapper ComponentMapper<PathComponent> pfm;

	@SuppressWarnings("unchecked")
	public PathfindingSystem() {
		super(Aspect.getAspectForAll(Position.class, PathComponent.class), 1.0f);
	}

	@Override
	protected void process(Entity e) {
		Position p = pm.get(e);
		PathComponent f = pfm.get(e);
		
		f.setOwner(e);
		findPath(p, start, goal, mustReach);
	}
	
	public Path findPath(PathComponent p, Position start, Position goal, boolean mustReach){
		Island island = (Island) world;
		PathFinder f = new AStarPathFinder(island.getTerrain(), 100, false);
		SquarePos s = new SquarePos(start);
		SquarePos g = new SquarePos(goal);
		
		return f.findPath(p, s.x, s.y, g.x, g.y, mustReach);
	}

}
