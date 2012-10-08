package se.exuvo.mmo.client.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import org.apache.log4j.Logger;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Transform;
import org.newdawn.slick.particles.ParticleSystem;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

import se.exuvo.mmo.client.Init;
import se.exuvo.mmo.client.ResourceManager;
import se.exuvo.mmo.client.world.Collidable;
import se.exuvo.mmo.client.world.Entity;
import se.exuvo.mmo.client.world.Square;
import se.exuvo.mmo.client.world.Unit;
import se.exuvo.mmo.client.world.World;
import se.exuvo.mmo.client.world.Zone;
import se.exuvo.mmo.client.world.abilities.Ability;
import se.exuvo.mmo.client.world.abilities.BlueprintAdd;
import se.exuvo.mmo.client.world.abilities.Move;
import se.exuvo.mmo.client.world.units.buildings.Blueprint;
import se.exuvo.mmo.shared.connection.OrderResponse;
import se.exuvo.mmo.shared.world.NetOrder;
import se.exuvo.mmo.shared.world.Position;
import se.exuvo.mmo.shared.world.NetAbility.validTarget;
import TWLSlick.BasicTWLGameState;

import com.esotericsoftware.kryonet.rmi.TimeoutException;

import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.EditField;
import de.matthiasmann.twl.Label;
import de.matthiasmann.twl.PopupWindow;
import de.matthiasmann.twl.ScrollPane;
import de.matthiasmann.twl.TextArea;
import de.matthiasmann.twl.Widget;
import de.matthiasmann.twl.EditField.Callback;
import de.matthiasmann.twl.textarea.SimpleTextAreaModel;
import de.matthiasmann.twl.textarea.TextAreaModel;
import de.matthiasmann.twl.textarea.TextAreaModel.TextElement;

public class Game extends BasicTWLGameState {
	private static final Logger log = Logger.getLogger(Game.class);
	
    private Input input;
	private int selectionX, selectionY;
	private boolean selecting;
	private int selectResistance = 20;
	private int screenX, screenY;
	private float zoom = 1;
	private static ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
	private ParticleSystem system;

	private Button back;
	
	private List<Long> selected = new ArrayList<Long>();
	private Ability selectedAbility = null;
	public static long mastermind = 0;
	private boolean building = false;
	
	//private ResizableFrame console;
	private Widget chat;
	private Label chatLabel;
	private EditField chatField;
	private List<String> chatFieldOld = new ArrayList<String>();
	private int chatFieldOldPos = -1;
	private Button chatButton;
	private TextArea chatArea;
	private ScrollPane chatScrollPane;
	private SimpleTextAreaModel chatModel;//TODO replace with HTML model to allow text callbacks
	
	private Widget abilitiesWindow;
	private long mouseRightDown = Long.MAX_VALUE;
	
	public static Sound error;
	public static Sound errorBuild;
	public static Sound errorBuild2;
	
	public boolean goToMainMenu = false;
	
	public Game(){
	}

	@Override
	public int getID() {
		return GUI.GAMESTATE;
	}
	
	@Override
	public void enter(GameContainer container, StateBasedGame game) throws SlickException {
		super.enter(container,game);
		log.info("Entered Game state");
		
		GUI.setBackgroundMusic(ResourceManager.getMusic("music/Arvale/Journey of Illusion/cave.xm"));
		GUI.getBackgroundMusic().loop();
		
		selecting = false;
		selected.clear();
		screenX=0;//TODO move to player start location
		screenY=0;
		chatField.setText("");
		chatModel.setText("\n");
		
	}
	
	@Override
	public void initResources(){
		system = new ParticleSystem("org/newdawn/slick/data/particle.tga", 2000);
		error = ResourceManager.getSound("sounds/Starcraft/Misc/BUZZ.WAV");
		errorBuild = ResourceManager.getSound("sounds/Starcraft/Terran/SCV/TSCErr01.wav");
		errorBuild2 = ResourceManager.getSound("sounds/Starcraft/Terran/SCV/TSCErr00.wav");
	}

