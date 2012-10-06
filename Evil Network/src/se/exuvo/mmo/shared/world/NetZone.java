package se.exuvo.mmo.shared.world;

import java.util.ArrayList;
import java.util.List;

import com.esotericsoftware.kryo.NotNull;

public class NetZone{
	private @NotNull NetSquare[][] squares;
	private @NotNull List<NetEntity> entities = new ArrayList<NetEntity>();
	
	@SuppressWarnings("unused")
	private NetZone(){
	}
	
	public NetZone(int x, int y){
		squares = new NetSquare[x][y];
	}

	public NetSquare[][] getSquares() {
		return squares;
	}

	public List<NetEntity> getEntities() {
		return entities;
	}


}
