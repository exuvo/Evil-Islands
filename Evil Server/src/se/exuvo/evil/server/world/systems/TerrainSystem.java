package se.exuvo.evil.server.world.systems;

import se.exuvo.evil.server.world.Groups;
import se.exuvo.evil.server.world.Island;
import se.exuvo.evil.server.world.Square;
import se.exuvo.evil.server.world.components.Position;
import se.exuvo.evil.server.world.components.Velocity;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.managers.GroupManager;
import com.artemis.systems.EntityProcessingSystem;

public class TerrainSystem extends EntityProcessingSystem {
	@Mapper ComponentMapper<Velocity> vm;
	@Mapper ComponentMapper<Position> pm;
	GroupManager gm = world.getManager(GroupManager.class);

	@SuppressWarnings("unchecked")
	public TerrainSystem() {
		super(Aspect.getAspectForAll(Velocity.class, Position.class));
	}
	
	//TODO check for added/removed entities
	protected void process(Entity e){
		if(gm.isInGroup(e, Groups.BUILDINGS)){
			Position p = pm.get(e);
			Velocity v = vm.get(e);
			
			SquarePos p1 = new SquarePos(p);
			SquarePos p2 = new SquarePos(v.getNext());
			
			if(!p1.eq(p2)){//Building has moved from one square to another
				Island island = (Island) world;
				island.getTerrain().getSquares()[p1.x][p1.y].removeBuilding(e);
				island.getTerrain().getSquares()[p2.x][p2.y].addBuilding(e);
			}
		}
	}
	
	public static class SquarePos{
		int x,y;
		
		public SquarePos(Position pos){
			x = (int) (pos.getX()/Square.size);
			y = (int) (pos.getY()/Square.size);
		}
		
		public boolean eq(SquarePos p){
			return x == p.x && y == p.y;
		}
	}

}