	@Override
	public void init(final GameContainer container, StateBasedGame game) throws SlickException {
		log.info("Initializing Game state");
		
		input = container.getInput();
		
		Ability.init();
		Unit.init();
	}
	
	@Override
	public void render(GameContainer container, StateBasedGame game, final Graphics g) throws SlickException {
		g.translate(-screenX*zoom, -screenY*zoom);
		g.scale(zoom, zoom);
		
		final Zone z = World.getZone();
		synchronized (z.getLock()) {
			for(int i= Math.max(screenX / Square.size, 0);
				i < Math.min(Math.max(Math.ceil((float)screenX / Square.size) + container.getWidth() / Square.size, 0), z.getSquares().length); i++){
				for(int o= Math.max(screenY / Square.size, 0);
					o < Math.min(Math.max(Math.ceil((float)screenY / Square.size) + container.getHeight() / Square.size, 0), z.getSquares().length); o++){
					if(z.getSquares()[i][o].isPathable()){
						g.setColor(Color.blue);
					}else{
						g.setColor(Color.red);
					}
					g.drawRect(i*Square.size, o*Square.size, Square.size-1, Square.size-1);
				}
			}
			 
			
			for(Entity e : z.getEntities()){
				boolean selectd = selected.contains(e.getId());
				boolean mouseOver = e instanceof Collidable && ((Collidable)e).getCollisionShape().contains(input.getMouseX()+screenX, input.getMouseY()+screenY);
				
				if(selectd && e instanceof Collidable){
					g.setColor(Color.magenta);
					Collidable c = (Collidable) e;
					Shape s = c.getCollisionShape().transform(Transform.createScaleTransform(1.2f, 1.2f));
					float radie = s.getBoundingCircleRadius();
					g.drawOval(c.getCollisionShape().getCenterX() - radie, c.getCollisionShape().getCenterY() - radie, 2*radie, 2*radie);
				}
				
				if(mouseOver){
					Collidable c = (Collidable) e;
					g.setColor(Color.decode("#50FF80"));
					g.drawString(e.getName(), e.getPosition().x - g.getFont().getWidth(e.getName())/2,
							e.getPosition().y -10 - c.getShape().getHeight()/2 + c.getShape().getMinY());
				}else{
					g.setColor(Color.green);
				}
				if(e instanceof Unit){
					Unit u = (Unit) e;
					Shape s = u.getShape();
					if(u.getPosition().x + s.getMaxX() > screenX && u.getPosition().x - s.getMinX() < screenX+container.getScreenWidth()
							&& u.getPosition().y + s.getMaxY() > screenY && u.getPosition().y - s.getMinY() < screenY+container.getScreenHeight()){
						u.draw(g);
					}
					
				}else if(e instanceof Blueprint){
					Blueprint c = (Blueprint) e;
					Shape s = c.getShape();
					if(c.getPosition().x + s.getMaxX() > screenX && c.getPosition().x - s.getMinX() < screenX+container.getScreenWidth()
							&& c.getPosition().y + s.getMaxY() > screenY && c.getPosition().y - s.getMinY() < screenY+container.getScreenHeight()){
						c.draw(g);
					}
				}else if(e instanceof Collidable){
					Collidable c = (Collidable) e;
					Shape s = c.getShape();
					if(c.getPosition().x + s.getMaxX() > screenX && c.getPosition().x - s.getMinX() < screenX+container.getScreenWidth()
							&& c.getPosition().y + s.getMaxY() > screenY && c.getPosition().y - s.getMinY() < screenY+container.getScreenHeight()){
						g.draw(c.getCollisionShape());
					}
				}else{
					if(e.getPosition().x > screenX && e.getPosition().x < screenX+container.getScreenWidth()
							&& e.getPosition().y > screenY && e.getPosition().y < screenY+container.getScreenHeight()){
						g.setColor(Color.white);
						g.drawOval(e.getPosition().x, e.getPosition().y, 10, 10);
						g.drawString(e.getName(), e.getPosition().x - g.getFont().getWidth(e.getName())/2, e.getPosition().y -10);
					}
					
				}
				if(e instanceof Collidable && (mouseOver || selectd)){
					Collidable c = (Collidable) e;
					g.drawRect(e.getPosition().x - 15,
							e.getPosition().y -15 - c.getShape().getHeight()/2 + c.getShape().getMinY(),
							30, 4);
					if(c.getMaxHP() > 0){
						float p = c.getHP()/c.getMaxHP();
						g.setColor(new Color(255*(1-p),255*p,0));
						g.fillRect(e.getPosition().x - 15,
							e.getPosition().y -15 - c.getShape().getHeight()/2 + c.getShape().getMinY(),
							30*p, 4);
					}
				}
			}
			
			if(building){
		    	//TODO draw a build menu and show costs
	    	}
		}
		
		if(selectedAbility != null){
			//TODO change cursor
			if(selectedAbility instanceof Move){
				g.setColor(Color.green);
				int x = input.getMouseX(), y = input.getMouseY();
				g.drawLine(x-10, y, x+10, y);
				g.drawLine(x, y-10, x, y+10);
				//container.setMouseCursor(new Image(0,0), 0, 0);
				
			}else if(selectedAbility instanceof BlueprintAdd){
				g.setColor(Color.blue);
	    		BlueprintAdd b = (BlueprintAdd) selectedAbility;
	    		if(selecting){
	    			for(int x = Square.size/2 + selectionX+screenX - (selectionX+screenX) % Square.size;
	    				input.getMouseX()>selectionX ? x < Square.size + input.getMouseX()+screenX - (input.getMouseX()+screenX) % Square.size
	    					: x > -Square.size/2 + input.getMouseX()+screenX - (input.getMouseX()+screenX) % Square.size
    					; x+=input.getMouseX()>selectionX ? Square.size : -Square.size){
	    				for(int y = Square.size/2 + selectionY+screenY - (selectionY+screenY) % Square.size;
	    					input.getMouseY()>selectionY ? y < Square.size + input.getMouseY()+screenX - (input.getMouseY()+screenX) % Square.size
	    						: y > -Square.size/2 + input.getMouseY()+screenX - (input.getMouseY()+screenX) % Square.size
    						; y+=input.getMouseY()>selectionY ? Square.size : -Square.size){
	    					g.draw(b.getShape().transform(Transform.createTranslateTransform(x, y)));
	    				}
	    			}
	    		}else{
		    		int x = Square.size/2 + input.getMouseX()+screenX - (input.getMouseX()+screenX) % Square.size;
		    		int y = Square.size/2 + input.getMouseY()+screenY - (input.getMouseY()+screenY) % Square.size;
		    		g.draw(b.getShape().transform(Transform.createTranslateTransform(x, y)));
	    		}
	    		
	    	}else{
				//container.setDefaultMouseCursor();
			}
		}else{
			if(selecting){
				g.setColor(Color.white);
				g.drawRect(selectionX+screenX, selectionY+screenY, input.getMouseX()-selectionX, input.getMouseY()-selectionY);
			}
		}
		system.render();
	}

