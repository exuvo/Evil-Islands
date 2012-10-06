package se.exuvo.evil.shared.connection;

import java.rmi.Remote;
import java.rmi.RemoteException;

import se.exuvo.evil.shared.world.NetZone;
import se.exuvo.evil.shared.world.Snapshot;

public interface ClientCommands extends Remote{
	
	public long getVersion();

}
