package se.exuvo.mmo.client.commands;

import java.util.List;

import org.apache.log4j.Logger;

public abstract class Command {
	public abstract String execute(String[] tokens);
	public abstract String getName();
	public abstract String getDescription();
	public abstract List<String> getNames();
	//public abstract List<String> getParameters();
	protected static Logger log = Logger.getLogger(Command.class);

}
