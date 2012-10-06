package se.exuvo.evil.server.world;

import java.util.concurrent.*;

import se.exuvo.evil.server.Settings;


public class ThreadHandler {
	private static ScheduledExecutorService executor = Executors.newScheduledThreadPool(Settings.getInt("threads"));
	
	public ThreadHandler(){
	}
	
	public static ScheduledFuture<?> add(Island z){
		return executor.scheduleAtFixedRate(z, 0, 1000/Settings.getInt("tickrate"), TimeUnit.MILLISECONDS);
	}
	
	public static Future<?> add(Runnable r){
		return executor.submit(r);
	}
	
	public static ScheduledExecutorService getExecutor(){
		return executor;
	}
}
