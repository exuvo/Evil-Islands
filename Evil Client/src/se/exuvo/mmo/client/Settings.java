package se.exuvo.mmo.client;

import com.martiansoftware.jsap.JSAPResult;

public class Settings extends se.exuvo.settings.Settings{
	
	public Settings(JSAPResult conf){
		add("loglvl", "INFO");
		add("timeout", 10000);
		add("autoConnect", false);
		add("server","exuvo.se:60050");
		add("GUI.FrameLimit",60);
		add("GUI.Width",1024);
		add("GUI.Height",768);
		add("GUI.Fullscreen",false);
		add("GUI.VSync",false);
		add("GUI.AlwaysRender",true);
		add("GUI.ShowFPS", true);
		add("SoundVolume",1f);
		add("MusicVolume",0.8f);
		
		if(!start(conf,"mmoClient")){
			log.fatal("Failed to read settings from file, please fix. Exiting.");
			System.exit(2);
		}
		
		logger.reloadLogLvl();
	}
	

	
}
