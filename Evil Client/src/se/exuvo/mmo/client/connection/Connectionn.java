package se.exuvo.mmo.client.connection;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;
import org.jasypt.util.password.ConfigurablePasswordEncryptor;
import org.jasypt.util.text.BasicTextEncryptor;

import se.exuvo.mmo.client.Settings;
import se.exuvo.mmo.client.gui.GUI;
import se.exuvo.mmo.client.gui.Game;
import se.exuvo.mmo.client.world.Entity;
import se.exuvo.mmo.client.world.World;
import se.exuvo.mmo.client.world.units.Genius;
import se.exuvo.mmo.shared.connection.ClientCommands;
import se.exuvo.mmo.shared.connection.Connect;
import se.exuvo.mmo.shared.connection.Login;
import se.exuvo.mmo.shared.connection.LoginResponse;
import se.exuvo.mmo.shared.connection.Message;
import se.exuvo.mmo.shared.connection.Network;
import se.exuvo.mmo.shared.connection.ServerCommands;
import se.exuvo.mmo.shared.connection.LoginResponse.Cause;
import se.exuvo.mmo.shared.world.NetZone;
import se.exuvo.mmo.shared.world.NetZonePart;
import se.exuvo.mmo.shared.world.Snapshot;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.rmi.ObjectSpace;
import com.esotericsoftware.kryonet.rmi.TimeoutException;


public class Connectionn extends com.esotericsoftware.kryonet.Listener implements ClientCommands{
	private static final Logger log = Logger.getLogger(Connectionn.class);
	private ServerCommands server;
	private Connect loginInterface;
	private boolean connected = false;
	private boolean authenticated = false;
	private long playerId;
	private static Client connection = new Client(8*1024*1024, 1*1024*1024);
	private static ObjectSpace objectSpace;
	private AtomicInteger counterRMI = new AtomicInteger(1);
	private static ExecutorService executor = Executors.newSingleThreadExecutor();

    public Connectionn() {
    	Network.register(connection);
    }
    
    public void disconnect(){
    	connection.stop();
		objectSpace.close();
		setAuthenticated(false);
		setConnected(false);
		setServer(null);
		loginInterface = null;
		log.info("Disconnected");
    }
    
    public boolean connect() throws IOException, Exception{
    	if(!isConnected()){
    		World.setWorld(null);
    		String server = Settings.getStr("server");
    		int port = 60050;
    		if(server.contains(":")){
    			port = Integer.parseInt(server.substring(server.indexOf(":")+1));
    			server = server.substring(0, server.indexOf(":"));
    		}
    		
    		try {
    			connection.start();
    			connection.connect(Settings.getInt("timeout"), server, port, port);
    			connection.addListener(this);
    			
    			Connect connect = ObjectSpace.getRemoteObject(connection, 0, Connect.class);

    			long version = connect.getVersion();
    			if(version != getVersion()){
    				throw new Exception("Version mismatch: client " + getVersion() + " != server " + version);
    			}
	            log.info("Connected to \"" + Settings.getStr("server") +":"+ Settings.getInt("port") + "\" with version " + version);
	            
	            objectSpace = new ObjectSpace();
    			objectSpace.register(0, this);
    			objectSpace.addConnection(connection);
	            
	            loginInterface = connect;
	            setConnected(true);
			} catch (IOException e) {
				log.warn("Failed to connect to server.", e);
				throw e;
			}
    	}
		return isConnected();
    }
    
