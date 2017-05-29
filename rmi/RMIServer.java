/*
 * Created on 01-Mar-2016
 */
package rmi;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;

import common.*;

public class RMIServer extends UnicastRemoteObject implements RMIServerI {

    private int totalMessages = -1;
    private int[] receivedMessages;
    private boolean messageReceived = false;
    private boolean finished = false;

    public RMIServer() throws RemoteException {
    }

    public void receiveMessage(MessageInfo msg) throws RemoteException {
	
	totalMessages = msg.totalMessages;
		
	if (!messageReceived) {
	    receivedMessages = new int[totalMessages+1];
	    for (int i = 1; i <= totalMessages; i++) {
		receivedMessages[i] = 0;
	    }
	}	
	
	messageReceived = true;

	//System.out.println(msg);

	receivedMessages[msg.messageNum] = 1;

	if (msg.messageNum == totalMessages) {
	    checkMissingMessages();
	    finished = true;
	}

    }

    public void checkMissingMessages() {
	System.out.println("The following messages are missing:\n");
	for (int i = 1; i <= totalMessages; i++) {
	    if (receivedMessages[i] == 0) {
		System.out.println(i);
	    }
	}
	System.out.println("\n");
    }

    public static void main(String[] args) {

	RMIServer rmis = null;
        String serverURL =  new String("rmi://localhost:3031/RMIServer");	
	if (System.getSecurityManager() == null) {
	    System.setSecurityManager(new SecurityManager());
	}
	
	try {
	    rmis = new RMIServer();
	    rebindServer(serverURL, rmis);
	} catch (RemoteException e) {
	    System.out.println("Cannot create server object, throwing " + e);
	    System.exit(-1);
	}
    }

    protected static void rebindServer(String serverURL, RMIServer server) {

	Registry registry = null;

	System.out.println(serverURL);

	try {
	    registry = LocateRegistry.createRegistry(3031);
	    try {
		Naming.rebind(serverURL,server);
	    } catch (MalformedURLException e) {
		System.out.println("FUCK YOU");
	    }
	    
	} catch (RemoteException ex) {
	    System.out.println("Cannot locate registry, throwing exception");
	    ex.printStackTrace();
	    	    
	}
	
	System.out.println("Server is bound");
    }
}
