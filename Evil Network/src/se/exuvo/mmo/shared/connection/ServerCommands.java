package se.exuvo.mmo.shared.connection;

import java.rmi.Remote;
import java.rmi.RemoteException;

import se.exuvo.mmo.shared.world.NetOrder;
import se.exuvo.mmo.shared.world.NetZone;

public interface ServerCommands extends Remote{
	
	public OrderResponse issueOrder(NetOrder order, boolean replace);
	public String issueCommand(String command);
	
	public long getId();

}