    public boolean login(String username, String password) throws Exception, TimeoutException{
    	if(isConnected() && !isAuthenticated()){
    		ConfigurablePasswordEncryptor passwordEncryptor = new ConfigurablePasswordEncryptor();
			passwordEncryptor.setAlgorithm("SHA-1");
			passwordEncryptor.setPlainDigest(true);
			
    		BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
    		textEncryptor.setPassword("<3");
    		
    		Login l = new Login();
    		l.clientRMIObjectSpaceID = 0;
    		l.username = username;
    		l.password = textEncryptor.encrypt(passwordEncryptor.encryptPassword(password));
    		
    		class i extends Listener {
				public LoginResponse response = null;
					    			
    			@Override
    			public void received(Connection connection, Object object) {
    				super.received(connection, object);
    				if(object instanceof LoginResponse){
    	    			response = (LoginResponse) object;
    	    			connection.removeListener(this);
    	    		}
    			}
    		}
    		
    		i ii = new i();
    		connection.addListener(ii);
    		connection.sendTCP(l);
    		
    		long start = System.currentTimeMillis();
    		while(ii.response == null){
    			try{
    				Thread.sleep(10);
    			}catch(InterruptedException e){}
    			if(System.currentTimeMillis() - start > Settings.getInt("timeout")){
    				//Timeout
    				connection.removeListener(ii);
    				throw new TimeoutException("Timeout while waiting for response from server.");
    			}
    		}
    		
    		if(ii.response.error != null){
    			if(ii.response.error == Cause.Version){
    				throw new Exception(ii.response.text);
    			}else if(ii.response.error == Cause.Credentials){
    				throw new Exception(ii.response.text);
    			}else{
    				log.error("Unknown exception while logging in:", new Exception(ii.response.text));
    				throw new RuntimeException(ii.response.text);
    			}
    		}
    		ServerCommands temp = ObjectSpace.getRemoteObject(connection, ii.response.serverRMIObjectSpaceID, ServerCommands.class);
    		
    		log.info("Logged in as \"" + username + "\"");
			setServer(temp);
			playerId = getServer().getId();
			setAuthenticated(true);
			
			start = System.currentTimeMillis();
    		while(World.getZone() == null){
    			try{
    				Thread.sleep(10);
    			}catch(InterruptedException e){}
    			if(System.currentTimeMillis() - start > Settings.getInt("timeout")){
    				throw new TimeoutException("Timeout while waiting for initial world from server.");
    			}
    		}
    	}
		return isAuthenticated();
    }
    
    public int getNextRMIId() {
		return counterRMI.getAndIncrement();
	}

	@Override
	public long getVersion(){
		return Network.serialVersionUID;
	}

	private void setServer(ServerCommands server) {
		this.server = server;
	}

	public ServerCommands getServer() {
		return server;
	}
	
	public void setConnected(boolean connected) {
		this.connected = connected;
	}

	public boolean isConnected() {
		return connected;
	}

	public void setAuthenticated(boolean authenticated) {
		this.authenticated = authenticated;
	}

	public boolean isAuthenticated() {
		return authenticated;
	}
	
	public long getPlayerId(){
		return playerId;
	}

	@Override
	public void disconnected(Connection connection) {
		super.disconnected(connection);
		log.info("Connection lost to server");
		disconnect();
	}

	private ByteArrayOutputStream zoneBuffer = new ByteArrayOutputStream();
	@Override
	public void received(final Connection connection,final Object object) {
		super.received(connection, object);

		executor.submit(new Runnable() {
			@Override
			public void run() {
				try{
					if(object instanceof Snapshot){
						Snapshot s = (Snapshot)object;
						World.changeWorld(s);
					}else if(object instanceof NetZonePart){
						NetZonePart p = (NetZonePart)object;
						if(p.bytes != null){
							zoneBuffer.write(p.bytes);
						}else{
							Kryo k = connection.getEndPoint().getKryo();
							NetZone z = k.readObject(ByteBuffer.wrap(zoneBuffer.toByteArray()), NetZone.class);
							World.setWorld(z);
							zoneBuffer.reset();
							
							synchronized (World.getZone().getLock()){
								for(Entity e : World.getZone().getEntities()){
									if(Genius.class.isAssignableFrom(e.getClass())){
										Game.mastermind = e.getId();
										break;
									}
								}
							}
						}
					}else if(object instanceof Message){
						Message m = (Message)object;
						((Game)GUI.game.getState(GUI.GAMESTATE)).chatAppend(m.source + ": " + m.message);
					}
				}catch(Throwable t){
					log.error("", t);
				}
		}});
	}
	
	

}

