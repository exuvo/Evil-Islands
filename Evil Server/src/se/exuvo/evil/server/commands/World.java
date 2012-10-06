package se.exuvo.evil.server.commands;

import java.util.ArrayList;
import java.util.List;

import se.exuvo.evil.server.Init;
import se.exuvo.evil.server.clients.Client;
import se.exuvo.evil.server.clients.Client.Access;
import se.exuvo.evil.server.world.Island;
import se.exuvo.evil.server.world.ThreadHandler;


public class World extends Command {

	@Override
	public String execute(String[] tokens, Client c, String phrase) {
		if(tokens.length > 1){
			if(tokens[1].equals("pause")){
				for(Island z : se.exuvo.evil.server.world.Planet.getZones()){
					if(z.getScheduler() != null){
						z.getScheduler().cancel(false);
						z.setScheduler(null);
					}
				}
				return "World paused";
				
			}else if(tokens[1].equals("unpause")){
				for(Island z : se.exuvo.evil.server.world.Planet.getZones()){
					if(z.getScheduler() == null){
						synchronized (z.getLock()) {
							z.setScheduler(ThreadHandler.add(z));
						}
					}
				}
				return "World unpaused";
			}else if(tokens[1].equals("clients")){
				return "Connected clients: " + Client.getClients().size();
			}
		}
		return getDescription();
	}

	@Override
	public String getName() {
		return "World";
	}

	@Override
	public String getDescription() {
		return "Pause/Unpause the world.  Usage: " + getName() + " pause|unpause|clients";
	}

	@Override
	public List<String> getNames() {
		List<String> l = new ArrayList<String>();
		l.add(getName());
		return l;
	}

	@Override
	public List<Access> getRequiredAccess() {
		List<Access> l = new ArrayList<Access>();
		l.add(Access.moderator);
		return l;
	}

}

