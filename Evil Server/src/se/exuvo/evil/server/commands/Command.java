package se.exuvo.evil.server.commands;

import java.util.List;

import org.apache.log4j.Logger;

import se.exuvo.evil.server.clients.Client;
import se.exuvo.evil.server.clients.Client.Access;

public abstract class Command {
	public abstract String execute(String[] tokens, Client c, String phrase);
	public abstract String getName();
	public abstract String getDescription();
	public abstract List<String> getNames();
	public abstract List<Access> getRequiredAccess();
	//public abstract List<String> getParameters();
	protected static Logger log = Logger.getLogger(Command.class);

}
