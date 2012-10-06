package se.exuvo.mmo.server.world;

import java.util.concurrent.*;

import se.exuvo.mmo.server.Settings;


public class ThreadHandler {
	private static ScheduledExecutorService executor = Executors.newScheduledThreadPool(Settings.getInt("updateThreads"));
	
	public ThreadHandler(){
	}
	
	public static ScheduledFuture<?> add(Island z){
		return executor.scheduleAtFixedRate(z, 0, Settings.getInt("updateDelay"), TimeUnit.MILLISECONDS);
	}
	
	public static Future<?> add(Runnable r){
		return executor.submit(r);
	}
	
	public static ScheduledExecutorService getExecutor(){
		return executor;
	}
}
