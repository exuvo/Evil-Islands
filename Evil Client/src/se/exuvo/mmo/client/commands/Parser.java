package se.exuvo.mmo.client.commands;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import se.exuvo.mmo.shared.ClassFinder;





public class Parser {
	private static final Logger log = Logger.getLogger(Parser.class);
	private static List<Command> commands = new ArrayList<Command>();
	
	public static void init(){
		commands.clear();
		log.debug("Loading commands");
		try {
			List<Class<?>> l = ClassFinder.getClasses("se.exuvo.mmo.client.commands");
			for(Class<?> c : l){
				if(Command.class.isAssignableFrom(c) && !c.equals(Command.class)){
					try {
						Class<? extends Command> cc = c.asSubclass(Command.class);
						Command p = cc.newInstance();
						if(p.getName() != null && !p.getName().equals("")){
							commands.add(p);
							log.trace("Loaded command: " + p.getName());
						}
					} catch (Throwable e) {
						log.warn("Failed to load command: \"" + c.getSimpleName() + "\"", e);
					} 
				}
			}
		} catch (ClassNotFoundException e) {
			log.warn("Failed to load commands",e);
		} catch (IOException e) {
			log.warn("Failed to load commands",e);
		}
	}
	
	public String parse(String phrase){
		log.debug("Parsing Command:" + phrase);
		String delims = "[ ]+";
		String[] tokens = phrase.split(delims);
		
		return parsecommand(tokens,phrase) + '\n';
	}
	
	private String parsecommand(String[] tokens, String phrase){
		if(tokens[0].equals("help")){
			StringBuffer help = new StringBuffer();
			for(Iterator<Command> it = commands.iterator();it.hasNext();){
				Command c = it.next();
				help.append(c.getName() + ":\n");
				/*help.append(" Names:");
				for(Iterator<String> names = c.getNames().iterator();names.hasNext();){
					help.append(names.next() + "; ");
				}*/
				help.append("\n  " + c.getDescription());
				help.append("\n");
			}
			
			return help.toString();
		
		}else{
			for(Iterator<Command> it = commands.iterator();it.hasNext();){
				Command c = it.next();
				for(Iterator<String> names = c.getNames().iterator();names.hasNext();){
					if(tokens[0].equalsIgnoreCase(names.next())){
						return c.execute(tokens);
					}
				}
			}
		}
		
		return "Unknown command \nTo see a list of avaible commands enter \"help\"";
	}
}

