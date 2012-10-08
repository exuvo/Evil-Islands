package se.exuvo.mmo.client.gui;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.log4j.Logger;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import TWLSlick.TWLStateBasedGame;

import se.exuvo.settings.Settings;

public class GUI extends TWLStateBasedGame implements Runnable {
	private static final Logger log = Logger.getLogger(GUI.class);
	public static final int MAINMENUSTATE   = 1;
	public static final int LOGINSTATE      = 2;
	public static final int GAMESTATE       = 3;
	public static final int CREDITSSTATE    = 4;
	public static final int LOADINGSTATE	= 5;
	public static final int OPTIONSSTATE	= 6;
	
	public static AppGameContainer container;
	public static StateBasedGame game;
	private static Music backgroundMusic;
	private boolean running = false;
	
	public GUI(){
		super("MMO Client");
		
		addState(new Loading());
		addState(new Menu());
		addState(new Login());
		addState(new Game());
		addState(new Credits());
		addState(new Options());
		
		try {
			container = new AppGameContainer(this);
			container.setDisplayMode(Settings.getInt("GUI.Width"), Settings.getInt("GUI.Height"), Settings.getBol("GUI.Fullscreen"));
			container.setVSync(Settings.getBol("GUI.VSync"));
			container.setAlwaysRender(Settings.getBol("GUI.AlwaysRender"));
			container.setTargetFrameRate(Settings.getInt("GUI.FrameLimit"));
			container.setShowFPS(Settings.getBol("GUI.ShowFPS"));
		} catch (SlickException e) {
			log.error("Failed to init GUI", e);
		}
		new Thread(this).start();
	}
	
	public boolean isRunning(){
		return running;
	}
	
	public void run(){
		if(!running){
			try {
				running = true;
				//TODO fix multi threading
				//container.startThreaded();
				container.start();
				running = false;
			} catch (SlickException e) {
				running = false;
				log.error("Error in GUI", e);
			} catch(Throwable t){
				running = false;
				log.fatal("Error in GUI", t);
				System.exit(5);
			}
		}
	}
	
	@Override
	public void initStatesList(GameContainer container) throws SlickException {		
		container.setSoundVolume(Settings.getFloat("SoundVolume"));
		container.setMusicVolume(Settings.getFloat("MusicVolume"));
	}

	@Override
	protected URL getThemeURL() {
		try {
			return new File("data/ui/guiTheme.xml").toURI().toURL();
		} catch (MalformedURLException e) {
			log.fatal("Fail to find UI xml", e);
			System.exit(4);
		}
		return null;
	}

	public static void setBackgroundMusic(Music backgroundMusic) {
		if(GUI.backgroundMusic != null){
			GUI.backgroundMusic.stop();
		}
		GUI.backgroundMusic = backgroundMusic;
	}

	public static Music getBackgroundMusic() {
		return backgroundMusic;
	}

	
}