	@Override
	public void update(GameContainer container, final StateBasedGame game, int delta) throws SlickException {
		float d = delta/1000.0f;
		if(goToMainMenu){
			game.enterState(GUI.MAINMENUSTATE);
			goToMainMenu = false;
		}else{
			if(!Init.connection.isConnected() && !rootPane.hasOpenPopups()){
				new Thread(){
					public void run(){
						PopupWindow p = ((BasicTWLGameState)GUI.game.getCurrentState()).disp("Server disconnected, you were probably kicked.");
						while(p.isOpen()){
							Thread.yield();
						}
						goToMainMenu = true;
					}
				}.start();
			}
			if(System.currentTimeMillis() - mouseRightDown > 400){//Abilites menu
				mouseRightDown = Long.MAX_VALUE;
				abilitiesWindow.removeAllChildren();
				
				//TODO show abilities menu for the selected units. if none show the build menu/ mastermind
	    		List<Ability> abi = new ArrayList<Ability>();
	    		Zone z = World.getZone();
	    		synchronized (z.getLock()) {
	    			if(selected.size() == 0){
	    				for(Entity e : z.getEntities()){
							if(e.getOwner() == Init.connection.getPlayerId() && e instanceof Collidable &&
									((Collidable) e).getCollisionShape().contains(input.getMouseX()+screenX, input.getMouseY()+screenY)){
								abi.addAll(e.getAbilities());
								selected.add(e.getId());
								break;
							}
		    			}
	    				if(abi.size() == 0){
	    					abi.addAll(z.getEntity(mastermind).getAbilities());
	    				}
	    			}
		    		for(long id : selected){
		    			Entity e = z.getEntity(id);
		    			if(e == null) continue;
		    			for(Ability b : e.getAbilities()){
		    				if(!abi.contains(b)) abi.add(b);
		    			}
		    		}
	    		}
	    		int x = 5;
	    		for(final Ability a : abi){
	    			final Button b = new Button(a.getName());
	    			b.setTooltipContent(a.getDescription());
	    			b.addCallback(new Runnable() {
						@Override
						public void run() {
							if(a.getTargets().contains(validTarget.self) && !a.getTargets().contains(validTarget.entity) && !a.getTargets().contains(validTarget.point)){
								useAbility(a);
								b.giveupKeyboardFocus();
							}else{
								selectedAbility = a;
							}
						}
					});
	    			b.setPosition(x, 10);
	    			abilitiesWindow.add(b);
	    			b.adjustSize();
	    			x += b.getWidth() - 5;
	    		}
	    		abilitiesWindow.adjustSize();
	    		abilitiesWindow.setPosition(input.getMouseX(), input.getMouseY() - abilitiesWindow.getHeight());
				abilitiesWindow.setVisible(true);
			}
			
			if(!chatField.hasKeyboardFocus()){
				float s = 140*d;
		    	if(input.isKeyDown(Input.KEY_UP)){
		    		screenY-=s;
		    	}else if(input.isKeyDown(Input.KEY_DOWN)){
		    		screenY+=s;
		    	}
		    	if(input.isKeyDown(Input.KEY_LEFT)){
		    		screenX-=s;
		    	}else if(input.isKeyDown(Input.KEY_RIGHT)){
		    		screenX+=s;
		    	}
			}
		
			World.getZone().run();
			system.update(delta);
		}
	}
	
