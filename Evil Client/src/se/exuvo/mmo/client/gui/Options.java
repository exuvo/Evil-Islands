package se.exuvo.mmo.client.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;
import org.newdawn.slick.util.Log;

import TWLSlick.BasicTWLGameState;
import TWLSlick.TWLStateBasedGame;

import se.exuvo.mmo.client.Init;
import se.exuvo.mmo.client.ResourceManager;
import se.exuvo.settings.Settings;

import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.ComboBox;
import de.matthiasmann.twl.EditField;
import de.matthiasmann.twl.Label;
import de.matthiasmann.twl.ToggleButton;
import de.matthiasmann.twl.ValueAdjusterInt;
import de.matthiasmann.twl.Widget;
import de.matthiasmann.twl.model.SimpleChangableListModel;
import de.matthiasmann.twl.model.SimpleIntegerModel;

/**
 * Menu associated to the options.
 * 
 * @author Vince
 * 
 */
public class Options extends BasicTWLGameState {
	private static final Logger log = Logger.getLogger(Options.class);

	private Image background;
	private long keyDelay = 0;
	private int oldSelected = 0;

	// TWL
	private Widget w;
	private EditField server;
	private EditField frameLimit;
	private ToggleButton fullScreen;
	private ToggleButton vSync;
	private ToggleButton alwaysRender;
	private ToggleButton showFPS;
	private ComboBox<Resolution> resolutionCombo;
	private SimpleIntegerModel musicVolume;
	private SimpleIntegerModel soundVolume;
	private Button back;
	private Button apply;
	
	class Resolution implements Comparable<Resolution> {

		private int width;
		private int height;

		public Resolution(int width, int height) {
			this.width = width;
			this.height = height;
		}

		@Override
		public String toString() {
			return width + " X " + height;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof Resolution) {
				Resolution r = (Resolution) obj;
				return this.width == r.width && height == r.height;
			} else
				return false;
		}

		@Override
		public int compareTo(Resolution o) {
			if (o.getWidth() == width) {
				return this.height - o.getHeight();
			} else
				return this.width - o.getWidth();
		}

		public int getWidth() {
			return width;
		}

