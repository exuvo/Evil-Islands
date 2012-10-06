package se.exuvo.evil.shared.world;

import java.util.ArrayList;
import java.util.List;

import com.esotericsoftware.kryo.NotNull;

public class NetEntity{
	private @NotNull String name;
	private long id;
	private long owner;
	private int hp;
	private int maxHP;
	private @NotNull List<NetAbility> abilities = new ArrayList<NetAbility>();
	private @NotNull Position position;
	private @NotNull String type = "";
	/*
	 * Nån lista som innehåller behaviors?, en med abilities (orders den kan ta)
	 */
	
	public NetEntity(){
		
	}
	
	public static final long WORLD = 0;


	public void setOwner(long owner) {
		this.owner = owner;
	}

	public long getOwner() {
		return owner;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	public Position getPosition() {
		return position;
	}

	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	public void addAbility(NetAbility name){
		abilities.add(name);
	}

	public void setAbilities(List<NetAbility> abilities) {
		this.abilities = abilities;
	}

	public List<NetAbility> getAbilities() {
		return abilities;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public void setHP(int hp) {
		this.hp = hp;
	}

	public int getHP() {
		return hp;
	}

	public void setMaxHP(int maxHP) {
		this.maxHP = maxHP;
	}

	public int getMaxHP() {
		return maxHP;
	}

}
