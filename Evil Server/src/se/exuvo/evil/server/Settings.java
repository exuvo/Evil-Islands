package se.exuvo.evil.server;

import com.martiansoftware.jsap.JSAPResult;

public class Settings extends se.exuvo.settings.Settings{
	
	public Settings(JSAPResult conf){
		add("loglvl", "INFO");
		add("threads", Runtime.getRuntime().availableProcessors() + 1);
		add("port", 60050);
		add("tickrate", 10);
		add("console", false);
		//add("timeout", 10000);
		
		if(!start(conf,"evil server")){
			log.fatal("Failed to read settings from file, please fix. Exiting.");
			System.exit(1);
		}
		
		logger.reloadLogLvl();
	}
	

	
}
