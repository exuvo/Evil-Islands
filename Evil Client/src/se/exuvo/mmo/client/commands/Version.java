package se.exuvo.mmo.client.commands;

import java.util.ArrayList;
import java.util.List;

import se.exuvo.mmo.client.Init;



public class Version extends Command {

	@Override
	public String execute(String[] tokens) {
		return "Current version is " + Init.serialVersionUID;
	}

	@Override
	public String getName() {
		return "version";
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
