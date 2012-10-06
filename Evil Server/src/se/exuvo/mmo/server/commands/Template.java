package se.exuvo.mmo.server.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import se.exuvo.mmo.server.clients.Client;
import se.exuvo.mmo.server.clients.Client.Access;


public class Template extends Command {

	@Override
	public String execute(String[] tokens, Client c, String phrase) {
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
		return Arrays.asList(getName());
	}

	@Override
	public List<Access> getRequiredAccess() {
		return Arrays.asList();
	}

}
