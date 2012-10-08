package se.exuvo.mmo.client.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

import se.exuvo.mmo.client.Init;
import se.exuvo.mmo.client.ResourceManager;
import TWLSlick.BasicTWLGameState;
import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.Widget;


public class Menu extends BasicTWLGameState {
	private static final Logger log = Logger.getLogger(Menu.class);
	private Image background;

	public static Sound select;
	public static Sound pick;
	private int oldSelected = 0;
	private long keyDelay = 0;
	private boolean first = true;
	
	private Widget menuWidget;
	private List<Button> buttons = new ArrayList<Button>();
	
	public Menu(){
	}

	@Override
	public int getID() {
		return GUI.MAINMENUSTATE;
	}
	
	@Override
    protected void createRootPane() {
        super.createRootPane();
        buttons.clear();

		menuWidget = new Widget();
               
        Button b = new Button("Connect");
        b.addCallback(new Runnable(){
        	@Override public void run() {
        		try {
        			pick.play();
					Init.connection.connect();
					GUI.game.enterState(GUI.LOGINSTATE, null, null);
				}catch (IOException e) {
					disp("Connection Failure: " + e);
				} catch (Exception e) {
					disp("Connection Failure: " + e);
				}
        	}
        	});
        buttons.add(b);
        menuWidget.add(b);
        
        b = new Button("Options");
        b.addCallback(new Runnable(){
        	@Override public void run() {
        		pick.play();
        		GUI.game.enterState(GUI.OPTIONSSTATE, new FadeOutTransition(), new FadeInTransition());
        	}
        	});
        buttons.add(b);
        menuWidget.add(b);
        
        b = new Button("Credits");
        b.addCallback(new Runnable(){
        	@Override public void run() {
        		pick.play();
				GUI.game.enterState(GUI.CREDITSSTATE, new FadeOutTransition(), new FadeInTransition());
        	}
        	});
        buttons.add(b);
        menuWidget.add(b);
        
        b = new Button("Exit");
        b.addCallback(new Runnable(){
				public void run() {
					GUI.container.exit();
				}
			});
        buttons.add(b);
        menuWidget.add(b);
        
        rootPane.add(menuWidget);
    }
	
    @Override
    protected void layoutRootPane() {
		int maxWidth = 0;
        int totalHeight = 0;
        Button last = new Button(" ");
    	for(Button b : buttons){
    		b.adjustSize();
    		if(totalHeight != 0){
    			totalHeight+=b.getHeight()*2;
    		}else{
    			totalHeight+=b.getHeight();
    		}
    		
    		maxWidth = Math.max(maxWidth, b.getWidth());
    		last = b;
    	}
    	
    	menuWidget.setSize((int)(maxWidth*1.5), totalHeight + last.getHeight()*2);
		menuWidget.setPosition(GUI.container.getWidth()/2 - menuWidget.getWidth()/2, GUI.container.getHeight()/2 - (int)(menuWidget.getHeight()/1.5));
		
		int y = menuWidget.getY();
    	for(Button b : buttons){
    		if(y != menuWidget.getY()){
    			y+=b.getHeight()*2;
    		}else{
    			y+=b.getHeight();
    		}
    		b.setPosition(menuWidget.getX() +menuWidget.getWidth()/2 - b.getWidth()/2, y);
    	}
    }
    
    @Override
    public void enter(GameContainer container, StateBasedGame game) throws SlickException {
        super.enter(container, game);
        if(first && (container.getWidth() < 1024 || container.getHeight() < 768)){
        	disp("Recommended minimum screen size is 1024x768");
        }
        
        if(GUI.getBackgroundMusic() != ResourceManager.getMusic("music/Arvale/Journey of Illusion/title-ending.xm")){
        	GUI.setBackgroundMusic(ResourceManager.getMusic("music/Arvale/Journey of Illusion/title-ending.xm"));
    		GUI.getBackgroundMusic().loop();
        }
        first = false;
    }

    @Override
	public void initResources(){
		select = ResourceManager.getSound("sounds/Megaman X3/SE/SE_1E.SE.wav");//1e,1C
		pick = ResourceManager.getSound("sounds/Megaman X3/SE/SE_1C.SE.wav");//5A,,1D
		background = ResourceManager.getImage("images/Derelict_by_Radojavor.jpg");
	}
    
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		GUI.game = game;
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		background.draw(0, 0, container.getWidth(), container.getHeight());
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		int selected = 0;
		int mouseX = container.getInput().getMouseX();
		int mouseY = container.getInput().getMouseY();
		
		for(int i=0; i<buttons.size();i++){
			Button b = buttons.get(i);
			if(b.isInside(mouseX, mouseY)){
				selected = i+1;
				b.requestKeyboardFocus();
				break;
			}
		}
		
		if(selected != oldSelected && selected > 0){
			select.play();
		}
		oldSelected = selected;
	}
	
	@Override
	public void keyPressed(int key, char c) {
		if(System.currentTimeMillis() - 300 > keyDelay){
			if(key == Input.KEY_UP || key == Input.KEY_LEFT){
				rootPane.focusPrevChild();;
				keyDelay = System.currentTimeMillis();
				select.play();
			}else if(key == Input.KEY_DOWN || key == Input.KEY_RIGHT){
				rootPane.focusNextChild();
				keyDelay = System.currentTimeMillis();
				select.play();
			}
		}
	}

}
