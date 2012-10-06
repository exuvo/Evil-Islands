package se.exuvo.evil.server.world;

import java.security.InvalidParameterException;
import java.util.List;

import org.newdawn.slick.util.pathfinding.PathFindingContext;
import org.newdawn.slick.util.pathfinding.TileBasedMap;

import se.exuvo.evil.server.world.components.DoorComponent;
import se.exuvo.evil.server.world.components.PathComponent;
import se.exuvo.evil.shared.world.Position;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.World;
import com.artemis.managers.PlayerManager;

public class Terrain implements TileBasedMap {
	private Square[][] squares;
	private World world;
	PlayerManager pm = world.getManager(PlayerManager.class);
	ComponentMapper<DoorComponent> dm;

	public Terrain(int width, int height, World world) {
		if (width < 1 || height < 1) {
			throw new InvalidParameterException("Why would you create terrain with no terrain?!");
		}

		squares = new Square[width][height];

		Square square;
		for (int a = 0; a < width; a++) {
			for (int b = 0; b < height; b++) {
				square = new Square();
				squares[a][b] = square;
			}
		}

		this.world = world;
		dm = ComponentMapper.getFor(DoorComponent.class, world);
	}

	public Square[][] getSquares() {
		return squares;
	}

	@Override
	public int getWidthInTiles() {
		return squares.length;
	}

	@Override
	public int getHeightInTiles() {
		return squares[0].length;
	}

	public int getWidth() {
		return getWidthInTiles() * Square.size;
	}

	public int getHeight() {
		return getHeightInTiles() * Square.size;
	}

	@Override
	public void pathFinderVisited(int x, int y) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean blocked(PathFindingContext context, int tx, int ty) {
		PathComponent m = (PathComponent) context.getMover();

		for (int i = 0; i < squares[tx][ty].getBuildings().size(); i++) {
			Entity e = squares[tx][ty].getBuildings().get(i);

			if (pm.getPlayer(m.getOwner()).hashCode() != pm.getPlayer(e).hashCode()) {
				return true;
			} else {
				DoorComponent d = dm.get(e);
				if (d != null) {
					if (d.getLockLevel() == DoorComponent.State.LOCKED || d.getLockLevel() == DoorComponent.State.GUARDED) {
						return false;
					}
				}
			}
		}

		return false;
	}

	@Override
	public float getCost(PathFindingContext context, int tx, int ty) {
		// TODO "penalizing nodes that lie along the paths of nearby moving units" http://www.policyalmanac.org/games/aStarTutorial.htm
//		float base = (float) (squares[tx][ty].getCost() * Math.sqrt(Math.pow(tx - context.getSourceX(), 2)
//				+ Math.pow(ty - context.getSourceY(), 2)));

//		Position pos = new Position(tx*Square.size, ty*Square.size);
//		Collidable c = (Collidable) context.getMover();
//		List<Unit> l = getUnitsInRangeOfPos(pos, c.getShape().getBoundingCircleRadius()+Square.size/2);
//		base += l.size();

		return squares[tx][ty].getCost();
	}

}
