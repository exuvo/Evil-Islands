package se.exuvo.mmo.shared.connection;

import java.rmi.Remote;
import java.rmi.RemoteException;

import se.exuvo.mmo.shared.world.NetZone;
import se.exuvo.mmo.shared.world.Snapshot;

public interface ClientCommands extends Remote{
	
	public long getVersion();

}
