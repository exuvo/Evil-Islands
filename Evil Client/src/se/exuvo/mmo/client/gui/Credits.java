package se.exuvo.mmo.client.gui;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.particles.ParticleSystem;
import org.newdawn.slick.particles.effects.FireEmitter;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

import se.exuvo.mmo.client.ResourceManager;

import TWLSlick.BasicTWLGameState;
import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.Label;
import de.matthiasmann.twl.Widget;


public class Credits extends BasicTWLGameState {
	private static final Logger log = Logger.getLogger(Credits.class);
	private Image background;
	private ParticleSystem system;
	
	private Button exitButton;
	private List<Label> labels = new ArrayList<Label>();
	private Widget w;
	
	public Credits(){
	}

	@Override
	public int getID() {
		return GUI.CREDITSSTATE;
	}
	
	
	
	@Override
    protected void createRootPane() {
        super.createRootPane();
                
        int x = GUI.container.getWidth() / 2;
		int y = 60;
		
		w = new Widget();
		w.setSize(300, GUI.container.getHeight() - (y+80));
		w.setPosition(x - w.getWidth()/2, y);
		
		Label l = new Label("--- PROGRAMMING ---");
		labels.add(l);
		w.add(l);
		
		l = new Label("exuvo");
		labels.add(l);
		w.add(l);
		
		l = new Label(" ");
		labels.add(l);
		w.add(l);
		
		l = new Label("--- ART ---");
		labels.add(l);
		w.add(l);
		
		l = new Label("torus (color blind)");
		labels.add(l);
		w.add(l);
		
		l = new Label(" ");
		labels.add(l);
		w.add(l);
		
		l = new Label("--- SOUND/MUSIC ---");
		labels.add(l);
		w.add(l);
		
		l = new Label("Megaman X, X3");
		labels.add(l);
		w.add(l);
		
		l = new Label("Arvale: Journey of Illusion");
		labels.add(l);
		w.add(l);
		
		l = new Label("Starcraft");
		labels.add(l);
		w.add(l);
		
		l = new Label(" ");
		labels.add(l);
		w.add(l);
		
		l = new Label("--- API ---");
		labels.add(l);
		w.add(l);
		
		l = new Label("Slick 2D");
		labels.add(l);
		w.add(l);
		
		l = new Label("TWL + Eforen");
		labels.add(l);
		w.add(l);
		
		l = new Label("Kryonet");
		labels.add(l);
		w.add(l);
		
		l = new Label(" ");
		labels.add(l);
		w.add(l);
		
		l = new Label("--- Other ---");
		labels.add(l);
		w.add(l);
		
		l = new Label("STKRTS by Vincent PIRAULT");
		labels.add(l);
		w.add(l);
		
		l = new Label(" ");
		labels.add(l);
		w.add(l);
		
		l = new Label("--- TESTING ---");
		labels.add(l);
		w.add(l);
		l = new Label("siretu?");
		labels.add(l);
		w.add(l);
		
		l = new Label("");
		labels.add(l);
		w.add(l);
		
		rootPane.add(w);
		
		exitButton = new Button("Return");
		exitButton.addCallback(new Runnable() {
			@Override
			public void run() {
				Menu.pick.play();
				GUI.game.enterState(GUI.MAINMENUSTATE, new FadeOutTransition(), new FadeInTransition());
			}
		});
		rootPane.add(exitButton);
    }
	
    @Override
    protected void layoutRootPane() {
    	exitButton.adjustSize();
    	exitButton.setPosition(w.getX() + w.getWidth()/2 - exitButton.getWidth()/2, w.getY() + w.getHeight() + 20 );
    	
    	int y = w.getY();
    	for(Label l : labels){
    		l.adjustSize();
    		l.setPosition(w.getX() +w.getWidth()/2 - l.getWidth()/2, y+=l.getHeight()*1.4);
    	}
    }
    
    @Override
    public void enter(GameContainer container, StateBasedGame game) throws SlickException{
    	super.enter(container, game);
    }
    
    @Override
	public void initResources(){
		//background = ResourceManager.getImage("");
		system = ResourceManager.getSystem("particle systems/space.xml");
	}

	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		GUI.game = game;	
		
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		//background.draw(0, 0, container.getWidth(), container.getHeight());
		for(int i=1;i < 4;i++){
			system.render(i*container.getWidth()/4, container.getHeight());
		}
	}

	private boolean exitSelected = false;
	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		int mouseX = container.getInput().getMouseX();
		int mouseY = container.getInput().getMouseY();
		
		boolean selected = exitButton.isInside(mouseX, mouseY);
		if(selected && !exitSelected){
			exitButton.requestKeyboardFocus();
			Menu.select.play();
		}
		exitSelected = selected;
		system.update(delta);
	}
	
	@Override
	public void mouseReleased(int button, final int x, final int y) {
    	if(button == Input.MOUSE_RIGHT_BUTTON){
    		GUI.game.enterState(GUI.MAINMENUSTATE, new FadeOutTransition(), new FadeInTransition());
    	}
	}
	
	@Override
    public void keyPressed(int key, char c) {
		if(key == Input.KEY_ESCAPE){
			GUI.game.enterState(GUI.MAINMENUSTATE, new FadeOutTransition(), new FadeInTransition());
		}else{
			if(!exitButton.hasKeyboardFocus()){
				if(key == Input.KEY_UP || key == Input.KEY_LEFT){
					rootPane.focusPrevChild();;
					Menu.select.play();
				}else if(key == Input.KEY_DOWN || key == Input.KEY_RIGHT){
					rootPane.focusNextChild();
					Menu.select.play();
				}
			}
		}	
	}

}
