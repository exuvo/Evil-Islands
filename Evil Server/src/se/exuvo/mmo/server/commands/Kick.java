package se.exuvo.mmo.server.commands;

import java.util.Arrays;
import java.util.List;

import se.exuvo.mmo.server.clients.Client;
import se.exuvo.mmo.server.clients.Client.Access;



public class Kick extends Command {

	@Override
	public String execute(String[] tokens, Client c, String phrase) {
		if(tokens.length < 2){
			return "Give a name to kick or i'll kick you instead!";
		}
		int found = 0;
		synchronized (Client.getClients()) {
			for(Client cc : Client.getClients()){
				if(cc.getPlayer().getUsername().equals(tokens[1])){
					synchronized (cc.getLock()) {
						cc.getConnection().close();
					}
					found++;
				}
			}
		}
		if(found > 0){
			return "Kicked " + found + " clients";
		}else{
			return "Found no clients with that name";
		}
	}

	@Override
	public String getName() {
		return "kick";
	}

	@Override
	public String getDescription() {
		return "Disconnects a client";
	}

	@Override
	public List<String> getNames() {
		return Arrays.asList(getName());
	}

	@Override
	public List<Access> getRequiredAccess() {
		return Arrays.asList(Access.admin);
	}

}
