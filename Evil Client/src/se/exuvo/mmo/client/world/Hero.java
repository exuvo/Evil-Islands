package se.exuvo.mmo.client.world;

import java.util.HashMap;

import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Vector2f;

import se.exuvo.mmo.shared.world.NetHero;
import se.exuvo.mmo.shared.world.NetMovable;
import se.exuvo.mmo.shared.world.Position;
import se.exuvo.mmo.shared.world.Behavior;

public class Hero extends Unit{
	private HashMap<String, Integer> stats = new HashMap<String, Integer>();
	//0-int,1-str,2-agi
	public Hero(){
		super();
	}
	
	public int readStat(int i){
		return hashToArray(stats)[i];
	}
	
	public Hero(NetHero h){
		super(h);
		this.stats=h.getStats();
	}
	
	@Override
	public boolean equals(Object other){
		if(other instanceof Hero){
			Hero m = (Hero) other;
			if(super.equals(m)){
				if(stats.equals(m.stats)){
					return true;
				}
			}
		}
		return false;
	}
	
	public int getStat(String s){
		return stats.get(s).intValue();
	}
	
	public int getStat(int i){
		return hashToArray(stats)[i];
	}
	
	public void setStats(HashMap<String,Integer> h){
		this.stats=(HashMap<String,Integer>)h.clone();
	}

	public int[] hashToArray(HashMap<String, Integer> h){
		return new int[]{h.get("int").intValue(),h.get("str").intValue(),h.get("agi").intValue()};
	}

	public HashMap<String, Integer> arrayToHash(int[] i){
		HashMap <String, Integer> h = new HashMap<String, Integer>();
		h.put("int",new Integer(i[0]));
		h.put("str",new Integer(i[1]));
		h.put("agi",new Integer(i[2]));
		return h;
	}
}