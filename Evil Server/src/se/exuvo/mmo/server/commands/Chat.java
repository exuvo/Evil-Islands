package se.exuvo.mmo.server.commands;

import java.util.ArrayList;
import java.util.List;

import se.exuvo.mmo.server.clients.Client;
import se.exuvo.mmo.server.clients.Client.Access;
import se.exuvo.mmo.shared.connection.Message;



public class Chat extends Command {

	@Override
	public String execute(String[] tokens, Client c, String phrase) {
		String message = "";
		for(int i=1;i<tokens.length;i++){
			message += tokens[i];
		}
		
		synchronized (Client.getClients()) {
			for(Client cc : Client.getClients()){
				if(c.getZone() == cc.getZone()){
					synchronized (cc.getLock()) {
						cc.getConnection().sendTCP(new Message(c.getUsername(), c.getId(), message));
					}
				}
			}
		}
		return "";
	}

	@Override
	public String getName() {
		return "chat";
	}

	@Override
	public String getDescription() {
		return "Chat with other clients. Defaults to those in the same zone.";
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
		return l;
	}

}
