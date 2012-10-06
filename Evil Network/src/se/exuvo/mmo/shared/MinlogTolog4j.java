package se.exuvo.mmo.shared;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.esotericsoftware.minlog.Log;

public class MinlogTolog4j extends Log.Logger{
	private static final Logger log = Logger.getLogger(MinlogTolog4j.class);

	@Override
	 public void log(int level, String category, String message, Throwable ex) {
        StringBuilder builder = new StringBuilder(256);

        if (category != null) {
                builder.append('[');
                builder.append(category);
                builder.append("] ");
        }

        builder.append(message);

        if (ex != null) {
        	log.log(getPriority(level), builder, ex);
        }else{
        	log.log(getPriority(level), builder);
        }
        
       

        print(builder.toString());
	}
	
	public Level getPriority(int level){
		 switch (level) {
	        case Log.LEVEL_ERROR:
	                return Level.ERROR;
	        case Log.LEVEL_WARN:
	        		return Level.WARN;
	        case Log.LEVEL_INFO:
	        		return Level.INFO;
	        case Log.LEVEL_DEBUG:
	        		return Level.DEBUG;
	        case Log.LEVEL_TRACE:
	        		return Level.TRACE;
	        }
		 return Level.ALL;
	}



}
