package se.exuvo.mmo.client.gui;

import org.apache.log4j.Logger;
import org.jasypt.digest.StandardStringDigester;
import org.jasypt.util.digest.Digester;
import org.jasypt.util.password.ConfigurablePasswordEncryptor;
import org.jasypt.util.text.BasicTextEncryptor;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;
import org.newdawn.slick.state.transition.VerticalSplitTransition;

import se.exuvo.mmo.client.Init;
import se.exuvo.mmo.client.ResourceManager;
import TWLSlick.BasicTWLGameState;

import com.esotericsoftware.kryonet.rmi.TimeoutException;

import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.EditField;
import de.matthiasmann.twl.Label;
import de.matthiasmann.twl.Widget;
import de.matthiasmann.twl.EditField.Callback;

public class Login extends BasicTWLGameState {
	private static final Logger log = Logger.getLogger(Login.class);
	private Image background;
	
	private Button login;
	private Button back;
	private Label label;
	private Label usernameLabel;
	private Label passwordLabel;
	private EditField username;
	private EditField password;
	private boolean first = true;
	private Widget window;
    
	private int mouseX, mouseY;
	private long keyDelay = 0;
	private int oldSelected = 0;

	public Login(){
	}

	@Override
	public int getID() {
		return GUI.LOGINSTATE;
	}
	
	private void connect(){
		try {
			Init.connection.login(username.getText(), password.getText());
			GUI.game.enterState(GUI.GAMESTATE, new FadeOutTransition(), new FadeInTransition());
		} catch (TimeoutException e) {
			disp("Connection Failure: " + e);
			log.warn("Failed to connect:",e);
			Init.connection.disconnect();
			GUI.game.enterState(GUI.MAINMENUSTATE, null, new VerticalSplitTransition());
		}catch (Exception e) {
			disp("Login Failure: " + e);
			log.warn("Failed to login:",e);
		}
	}
	
	private void back(){
		Init.connection.disconnect();
		GUI.game.enterState(GUI.MAINMENUSTATE);
	}
	
	@Override
    protected void createRootPane() {
        super.createRootPane();
        
        login = new Button("Login");
        login.addCallback( new Runnable() {
			@Override
			public void run() {
				Menu.pick.play();
				connect();
			}
		});
        
        back = new Button("Back");
        back.addCallback( new Runnable() {
			@Override
			public void run() {
				Menu.pick.play();
				back();
			}
		});
        
        username = new EditField();
        password = new EditField();
        password.setPasswordMasking(true);
        
        username.addCallback(new Callback(){
			@Override
			public void callback(int key) {
				if(key == Input.KEY_ENTER){
					Menu.pick.play();
					connect();
				}
			}
        });
        password.addCallback(new Callback(){
			@Override
			public void callback(int key) {
				if(key == Input.KEY_ENTER){
					Menu.pick.play();
					connect();
				}
			}
        });
        
        label = new Label("Login");
        usernameLabel = new Label("Username:");
        passwordLabel = new Label("Password:");
        
        window = new Widget();
        
        window.add(label);
        window.add(usernameLabel);
        window.add(username);
        window.add(passwordLabel);
        window.add(password);
        window.add(login);
        window.add(back);
        
        rootPane.add(window);
    }
	
    @Override
    protected void layoutRootPane() {
    	if(first){
	    	window.setSize(350, 150);
	    	
	    	usernameLabel.adjustSize();
	        passwordLabel.adjustSize();
	        username.adjustSize();
	        username.setSize(200, username.getHeight());
	        password.adjustSize();
	        password.setSize(200, password.getHeight());
	        login.adjustSize();
	        back.adjustSize();
	    	label.adjustSize();
	    	
	        label.setPosition(window.getWidth()/2 - label.getWidth()/2, 10);
	        usernameLabel.setPosition(label.getX() + label.getWidth()/2 - (usernameLabel.getWidth() + username.getWidth())/2 - 5, label.getY() + 30);
	        username.setPosition(label.getX() + label.getWidth()/2 - (usernameLabel.getWidth() + username.getWidth())/2 + usernameLabel.getWidth(), usernameLabel.getY());
	        passwordLabel.setPosition(usernameLabel.getX(), usernameLabel.getY() + username.getHeight() + 15);
	        password.setPosition(username.getX(), passwordLabel.getY());
	        
	        login.setPosition(window.getWidth()/2 - (login.getWidth() + back.getWidth())/2, passwordLabel.getY() + 30);
	        back.setPosition(window.getWidth()/2 - (login.getWidth() + back.getWidth())/2 + back.getWidth(), login.getY());
	        
	        window.setPosition((GUI.container.getWidth() / 2) - window.getWidth()/2, (GUI.container.getHeight() / 2) - window.getHeight()/2 - 40);
    	}
    	first = false;
    }
    
    @Override
    public void reloadRootPane(){
    	first = true;
    	super.reloadRootPane();
    }
    
    public void mouseMoved(int oldx, int oldy, int newx, int newy) {
    	//mouseX = newx;	//TWL consumar dom
    	//mouseY = newy;
	}
    
    @Override
    public void mousePressed(int button, int x, int y) {
    	//System.out.println("Mpress");
	}
    
    @Override
    public void keyPressed(int key, char c) {    	
    	if(System.currentTimeMillis() - 300 > keyDelay){
    		if(key == Input.KEY_UP || key == Input.KEY_LEFT){
				if(password.hasKeyboardFocus()){
					username.requestKeyboardFocus();
				}else if(login.hasKeyboardFocus()){
					password.requestKeyboardFocus();
				}else if(back.hasKeyboardFocus()){
					login.requestKeyboardFocus();
				}else if(username.hasKeyboardFocus()){
					back.requestKeyboardFocus();
				}else{
					username.requestKeyboardFocus();
				}
				keyDelay = System.currentTimeMillis();
				Menu.select.play();
			}else if(key == Input.KEY_DOWN || key == Input.KEY_RIGHT){
				if(username.hasKeyboardFocus()){
					password.requestKeyboardFocus();
				}else if(password.hasKeyboardFocus()){
					login.requestKeyboardFocus();
				}else if(login.hasKeyboardFocus()){
					back.requestKeyboardFocus();
				}else if(back.hasKeyboardFocus()){
					username.requestKeyboardFocus();
				}
				keyDelay = System.currentTimeMillis();
				Menu.select.play();
			}else if(key == Input.KEY_ESCAPE){
				back();
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
	public void initResources(){
    	Menu.select = ResourceManager.getSound("sounds/Megaman X3/SE/SE_1C.SE.wav");
    	Menu.pick = ResourceManager.getSound("sounds/Megaman X3/SE/SE_1D.SE.wav");
		
		background = ResourceManager.getImage("images/Derelict_by_Radojavor.jpg");
	}
    
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		GUI.game = game;
		
		
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		// render the background
		background.draw(0, 0, container.getWidth(), container.getHeight());
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		int selected = 0;
		mouseX = container.getInput().getMouseX();
		mouseY = container.getInput().getMouseY();
		
		if(login.isInside(mouseX, mouseY))selected = 1;
		if(back.isInside(mouseX, mouseY))selected = 2;
		
		if(selected != oldSelected && selected > 0){
			Menu.select.play();
		}
		oldSelected = selected;
	}

}
