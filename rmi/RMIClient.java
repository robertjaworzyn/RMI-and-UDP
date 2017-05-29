/*
 * Created on 01-Mar-2016
 */
package rmi;

import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import common.MessageInfo;

public class RMIClient {

    public static void main(String[] args) {

	RMIServerI iRMIServer = null;

	// Check arguments for Server host and number of messages
	if (args.length < 2){
	    System.out.println("Needs 2 arguments: ServerHostName/IPAddress, TotalMessageCount");
	    System.exit(-1);
	}
	
	String urlServer = new String("rmi://" + args[0] + "/RMIServer");
	int numMessages = Integer.parseInt(args[1]);

	if (System.getSecurityManager() == null) {
	    System.setSecurityManager(new SecurityManager());
	}
	
	try {
	    Registry registry = LocateRegistry.getRegistry(args[0], 3031);
	    
	    try {
	        iRMIServer = (RMIServerI) registry.lookup("RMIServer");
	    } catch (NotBoundException e) {
		e.printStackTrace();
	    }
	    for (int i = 0; i < numMessages; i++) {
		String message = Integer.toString(numMessages) + ';' + Integer.toString(i+1);
		MessageInfo msg = null;
		try {
		    msg = new MessageInfo(message);
		} catch(Exception e) {
		    System.out.println("Could not construct MessageInfo");
		    e.printStackTrace();
		}
	   
		iRMIServer.receiveMessage(msg);
	    
	    }
	} catch(RemoteException e) {
	    System.out.println("Remote problems again");
	    e.printStackTrace();
	}
	

	
	
	// TO-DO: Initialise Security Manager

	// TO-DO: Bind to RMIServer

	// TO-DO: Attempt to send messages the specified number of times

    }
}
