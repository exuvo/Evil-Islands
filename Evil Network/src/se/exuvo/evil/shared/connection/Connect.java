package se.exuvo.evil.shared.connection;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Connect extends Remote{
	
	public long getVersion() throws RemoteException;
	
	public boolean register(String username, String password) throws RemoteException;


}
