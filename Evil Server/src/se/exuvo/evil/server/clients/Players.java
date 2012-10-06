package se.exuvo.evil.server.clients;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.log4j.Logger;


public class Players {
	private static final Logger log = Logger.getLogger(Players.class);
	private static List<Player> players = new ArrayList<Player>();
	static AtomicLong counter = new AtomicLong(1);//TODO load counter from database
	
	public static synchronized boolean load(){
		return false;
	}
	
	public static synchronized boolean save(){
		return false;
	}
	
	public static synchronized Player newPlayer(String username, String password){
		Player p = new Player(username, password, counter.getAndIncrement());
		players.add(p);
		log.info("Registered a new player: \"" + username + "\"");
		return p;
	}
	
	public static synchronized Player getPlayer(String username){
		for(Player p : players){
			if(p.getUsername().equals(username)){
				return p;
			}
		}
		return null;
	}
	
	public static synchronized Player getPlayer(long id){
		for(Player p : players){
			if(p.getID() == id){
				return p;
			}
		}
		return null;
	}
	

}



