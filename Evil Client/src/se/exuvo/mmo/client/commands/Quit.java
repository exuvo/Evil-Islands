package se.exuvo.mmo.client.commands;

import java.util.ArrayList;
import java.util.List;

public class Quit extends Command {

	@Override
	public String execute(String[] tokens) {
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

}
