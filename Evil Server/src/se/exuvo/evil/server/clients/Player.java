package se.exuvo.evil.server.clients;

import org.apache.log4j.Logger;

import se.exuvo.evil.server.world.Island;
import se.exuvo.evil.server.world.o.units.Genius;


public class Player {
	private static final Logger log = Logger.getLogger(Player.class);
	private String username;
	private String password;
	private int id = -1;
	private Island island;
	private Genius genius;
	
	public Player(String username, String password, int id){
		setUsername(username);
		setPassword(password);
		this.id = id;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUsername() {
		return username;
	}

	public long getID() {
		return id;
	}

	public void setIsland(Island zone) {
		this.island = zone;
	}

	public Island getIsland() {
		return island;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword() {
		return password;
	}
	
	public void setGenius(Genius genius) {
		this.genius = genius;
	}

	public Genius getGenius() {
		return genius;
	}
	
	@Override
	public String toString(){
		return username;
	}
	
	@Override
	public int hashCode(){
		return (int) id;
	}

}