	public void chatAppend(String s){
		synchronized (chatModel) {
			TextAreaModel.TextElement t = (TextElement)chatModel.iterator().next();
			chatModel.setText(s + "\n" + t.getText());
		}
	}
	
	@Override
    protected void createRootPane() {
        super.createRootPane();
        
        back = new Button("Back");
        back.addCallback( new Runnable(){
			@Override
			public void run() {
				Menu.pick.play();
				Init.connection.disconnect();
				GUI.game.enterState(GUI.MAINMENUSTATE, new FadeOutTransition(), new FadeInTransition());
			}
		});
        
        abilitiesWindow = new Widget();
        abilitiesWindow.setVisible(false);
        
        	chat = new Widget();
        	chat.setSize(350, 200);
        	chat.setPosition(0, GUI.container.getHeight() - chat.getHeight());
			
			chatLabel = new Label("Chat");
			chat.add(chatLabel);
			
			chatArea = new TextArea();
			chatModel = new SimpleTextAreaModel();
			chatArea.setModel(chatModel);
			//chatArea.setSize(100, 100);
			
			chatScrollPane = new ScrollPane(chatArea);
			chatScrollPane.setFixed(ScrollPane.Fixed.HORIZONTAL);
			chat.add(chatScrollPane);
			
			chatField = new EditField();
			chatField.setForwardUnhandledKeysToCallback(true);
			//TODO chatField.setAutoCompletion(dataSource); with server command list
			chatField.addCallback(new Callback() {
				@Override
				public void callback(int key) {
					if(key == Input.KEY_ENTER){
						String text = chatField.getText();
						if(text.startsWith("/")){
							text = text.substring(1);
						}else{
							text = "chat " + text;
						}
						try{
							String s = Init.connection.getServer().issueCommand(text);
						
							chatAppend(s);
							log.trace("Issued command \"" + chatField.getText() + "\", response:\"" + s + "\"");
							
							chatFieldOldPos = -1;
							chatFieldOld.add(0, chatField.getText());
							chatField.setText("");
							if(chatFieldOld.size() > 20){
								chatFieldOld.subList(19, chatFieldOld.size()).clear();
							}
						}catch(TimeoutException t){
							return;
						}
					}else if(key == Input.KEY_UP){
						if(chatFieldOldPos < chatFieldOld.size()-1){
							chatField.setText(chatFieldOld.get(++chatFieldOldPos));
						}
					}else if(key == Input.KEY_DOWN){
						if(chatFieldOldPos > 0){
							chatField.setText(chatFieldOld.get(--chatFieldOldPos));
						}else{
							chatFieldOldPos = -1;
							chatField.setText("");
						}
					}
				}
			});
			chat.add(chatField);
			
			chatButton =  new Button();
			chatButton.setText("x");
			chatButton.addCallback(new Runnable() {
				@Override
				public void run() {
					chat.setVisible(false);
				}
			});
			chatButton.setSize(20, 20);
			chat.add(chatButton);
        
			rootPane.add(chat);
        rootPane.add(back);
        rootPane.add(abilitiesWindow);
    }
	
