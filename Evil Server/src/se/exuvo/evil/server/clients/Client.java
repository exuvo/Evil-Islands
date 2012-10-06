package se.exuvo.evil.server.clients;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;

import se.exuvo.evil.server.Settings;
import se.exuvo.evil.server.commands.Parser;
import se.exuvo.evil.server.world.Island;
import se.exuvo.evil.server.world.Planet;
import se.exuvo.evil.server.world.Unit;
import se.exuvo.evil.server.world.o.Order;
import se.exuvo.evil.shared.connection.ClientCommands;
import se.exuvo.evil.shared.connection.InvalidOrderException;
import se.exuvo.evil.shared.connection.OrderResponse;
import se.exuvo.evil.shared.connection.ServerCommands;
import se.exuvo.evil.shared.world.NetOrder;
import se.exuvo.evil.shared.world.NetZone;
import se.exuvo.evil.shared.world.NetZonePart;
import se.exuvo.evil.shared.world.Snapshot;

import com.esotericsoftware.kryo.ObjectBuffer;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.rmi.ObjectSpace;

public class Client extends com.esotericsoftware.kryonet.Listener implements ServerCommands{
	private static List<Client> clients = new ArrayList<Client>();
	private static final Logger log = Logger.getLogger(Client.class);
	private AtomicInteger counterRMI = new AtomicInteger(1);
	private ClientCommands client;
	private Connection connection;
	private Island zone = Planet.getHub();
	private Player player;
	private Object lock = new Object();
	private static ScheduledExecutorService executor = Executors.newScheduledThreadPool(Settings.getInt("updateThreads"));
	public static enum Access {
		kami_sama, admin, moderator, noob, banned;
	};
	private List<Access> access = new ArrayList<Access>();
	private ObjectSpace objectSpace = new ObjectSpace();
	
	
	public Client(ClientCommands client, Player p, Connection c){
		setClient(client);
		player = p;
		setConnection(c);
		getObjectSpace().addConnection(c);
		c.addListener(this);
		addAccess(Access.kami_sama);
		addAccess(Access.moderator);
	}

	public static List<Client> getClients() {
		return clients;
	}
	
	private void setClient(ClientCommands client) {
		this.client = client;
	}

	public ClientCommands getClient() {
		return client;
	}

	public List<NetZonePart> getNetZone(){
		NetZone z;
		synchronized (zone.getLock()) {
			z = zone.getNet();
		}
		
		ObjectBuffer buffer = new ObjectBuffer(connection.getEndPoint().getKryo(), 1*1024*1024);//TODO fails here if buffer is too small
		byte[] bytes = buffer.writeObject(z);
		ByteArrayInputStream b = new ByteArrayInputStream(bytes);
		
		List<NetZonePart> l = new ArrayList<NetZonePart>();
		while(b.available()>0){
			byte [] a = new byte[b.available()>=1024 ? 1024:b.available()];
			try {
				b.read(a);
				l.add(new NetZonePart(a));
			} catch (IOException e) {
				log.warn("Failed to break NetZone into parts", e);
			}
		}
		
		l.add(new NetZonePart());
		return l;
	}
	
	public OrderResponse issueOrder(NetOrder order, boolean queue){
		log.debug("Order \"" + order.getAbility().getId() + "\" issued by Client " + getId() + " \"" + getUsername() + "\"");
		try{
			Order o = new Order(order);
			o.setIssuer(this);
			synchronized(o.getCaster().getZone().getLock()){
				if(o.getCaster() instanceof Unit){
					o.isValid();
					Unit u = (Unit) o.getCaster();
					if(!queue){
						u.getQueue().clear();
					}
					u.addOrder(o);
				}else{
					o.use();
				}
			}
			return new OrderResponse();
		} catch (InvalidOrderException e) {
			log.warn("Order from Client " + getId() + " has failed", e);
			//getConnection().sendTCP(new Message("Order failed" , e.getMessage()));
			return new OrderResponse(e.getMessage());
		}
	}

	@Override
	public String issueCommand(String command) {
		return new Parser().parse(command, this);
	}
	
	public Island getZone(){
		return zone;
	}

	public void setZone(Island zone) {
		this.zone = zone;
	}

	public Object getLock() {
		return lock;
	}
	
	public void sendSnapshot(final Snapshot s){//TODO send a full snapshot using tcp every x seconds to maintain sync
		executor.submit(new Runnable(){
			public void run(){
				log.trace("Sending snapshot of size:" + (s.getEntities().size() + s.getDeletedEntites().size()));
				if(getConnection().sendUDP(s) == 0){//If sent 0 bytes:
					//remove();
					//TODO warn client about packet loss using TCP
				}
			}
		});
	}

	public void remove() {
		log.info("Removing client " + connection.getID() + "\" " + getUsername() + "\"");
		synchronized (clients) {
			clients.remove(this);
		}
		synchronized (getLock()) {
			setClient(null);
			setZone(null);
			setConnection(null);
		}
	}

	@Override
	public long getId() {
		return player.getID();
	}

	public void addAccess(Access newAccess) {
		access.add(newAccess);
	}

	public List<Access> getAccess() {
		return access;
	}
	
	public boolean hasAccess(Access check){
		return access.contains(check);
	}

	public String getUsername() {
		return player.getUsername();
	}

	public int getNextRMIId() {
		return counterRMI.getAndIncrement();
	}

	public void setObjectSpace(ObjectSpace objectSpace) {
		this.objectSpace = objectSpace;
	}

	public ObjectSpace getObjectSpace() {
		return objectSpace;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public Connection getConnection() {
		return connection;
	}
	
	public Player getPlayer(){
		return player;
	}
	
	@Override
	public void disconnected(Connection connection) {
		super.disconnected(connection);
		log.info("Connection lost to client " + connection.getID() + "\" " + getUsername() + "\"");
		remove();
	}

}



