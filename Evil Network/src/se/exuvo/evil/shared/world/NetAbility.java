package se.exuvo.evil.shared.world;

import java.util.List;

import com.esotericsoftware.kryo.NotNull;

public class NetAbility{
	private @NotNull String id;
	private @NotNull int range;
	private @NotNull String name;
	public static enum validTarget {self, entity, point};
	private @NotNull List<validTarget> targets;
	
	public NetAbility(){
		
	}
	
	public NetAbility(String id, String name){
		setId(id);
		setName(name);
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setRange(int range) {
		this.range = range;
	}

	public int getRange() {
		return range;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setTargets(List<validTarget> targets) {
		this.targets = targets;
	}

	public List<validTarget> getTargets() {
		return targets;
	}

}
