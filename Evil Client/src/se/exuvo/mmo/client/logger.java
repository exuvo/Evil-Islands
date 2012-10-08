package se.exuvo.mmo.client;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class logger {
	
	public static void reloadLogLvl(){
		Level level = Level.toLevel(Settings.getStr("loglvl"));
		/*for (Enumeration<?> it = org.apache.log4j.LogManager.getCurrentLoggers(); it.hasMoreElements(); ){
			Logger l = (Logger) it.nextElement();
			l.setLevel(level);
		}*/
		Logger.getLogger("se.exuvo").setLevel(level);
		org.apache.log4j.Logger.getLogger(logger.class).info("Changed log level to " + level);
	}
}
