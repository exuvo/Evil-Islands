package se.exuvo.mmo.shared.world;

import java.util.HashMap;

import com.esotericsoftware.kryo.NotNull;

public class NetHero extends NetUnit{
	private @NotNull HashMap<String, Integer> stats = new HashMap<String, Integer>();
	
	public NetHero(){
		
	}
	
	public void setStats(HashMap<String,Integer> h){
		stats=h;
	}
	
	public HashMap<String,Integer> getStats(){
		return stats;
	}
}
