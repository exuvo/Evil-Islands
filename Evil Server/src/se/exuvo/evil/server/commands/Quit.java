package se.exuvo.evil.server.commands;

import java.util.ArrayList;
import java.util.List;

import se.exuvo.evil.server.clients.Client;
import se.exuvo.evil.server.clients.Client.Access;

public class Quit extends Command {

	@Override
	public String execute(String[] tokens, Client c, String phrase) {
		log.fatal("### Exiting ###");
		System.exit(0);
		return null;
	}

	@Override
	public String getName() {
		return "quit";
	}

	@Override
	public String getDescription() {
		return "Ends the program.";
	}

	@Override
	public List<String> getNames() {
		List<String> l = new ArrayList<String>();
		l.add(getName());
		l.add("exit");
		return l;
	}

	@Override
	public List<Access> getRequiredAccess() {
		List<Access> l = new ArrayList<Access>();
		l.add(Access.kami_sama);
		return l;
	}

}
