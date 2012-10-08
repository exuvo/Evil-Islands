package se.exuvo.evil.server.clients;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.jasypt.util.password.ConfigurablePasswordEncryptor;
import org.jasypt.util.text.BasicTextEncryptor;

import se.exuvo.evil.server.Settings;
import se.exuvo.evil.server.clients.Client.Access;
import se.exuvo.evil.server.world.Planet;
import se.exuvo.evil.server.world.ThreadHandler;
import se.exuvo.evil.server.world.events.ConnectEvent;
import se.exuvo.evil.server.world.o.units.Genius;
import se.exuvo.evil.server.world.o.units.minions.Worker;
import se.exuvo.evil.shared.connection.ClientCommands;
import se.exuvo.evil.shared.connection.Connect;
import se.exuvo.evil.shared.connection.Login;
import se.exuvo.evil.shared.connection.LoginResponse;
import se.exuvo.evil.shared.connection.Network;
import se.exuvo.evil.shared.world.NetZonePart;
import se.exuvo.evil.shared.world.Position;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.kryonet.rmi.ObjectSpace;

public class Listener extends com.esotericsoftware.kryonet.Listener implements Connect{
	private boolean listening = false;
	private static final Logger log = Logger.getLogger(Listener.class);
	private static Server server = new Server(8*1024*1024, 1*1024*1024);
	private static ObjectSpace loginObjectSpace;
	
	public Listener(){
		Network.register(server);
	}
	
	public boolean start(){
		int port = Settings.getInt("port");
		try {
			server.start();
			server.bind(port, port);
			server.addListener(this);
			
			loginObjectSpace = new ObjectSpace();
			loginObjectSpace.register(0, this);
			
			setListening(true);
			log.info("Listening for connections on port: " + port);
			return true;
		} catch (IOException e) {
			log.error("Failed to create connection listener on port " + port, e);
		}
		return false;
	}
	
	public void stop(){
		server.stop();
		loginObjectSpace.close();
		setListening(false);
	}
	
	public void setListening(boolean listening) {
		this.listening = listening;
	}

	public boolean isListening() {
		return listening;
	}
	
	public long getVersion(){
		return Network.serialVersionUID;
	}

	/**
	 * Register player
	 */
	@Override
	public boolean register(String username, String password){
		// TODO check if username exists, create new user
		return false;
	}
	
	@Override
	public void connected(Connection connection) {
		super.connected(connection);
		loginObjectSpace.addConnection(connection);
		
		log.info("Client " + connection.getID() + " from \"" + connection.getRemoteAddressTCP() + "\" has connected.");
	}

	@Override
	public void disconnected(Connection connection) {
		super.disconnected(connection);
		//objectSpace.removeConnection(connection); does by itself
	}

	@Override
	public void received(final Connection connection, final Object object) {
		super.received(connection, object);
		
		ThreadHandler.add(new Runnable() {
			@Override
			public void run() {
				try{
					if(object instanceof Login){
						Login l = (Login)object;
						ClientCommands client = ObjectSpace.getRemoteObject(connection, l.clientRMIObjectSpaceID, ClientCommands.class);
						
						long version = client.getVersion();
		    			if(version != getVersion()){ 
		    				connection.sendTCP(new LoginResponse("Version mismatch: server " +
		    						getVersion() + " != client " + version, LoginResponse.Cause.Version));
		    				log.info("Client " + connection.getID() + " \"" + l.username + "\" failed to login: Version mismatch: server " +
		    						getVersion() + " != client " + version);
		    				return;
		    			}
						
						//TODO authenticate user with database, load saved stuff
						ConfigurablePasswordEncryptor passwordEncryptor = new ConfigurablePasswordEncryptor();
						passwordEncryptor.setAlgorithm("SHA-1");
						passwordEncryptor.setPlainDigest(true);
						
						BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
						textEncryptor.setPassword("<3");
						l.password = textEncryptor.decrypt(l.password);
						
						Player p = Players.getPlayer(l.username);
						if(p == null){
							p = Players.newPlayer(l.username, l.password);
							//TODO create new island
							p.setIsland(Planet.getHub());
							Genius g = new Genius();
							Worker w = new Worker();Worker w2 = new Worker();Worker w3 = new Worker();
								g.setOwner(p.getID());
								w.setOwner(p.getID());w2.setOwner(p.getID());w3.setOwner(p.getID());
								g.setPosition(new Position(150, 200));
								w.setPosition(new Position(160, 210));w2.setPosition(new Position(160, 210));w3.setPosition(new Position(160, 210));
								g.setName("Genius " + p.getUsername());
								synchronized (p.getIsland().getLock()) {
									p.getIsland().addEntity(g);
									p.getIsland().addEntity(w);p.getIsland().addEntity(w2);p.getIsland().addEntity(w3);
								}
							p.setGenius(g);
						}
						
						if (l.password.equals(p.getPassword())){
						//if (passwordEncryptor.checkPassword(textEncryptor.decrypt(l.password), hash)){
							log.info("Player " + connection.getID() + " \"" + l.username + "\" logged in with version " + client.getVersion());
							
							Client c = new Client(client, p, connection);
							
							if(c.hasAccess(Access.banned)){
								connection.sendTCP(new LoginResponse("You be banned! Be gone!", LoginResponse.Cause.Banned));
								log.info("Player " + connection.getID() + " \"" + l.username + "\" failed to login: banned");
								return;
							}
							int id = c.getNextRMIId();
							c.getObjectSpace().register(id, c);
							Planet.fireEvent(new ConnectEvent(this, c));
							
							connection.sendTCP(new LoginResponse(id));
							loginObjectSpace.removeConnection(connection);//Prevent dual login from same client
							
							for(NetZonePart z :c.getNetZone()){
								connection.sendTCP(z);
							}
							log.debug("Sent world to Client " + connection.getID() + " \"" + l.username + "\"");
							
							synchronized (Client.getClients()){
								Client.getClients().add(c);
							}
						} else {
							// bad login!
							connection.sendTCP(new LoginResponse("You fail with password",LoginResponse.Cause.Credentials));
							log.info("Client " + connection.getID() + " \"" + l.username + "\" failed to login: InvalidCredentials");
						}
					}
				}catch(Throwable t){
					log.warn("", t);
				}
			}	
		});
	}

}

