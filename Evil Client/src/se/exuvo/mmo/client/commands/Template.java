package se.exuvo.mmo.client.commands;

import java.util.ArrayList;
import java.util.List;


public class Template extends Command {

	@Override
	public String execute(String[] tokens) {
		return "";
	}

	@Override
	public String getName() {
		return "";
	}

	@Override
	public String getDescription() {
		return ".";
	}

	@Override
	public List<String> getNames() {
		List<String> l = new ArrayList<String>();
		l.add(getName());
		return l;
	}

}
