package se.exuvo.mmo.shared.connection;

import java.io.IOException;

import org.apache.log4j.Logger;

import se.exuvo.mmo.shared.ClassFinder;
import se.exuvo.mmo.shared.world.NetAbility;
import se.exuvo.mmo.shared.world.NetBlueprint;
import se.exuvo.mmo.shared.world.NetBlueprintAdd;
import se.exuvo.mmo.shared.world.NetBuilding;
import se.exuvo.mmo.shared.world.NetCollidable;
import se.exuvo.mmo.shared.world.NetDirt;
import se.exuvo.mmo.shared.world.NetDoor;
import se.exuvo.mmo.shared.world.NetEntity;
import se.exuvo.mmo.shared.world.NetHero;
import se.exuvo.mmo.shared.world.NetMovable;
import se.exuvo.mmo.shared.world.NetOrder;
import se.exuvo.mmo.shared.world.NetSquare;
import se.exuvo.mmo.shared.world.NetUnit;
import se.exuvo.mmo.shared.world.NetZone;
import se.exuvo.mmo.shared.world.NetZonePart;
import se.exuvo.mmo.shared.world.Position;
import se.exuvo.mmo.shared.world.Snapshot;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.compress.DeflateCompressor;
import com.esotericsoftware.kryo.serialize.FieldSerializer;
import com.esotericsoftware.kryonet.EndPoint;
import com.esotericsoftware.kryonet.rmi.ObjectSpace;

public class Network {
	public static final long serialVersionUID = 1;;
	private static final Logger log = Logger.getLogger(Network.class);
	
	static public void register(EndPoint endPoint) {
	    Kryo kryo = endPoint.getKryo();
	    ObjectSpace.registerClasses(kryo);
	    //kryo.setRegistrationOptional(true);//Less efficient than registering, seems to fail as both sides does not have it(a class) registered at the time of sending
	    
	    kryo.register(Connect.class);
	    kryo.register(ClientCommands.class);
	    kryo.register(Login.class);
	    kryo.register(LoginResponse.class);
	    kryo.register(LoginResponse.Cause.class);
	    kryo.register(ClientCommands.class);
	    kryo.register(ServerCommands.class);
	    
	    kryo.register(int[].class);
	    kryo.register(boolean[].class);
	    kryo.register(float[].class);
	    kryo.register(double[].class);
	    kryo.register(byte[].class);
	    kryo.register(java.util.ArrayList.class);
	    kryo.register(java.util.HashMap.class);
	    
	    addPackage("org.newdawn.slick.geom", kryo);
	    
	    kryo.register(Message.class);
	    kryo.register(OrderResponse.class);
	    
	    kryo.register(NetAbility.class);
	    kryo.register(NetAbility.validTarget.class);
	    kryo.register(NetBlueprint.class);
	    kryo.register(NetBlueprintAdd.class);
	    kryo.register(NetBuilding.class);
	    kryo.register(NetCollidable.class);
	    kryo.register(NetDirt.class);
	    kryo.register(NetDoor.class);
	    kryo.register(NetEntity.class);
	    kryo.register(NetHero.class);
	    kryo.register(NetMovable.class);
	    kryo.register(NetOrder.class);
	    kryo.register(NetSquare.class);
	    kryo.register(NetSquare[][].class);
	    kryo.register(NetUnit.class);
	    kryo.register(NetZone.class, new DeflateCompressor(new FieldSerializer(kryo, NetZone.class), 1*1024*1024));
	    kryo.register(NetZonePart.class);
	    kryo.register(Position.class);
	    kryo.register(Snapshot.class);
	}
	
	private static void addPackage(String packageName, Kryo kryo){
		try {
			for(Class<?> c :  ClassFinder.getClasses(packageName)){
				 kryo.register(c);
			}
		} catch (ClassNotFoundException e) {
			log.warn("Failed to add package classes to kryo", e);
		} catch (IOException e) {
			log.warn("Failed to add package classes to kryo", e);
		}
	}
}
