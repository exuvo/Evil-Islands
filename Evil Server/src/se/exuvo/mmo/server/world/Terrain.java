package se.exuvo.mmo.server.world;

import java.util.List;

import org.newdawn.slick.util.pathfinding.PathFindingContext;
import org.newdawn.slick.util.pathfinding.TileBasedMap;

import se.exuvo.mmo.shared.world.Position;

public class Terrain implements TileBasedMap {
	private Square[][] squares;
	
	
	public Terrain(int width, int height){
		squares = new Square[width][height];
		
		Square square;
		for(int a= 0;a<width;a++){
			for(int b= 0;b<height;b++){
				square = new Square();
				squares[a][b] = square;
			}
		}
	}
	
	@Override
	public int getWidthInTiles() {
		return squares.length;
	}

	@Override
	public int getHeightInTiles() {
		return squares[0].length;
	}

	@Override
	public void pathFinderVisited(int x, int y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean blocked(PathFindingContext context, int tx, int ty) {
		Movable m = (Movable) context.getMover();
		Position pos = new Position(tx*Square.size + Square.size/2, ty*Square.size + Square.size/2);
		
		//TODO check if it is a door and then if it would open for us
		//TODO might fail if more that 1 thingy there?
		if(m.isCollidingWithSomethingAtPos(pos)){
			Collidable c = m.isCollidingWithCollidableAtPos(pos);
			if(c != null && c instanceof Door){
					Door d = (Door) c;
					if(d.getLockLevel() == Door.OPEN || d.getLockLevel() == Door.CLOSED && d.getOwnerID() == m.getOwnerID()){
						return false;
					}
			}else{
				return true;
			}
		}
		return false;
	}

	@Override
	public float getCost(PathFindingContext context, int tx, int ty) {
		//TODO "penalizing nodes that lie along the paths of nearby moving units" http://www.policyalmanac.org/games/aStarTutorial.htm
		float base = (float) (squares[tx][ty].getCost() * Math.sqrt(Math.pow(tx-context.getSourceX(), 2) + Math.pow(ty-context.getSourceY(), 2)));
		
		Position pos = new Position(tx*Square.size, ty*Square.size);
		Collidable c = (Collidable) context.getMover();
		List<Unit> l = getUnitsInRangeOfPos(pos, c.getShape().getBoundingCircleRadius()+Square.size/2);
		base += l.size();
		
		return base;
	}

}