		public int getHeight() {
			return height;
		}
	}
	
	@Override
	public void enter(GameContainer container, StateBasedGame game) throws SlickException {
		super.enter(container,game);
		log.info("Entered Options state");
		
		server.setText(Settings.getStr("server"));
		for (int i = 0; i < resolutionCombo.getModel().getNumEntries(); i++) {
			if (resolutionCombo.getModel().getEntry(i).getWidth() == Settings.getInt("GUI.Width") && resolutionCombo.getModel().getEntry(i).getHeight() == Settings.getInt("GUI.Height")) {
				resolutionCombo.setSelected(i);
			}
		}
		fullScreen.setActive(Settings.getBol("GUI.Fullscreen"));
		frameLimit.setText("" + Settings.getInt("GUI.FrameLimit"));
		showFPS.setActive(Settings.getBol("GUI.ShowFPS"));
		alwaysRender.setActive(Settings.getBol("GUI.AlwaysRender"));
		vSync.setActive(Settings.getBol("GUI.VSync"));
		musicVolume.setValue((int) (Settings.getFloat("MusicVolume") * 100));
		soundVolume.setValue((int) (Settings.getFloat("SoundVolume") * 100));
		
	}

	@Override
	public void initResources() {
		background = ResourceManager.getImage("images/options_view_background.jpg");
	}

	@Override
    protected void createRootPane() {
		super.createRootPane();
		
		w = new Widget();
		w.setSize(300, 400);
		w.setPosition(GUI.container.getWidth() / 2 - w.getWidth()/2, GUI.container.getHeight()/2 - w.getHeight()/2);
		
		int y = 20;

		Label label = new Label("Server:");
		label.setPosition(20, y+10);
		w.add(label);

		server = new EditField();
		server.setSize(160, 16);
		server.setPosition(110, y);
		w.add(server);

		label = new Label("Resolution:");
		label.setPosition(20, (y+=40) + 10);
		w.add(label);

		resolutionCombo = new ComboBox<Resolution>();
		resolutionCombo.setSize(160, 20);
		resolutionCombo.setPosition(110, y);
		SimpleChangableListModel<Resolution> model = new SimpleChangableListModel<Resolution>();

		List<Resolution> array = new ArrayList<Resolution>();
		try {
			DisplayMode[] modes = Display.getAvailableDisplayModes();
			for (int i = 0; i < modes.length; i++) {
				DisplayMode d = modes[i];
				if (d.getWidth() >= 800 && d.getHeight() >= 600) {
					Resolution r = new Resolution(d.getWidth(), d.getHeight());
					if (!array.contains(r)) {
						array.add(r);
					}
				}
			}

			Resolution r = new Resolution(Settings.getInt("GUI.Width"), Settings.getInt("GUI.Height"));
			if (!array.contains(r)) {
				array.add(r);
			}
		} catch (LWJGLException e) {
			e.printStackTrace();
		}

		Collections.sort(array);
		for (int i = 0; i < array.size(); i++) {
			model.addElement(array.get(i));
		}

		resolutionCombo.setModel(model);
		w.add(resolutionCombo);

		label = new Label("FS:");
		label.setPosition(228, y+10);
		w.add(label);

		fullScreen = new ToggleButton();
		fullScreen.setTheme("checkbox");
		fullScreen.setSize(20, 20);
		fullScreen.setPosition(256, y);
		w.add(fullScreen);
		
		label = new Label("Frame Limit:");
		label.setPosition(20, (y+=40) + 10);
		w.add(label);
		
		frameLimit = new EditField();
		frameLimit.setSize(50, 16);
		frameLimit.setPosition(110, y);
		w.add(frameLimit);
		
		label = new Label("Show FPS:");
		label.setPosition(185, y + 10);
		w.add(label);
		
		showFPS = new ToggleButton();
		showFPS.setTheme("checkbox");
		showFPS.setSize(20, 20);
		showFPS.setPosition(256, y);
		w.add(showFPS);
		
		label = new Label("Always Render:");
		label.setPosition(20, (y+=40) + 10);
		w.add(label);
		
		alwaysRender = new ToggleButton();
		alwaysRender.setTheme("checkbox");
		alwaysRender.setSize(20, 20);
		alwaysRender.setPosition(125, y);
		
		w.add(alwaysRender);
		
		label = new Label("VSync:");
		label.setPosition(160, y + 10);
		w.add(label);
		
		vSync = new ToggleButton();
		vSync.setTheme("checkbox");
		vSync.setSize(20, 20);
		vSync.setPosition(210, y);
		w.add(vSync);
		
		label = new Label("Music:");
		label.setPosition(20, (y+=40) + 10);
		w.add(label);

		musicVolume = new SimpleIntegerModel(0, 100, 0);
		ValueAdjusterInt vai = new ValueAdjusterInt(musicVolume);
		vai.setSize(168, 20);
		vai.setPosition(108, y);
		w.add(vai);

		label = new Label("Sound:");
		label.setPosition(20, (y+=40) + 10);
		w.add(label);

		soundVolume = new SimpleIntegerModel(0, 100, 0);
		vai = new ValueAdjusterInt(soundVolume);
		vai.setSize(168, 20);
		vai.setPosition(108, y);
		w.add(vai);
		
		rootPane.add(w);

		apply = new Button("Apply");
		apply.addCallback(new Runnable() {
			@Override
			public void run() {
				Menu.pick.play();
				if(server.getText().isEmpty()){
					Settings.set("server", "exuvo.se:60050");
				}else{
					Settings.set("server", server.getText());
				}
				
				Settings.set("SoundVolume", ((float) soundVolume.getValue()) / 100);
				Settings.set("MusicVolume", ((float) musicVolume.getValue()) / 100);
				Settings.set("GUI.FrameLimit", Integer.parseInt(frameLimit.getText()));
				Settings.set("GUI.VSync", vSync.isActive());
				Settings.set("GUI.AlwaysRender", alwaysRender.isActive());
				Settings.set("GUI.ShowFPS", showFPS.isActive());
				//Settings.save();

				GUI.container.setTargetFrameRate(Settings.getInt("GUI.FrameLimit"));
				GUI.container.setVSync(Settings.getBol("GUI.VSync"));
				GUI.container.setAlwaysRender(Settings.getBol("GUI.AlwaysRender"));
				GUI.container.setShowFPS(Settings.getBol("GUI.ShowFPS"));
				GUI.container.setSoundVolume(Settings.getFloat("SoundVolume"));
				GUI.container.setMusicVolume(Settings.getFloat("MusicVolume"));
				
				
				final int width = Settings.getInt("GUI.Width");
				final int height = Settings.getInt("GUI.Height");
				final boolean fullscreen = Settings.getBol("GUI.Fullscreen");
				
				Resolution r = getSelectedResolution();
				if (r != null) {
					Settings.set("GUI.Width", r.getWidth());
					Settings.set("GUI.Height", r.getHeight());
				}
				Settings.set("GUI.Fullscreen", fullScreen.isActive());
				
				if(width != Settings.getInt("GUI.Width") || height != Settings.getInt("GUI.Height") || fullscreen != Settings.getBol("GUI.Fullscreen")){
					try {
						log.info("Changing dislay mode to " + r);
						GUI.container.setDisplayMode(Settings.getInt("GUI.Width"), Settings.getInt("GUI.Height"), Settings.getBol("GUI.Fullscreen"));
						
						log.info("Reloading GUI: TWL");
						for (int i = 1; i <= Init.getGUI().getStateCount(); i++) {
							((BasicTWLGameState)Init.getGUI().getState(i)).reloadRootPane();
						}
						((TWLStateBasedGame)GUI.game).reloadGUI(rootPane);
					} catch (SlickException e) {
						log.warn("Failed to change display mode", e);
						//Failed to init with new settings, reverting
						Settings.set("GUI.Width", width);
						Settings.set("GUI.Height", height);
						Settings.set("GUI.Fullscreen", fullscreen);
						try {
							GUI.container.setDisplayMode(Settings.getInt("GUI.Width"), Settings.getInt("GUI.Height"), Settings.getBol("GUI.Fullscreen"));
							((BasicTWLGameState)GUI.game.getCurrentState()).disp("Failed to change display mode, reverted to old.");
						} catch (SlickException e1) {
							log.fatal("Error", e1);
							System.exit(5);
						}
					}
				}
			}
		});
		rootPane.add(apply);
		
		back = new Button("Back");
		back.addCallback(new Runnable() {
			@Override
			public void run() {
				Menu.pick.play();
				GUI.game.enterState(GUI.MAINMENUSTATE, new FadeOutTransition(), new FadeInTransition());
			}
		});
		rootPane.add(back);
	}
	
	@Override
    protected void layoutRootPane() {
		back.adjustSize();
		apply.adjustSize();
		back.setPosition(w.getX() + w.getWidth()/2 - back.getWidth(), w.getY() + w.getHeight() + 15 );
		apply.setPosition(back.getX() + back.getWidth(), back.getY());
    }

	public Resolution getSelectedResolution() {
		if (resolutionCombo.getSelected() != -1) {
			return (Resolution) ((SimpleChangableListModel<Resolution>) resolutionCombo.getModel()).getEntry(resolutionCombo.getSelected());
		}
		return null;
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		background.draw(0, 0, container.getWidth(), container.getHeight());
	}

	@Override
	public int getID() {
		return GUI.OPTIONSSTATE;
	}

	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		GUI.game = game;
	}
	
	@Override
    public void keyPressed(int key, char c) {
		if(key == Input.KEY_ESCAPE ){
			GUI.game.enterState(GUI.MAINMENUSTATE, new FadeOutTransition(), new FadeInTransition());
		}
		if(System.currentTimeMillis() - 300 > keyDelay){
			if(key == Input.KEY_UP || key == Input.KEY_LEFT){
				rootPane.focusPrevChild();;
				keyDelay = System.currentTimeMillis();
				Menu.select.play();
			}else if(key == Input.KEY_DOWN || key == Input.KEY_RIGHT){
				rootPane.focusNextChild();
				keyDelay = System.currentTimeMillis();
				Menu.select.play();
			}
		}
	}
	
	@Override
	public void mouseReleased(int button, final int x, final int y) {
    	if(button == Input.MOUSE_RIGHT_BUTTON){
    		GUI.game.enterState(GUI.MAINMENUSTATE, new FadeOutTransition(), new FadeInTransition());
    	}
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)throws SlickException {
		int selected = 0;
		int mouseX = container.getInput().getMouseX();
		int mouseY = container.getInput().getMouseY();
		
		if(apply.isInside(mouseX, mouseY))selected = 1;
		if(back.isInside(mouseX, mouseY))selected = 2;
		
		if(selected != oldSelected && selected > 0){
			Menu.select.play();
		}
		oldSelected = selected;
	}

}