    @Override
    protected void layoutRootPane(){
    	back.adjustSize();
    	back.setPosition(GUI.container.getWidth() - back.getWidth(), 0);
    	
    	chatField.adjustSize();
    	chatField.setSize(chat.getWidth() - 20, chatField.getHeight());
    	chatLabel.adjustSize();
    	chatButton.adjustSize();
    	
//    	chatArea.setPosition(chat.getX()+10, chat.getY()+10);
//    	chatArea.setSize(chat.getWidth()-20, chat.getHeight() - chatField.getHeight() - 3);
    	chatScrollPane.setPosition(chat.getX()+10, chat.getY()+10);
    	chatScrollPane.setSize(chat.getWidth()-20, chat.getHeight() - chatField.getHeight() - 1);
    	
    	chatField.setPosition(chat.getX() + chat.getWidth()/2 - chatField.getWidth()/2, chat.getY() + chat.getHeight() - chatField.getHeight() - 4);
    	chatLabel.setPosition(chat.getX() + chat.getWidth()/2 - chatLabel.getWidth()/2, chat.getY());
    	chatButton.setPosition(chat.getX() + chat.getWidth() - chatButton.getWidth(), chat.getY());
    }
    
    public void mouseMoved(int oldX, int oldY, int newX, int newY) {
	}
    
