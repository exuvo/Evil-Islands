package se.exuvo.evil.server.commands;

import java.util.ArrayList;
import java.util.List;

import se.exuvo.evil.server.Init;
import se.exuvo.evil.server.clients.Client;
import se.exuvo.evil.server.clients.Client.Access;



public class Version extends Command {

	@Override
	public String execute(String[] tokens, Client c, String phrase) {
		return "Current version is " + Init.serialVersionUID;
	}

	@Override
	public String getName() {
		return "version";
	}

	@Override
	public String getDescription() {
		return "Prints server version.";
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
