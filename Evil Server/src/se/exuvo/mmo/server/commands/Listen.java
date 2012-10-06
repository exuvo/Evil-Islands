package se.exuvo.mmo.server.commands;

import java.util.ArrayList;
import java.util.List;

import se.exuvo.mmo.server.Init;
import se.exuvo.mmo.server.clients.Client;
import se.exuvo.mmo.server.clients.Client.Access;


public class Listen extends Command {

	@Override
	public String execute(String[] tokens, Client c, String phrase) {
		if(tokens.length > 1){
			if(tokens[1].equals("true")){
				if(Init.listener.isListening()){
					return "Already listening for connections";
				}else{
					Init.listener.start();
					if(Init.listener.isListening()){
						return "Listening for connections";
					}else{
						return "Failed to start listening for connections, see log";
					}
				}
			}else if(tokens[1].equals("false")){
				if(!Init.listener.isListening()){
					return "Not listening for connections";
				}else{
					Init.listener.stop();
					if(!Init.listener.isListening()){
						return "Stopped listening for connections";
					}else{
						return "Failed to stop listening for connections, see log";
					}
				}
			}
		}
		
		if(!Init.listener.isListening()){
			return "Not listening for connections";
		}else{
			return "Listening for connections";
		}
	}

	@Override
	public String getName() {
		return "Listen";
	}

	@Override
	public String getDescription() {
		return "Enable/Disable listening for client connections. Usage: " + getName() + " true|false";
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
		l.add(Access.kami_sama);
		return l;
	}

}
