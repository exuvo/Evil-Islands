package se.exuvo.evil.shared.world;

import java.util.ArrayList;
import java.util.List;

import com.esotericsoftware.kryo.NotNull;


public class Snapshot{
	private @NotNull List<NetEntity> entities = new ArrayList<NetEntity>();
	private @NotNull List<Long> deletedEntites = new ArrayList<Long>();
	

	public Snapshot(final List<NetEntity> a){
		entities=a;
	}
	
	public Snapshot(final List<NetEntity> a, final List<Long> d){
		entities=a;
		deletedEntites = d;
	}
	
	public Snapshot(){
	}

	public List<NetEntity> getEntities() {
		return entities;
	}

	public List<Long> getDeletedEntites() {
		return deletedEntites;
	}
}