    @Override
    public void mouseDragged(int oldX, int oldY, int newX, int newY) {
    	if(input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON) && !selecting && rootPane.getWidgetAt(oldX, oldY) == rootPane){
    		if(Math.abs(selectionX - newX) > selectResistance || Math.abs(selectionY - newY) > selectResistance){
    			selecting = true;
    			log.trace("begin dragging");
    		}
    	}
    }
    
    @Override
    public void mouseClicked(int button, int x, int y, int clickCount) {
    	if(button == Input.MOUSE_LEFT_BUTTON && clickCount == 1){
	    	Zone z = World.getZone();
			synchronized (z.getLock()) {
				for(Entity e : z.getEntities()){
					if(e instanceof Collidable){
						if(((Collidable)e).getCollisionShape().contains(x+screenX, y+screenY)){
							if(!input.isKeyDown(Input.KEY_LSHIFT)){
								selected.clear();
							}
							if(selected.contains(e.getId())){
								selected.remove(e.getId());
							}else{
								selected.add(e.getId());
							}
							
							break;
						}
					}
				}
			}
    	}
	}
    
    @Override
    public void mousePressed(int button, int x, int y) {
    	if(button == Input.MOUSE_LEFT_BUTTON){
    		selectionX = x;
    		selectionY = y;
    	}else if(button == Input.MOUSE_RIGHT_BUTTON){
    		mouseRightDown = System.currentTimeMillis();
    	}
	}
    
    @Override
    public void mouseReleased(int button, final int x, final int y) {
    	if((button == Input.MOUSE_RIGHT_BUTTON)
    			&& !(rootPane.getWidgetAt(x, y) == abilitiesWindow || abilitiesWindow.getChildIndex(rootPane.getWidgetAt(x, y)) != -1)){
    		mouseRightDown = Long.MAX_VALUE;
    		abilitiesWindow.setVisible(false);
    		selectedAbility = null;
    	}
    	
    	if(button == Input.MOUSE_LEFT_BUTTON && selecting && selectedAbility == null){
    		log.trace("dragged from " + selectionX + ":" + selectionY + " to " + x + ":" + y);
        	
        	Zone z = World.getZone();
    		synchronized (z.getLock()) {
    			if(!input.isKeyDown(Input.KEY_LSHIFT)){
    				selected.clear();
    			}
    			
    			float x1 = (selectionX < x) ? selectionX : x;
				float y1 = (selectionY < y) ? selectionY : y;
				float x2 = (selectionX < x) ? x : selectionX;
				float y2 = (selectionY < y) ? y : selectionY;
				
				x1 += screenX;
				x2 += screenX;
				y1 += screenY;
				y2 += screenY;
				
    			for(Entity e : z.getEntities()){
    				Position p = e.getPosition();
    				
					if((x1 < p.x && p.x < x2) && (y1 < p.y && p.y < y2)){
						selected.add(e.getId());
					}
    			}
    		}
    		selecting = false;
    	}else if(selectedAbility != null && button == Input.MOUSE_LEFT_BUTTON && selecting && !selectedAbility.getTargets().contains(validTarget.point)){
    		log.debug("Unable Issuing area orders");
    		
    		if(!selectedAbility.getTargets().contains(validTarget.point)){
				log.debug("Order failed: Can not target points");
				chatAppend("Order failed: Can not target points");
			}
    		selecting = false;
    	}else if(selectedAbility != null && button == Input.MOUSE_LEFT_BUTTON && selecting){
    		log.debug("Issuing area orders");
    		selecting = false;
    		
	    	Zone z = World.getZone();
			synchronized (z.getLock()){
				for(Entity c : z.getEntities()){
					if((selected.size() == 0 || selected.contains(c.getId()) && c.getOwner() == Init.connection.getPlayerId())){
						if(selected.size() == 0) c = z.getEntity(mastermind);
							
						Ability a = selectedAbility;
						if(c.getAbility(a.getId()) == null) continue;
						
						for(int tx = Square.size/2 + selectionX+screenX - (selectionX+screenX) % Square.size;
							input.getMouseX()>selectionX ? tx < Square.size + input.getMouseX()+screenX - (input.getMouseX()+screenX) % Square.size
								: tx > -Square.size/2 + input.getMouseX()+screenX - (input.getMouseX()+screenX) % Square.size
							; tx+=input.getMouseX()>selectionX ? Square.size : -Square.size){
							for(int ty = Square.size/2 + selectionY+screenY - (selectionY+screenY) % Square.size;
								input.getMouseY()>selectionY ? ty < Square.size + input.getMouseY()+screenX - (input.getMouseY()+screenX) % Square.size
									: ty > -Square.size/2 + input.getMouseY()+screenX - (input.getMouseY()+screenX) % Square.size
								; ty+=input.getMouseY()>selectionY ? Square.size : -Square.size){
								issueOrder(a, c, new Position(tx, ty), true);
							}
						}
						if(selected.size() == 0)break;
					}
				}
			}
    	}else if((button == Input.MOUSE_RIGHT_BUTTON && !(rootPane.getWidgetAt(x, y) == abilitiesWindow || abilitiesWindow.getChildIndex(rootPane.getWidgetAt(x, y)) != -1)) 
    			|| (selectedAbility != null && button == Input.MOUSE_LEFT_BUTTON)){
    		if(button == Input.MOUSE_RIGHT_BUTTON && selectedAbility != null){
    			abilitiesWindow.setVisible(false);
        		selectedAbility = null;
        		return;
    		}
    		
    		log.debug("Issuing orders");
	    	Zone z = World.getZone();
	    	final boolean queue = input.isKeyDown(Input.KEY_LSHIFT);
			synchronized (z.getLock()){
				for(Entity c : z.getEntities()){
					if((selected.contains(c.getId()) && c.getOwner() == Init.connection.getPlayerId()) || selected.size() == 0){
						if(selected.size() == 0) c = z.getEntity(mastermind);
						
						Entity t = null;
						if(selectedAbility != null && selectedAbility.getTargets().contains(validTarget.entity) || selectedAbility == null){
							for(Entity eee : z.getEntities()){
								if(eee instanceof Collidable && ((Collidable) eee).getCollisionShape().contains(x+screenX, y+screenY)){
									t = eee;
									break;
								}
			    			}
							if(t == null && selectedAbility != null && selectedAbility.getTargets().contains(validTarget.entity) && !selectedAbility.getTargets().contains(validTarget.point)){
								log.debug("Order failed: Can only target entities and no entities are under the mouse");
								chatAppend("Order failed: Can only target entities and no entities are under the mouse");
								continue;
							}
						}else if(selectedAbility != null && selectedAbility.getTargets().contains(validTarget.self) && c.getAbility(selectedAbility.getId()) != null){
							t = c;
						}
						
						Ability a = null;
						int tx = x+screenX, ty = y+screenY;
						if(selectedAbility == null){
							if(t != null){
								if(t instanceof Blueprint){
									c = t;
									Blueprint b = (Blueprint) t;
									if(b.isPaused()){
										a = c.getAbility("BlueprintUnpause");
									}else{
										a = c.getAbility("BlueprintPause");
									}
								}
							}else if(selected.size() != 0){
								if(c.getAbilities(Move.class).size() > 0){
									a = c.getAbilities(Move.class).get(0);
									
								/*}else if(e.getAbilities(Attack.class).size() > 0 && e.isEnemy(eeee)){
									a = e.getAbilities(Attack.class).get(0).getNet();
								*/
								}
							}
							if(a == null){
								continue;
							}
						}else{
							if(selected.size() == 0 && t != null && t.getOwner() == Init.connection.getPlayerId() && c.getAbility(selectedAbility.getId()) == null) c = t;
							if(c.getAbility(selectedAbility.getId()) == null) continue;
							a = selectedAbility;
							if(selectedAbility instanceof BlueprintAdd){
					    		tx = Square.size/2 + x+screenX - (x+screenX) % Square.size;
					    		ty = Square.size/2 + y+screenY - (y+screenY) % Square.size;
							}
						}
						
						if(t != null && a.getTargets().contains(validTarget.self) && !a.getTargets().contains(validTarget.entity)){
							t = c;
						}
						
						if(t != null){
							issueOrder(a, c, t, queue);
						}else{
							issueOrder(a, c, new Position(tx, ty), queue);
						}
						
						if(selected.size() == 0)break;
					}
				}
			}
    	}
    	if(selectedAbility != null && (button == Input.MOUSE_LEFT_BUTTON && !input.isKeyDown(Input.KEY_LSHIFT))){
			abilitiesWindow.setVisible(false);
    		selectedAbility = null;
		}
    	if(abilitiesWindow.isVisible() && selectedAbility == null && !(rootPane.getWidgetAt(x, y) == abilitiesWindow || abilitiesWindow.getChildIndex(rootPane.getWidgetAt(x, y)) != -1)){
			abilitiesWindow.setVisible(false);
		}
	}
    
    public void issueOrder(final Ability ability, final Entity caster, final Entity target){
    	issueOrder(ability, caster, target, false);
    }
    
    public void issueOrder(final Ability ability, final Entity caster, final Entity target, final boolean queue){
    	executor.submit(new Runnable(){
			public void run() {
				log.debug("Issuing \"" + ability.getName() + "\" order to " + caster + " targeting " + target);
				try {
					OrderResponse response = Init.connection.getServer().issueOrder(new NetOrder(caster.getId(), ability.getNet(), target.getId()), queue);
					if(response.error != null){
						log.info("Order failed: " + response.error);
						chatAppend("Order failed: " + response.error);
						if(ability instanceof BlueprintAdd){
							if(!errorBuild.playing()){
								errorBuild.play();
							}
						}else{
							if(!error.playing()){
								error.play();
							}
						}
					}
				} catch (Throwable ex) {
					log.warn("",ex);
				}
			};
		});
    }
    
    public void issueOrder(final Ability ability, final Entity caster, final Position target){
    	issueOrder(ability, caster, target, false);
    }
    
    public void issueOrder(final Ability ability, final Entity caster, final Position target, final boolean queue){
    	executor.submit(new Runnable(){
			public void run() {
				log.debug("Issuing \"" + ability.getName() + "\" order to " + caster + " targeting " + target);
				try {
					OrderResponse response = Init.connection.getServer().issueOrder(new NetOrder(caster.getId(), ability.getNet(), target), queue);
					if(response.error != null){
						log.info("Order failed: " + response.error);
						chatAppend("Order failed: " + response.error);
						if(ability instanceof BlueprintAdd){
							if(!errorBuild.playing()){
								errorBuild.play();
							}
						}else{
							if(!error.playing()){
								error.play();
							}
						}
					}
				} catch (Throwable ex) {
					log.warn("",ex);
				}
			};
		});
    }
    
    public void useAbility(Ability a){
    	if(!a.getTargets().contains(validTarget.self)){
    		return;
    	}
    	Zone z = World.getZone();
    	synchronized (z.getLock()){
			for(Entity c : z.getEntities()){
				if((selected.contains(c.getId()) && c.getOwner() == Init.connection.getPlayerId()) || selected.size() == 0){
					if(selected.size() == 0)
						c = z.getEntity(mastermind);
					
					if(c.getAbility(a.getId()) == null) continue;
					issueOrder(a, c, c);
					
					if(selected.size() == 0)break;
				}
			}
		}
    }
    
    @Override
    public void keyPressed(int key, char c) {    	
    	if(key == Input.KEY_ESCAPE){
    		//TODO go to main menu with Connect replaced with Disconnect and added Resume
    		Init.connection.disconnect();
			GUI.game.enterState(GUI.MAINMENUSTATE);
    	}else if(key == Input.KEY_C){
    		if(chat.isVisible()){
    			chat.setVisible(false);
    		}else{
    			chat.setVisible(true);
        		chat.requestKeyboardFocus();
    		}
    	}else if(key == Input.KEY_Z){
    		if(zoom == 1){
    			zoom = 10;
    		}else{
    			zoom = 1;
    		}
    	}
	}
	
}
