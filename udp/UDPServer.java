/*
 * Created on 01-Mar-2016
 */
package udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Arrays;

import common.MessageInfo;

public class UDPServer {

    private DatagramSocket recvSoc;
    private int totalMessages = -1;
    private int[] receivedMessages;
    private boolean finished;
    private boolean messageReceived = false;
    
    private void run() {
	int pacSize;
	byte[] pacData;
	DatagramPacket pac;

	pacSize = 256;
	pacData = new byte[pacSize];
	pac = new DatagramPacket(pacData, pacSize);
	finished = false;
	while (!finished){

	    try {
		recvSoc.receive(pac);
	    } catch(IOException e) {
		System.out.println("Error receiving packet, throwing exception " + e);
		checkMissingMessages();
		System.exit(-1);
	    }
	
	    String received = new String(pac.getData(), 0, pac.getLength());
	    
	    processMessage(received);
	    
	    // TO-DO: Receive the messages and process them by calling processMessage(...).
	    //        Use a timeout (e.g. 30 secs) to ensure the program doesn't block forever
	}
	
    }
    
    public void processMessage(String data) {
	MessageInfo msg = null;

	try {
	    msg = new MessageInfo(data);
	} catch(Exception e) {
	    System.out.println("Could not construct MessageInfo");
	    System.exit(-1);
	}

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
	
	// TO-DO: Use the data to construct a new MessageInfo object
	
	// TO-DO: On receipt of first message, initialise the receive buffer
	
	// TO-DO: Log receipt of the message
	
	// TO-DO: If this is the last expected message, then identify
	//        any missing messages
	
    }

    private void checkMissingMessages() {
	System.out.println("The following messages are missing:\n");
	for (int i = 1; i <= totalMessages; i++) {
	    //System.out.println(receivedMessages[i]);
	    if (receivedMessages[i] == 0) {
		System.out.println(i + " ");
	    }
	}
	System.out.println("\n");
    }
    
    public UDPServer(int rp) {
	// TO-DO: Initialise UDP socket for receiving data
	try {
	    recvSoc = new DatagramSocket(rp);
	    recvSoc.setSoTimeout(10000);
	} catch(SocketException e) {
	    System.out.println("Unable to create UDP receiver socket, throwing exception " + e);
	    System.exit(-1);
	}
	
	// Done Initialisation
	System.out.println("UDPServer ready");
    }

    public static void main(String args[]) {
	int recvPort;

	// Get the parameters from command line
	if (args.length < 1) {
	    System.err.println("Arguments required: recv port");
	    System.exit(-1);
	}
	recvPort = Integer.parseInt(args[0]);
	UDPServer server = new UDPServer(recvPort);
	server.run();
	// TO-DO: Construct Server object and start it by calling run().
    }

}
