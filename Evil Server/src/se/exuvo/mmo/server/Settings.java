package se.exuvo.mmo.server;

import com.martiansoftware.jsap.JSAPResult;

public class Settings extends se.exuvo.settings.Settings{
	
	public Settings(JSAPResult conf){
		add("loglvl", "INFO");
		add("updateThreads",Runtime.getRuntime().availableProcessors()*2);
		add("port",60050);
		add("updateDelay",100);
		add("console", false);
		//add("timeout", 10000);
		
		if(!start(conf,"mmo")){
			log.fatal("Failed to read settings from file, please fix. Exiting.");
			System.exit(1);
		}
		
		logger.reloadLogLvl();
	}
	

	
}
