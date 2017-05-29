/*
 * Created on 01-Mar-2016
 */
package udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import common.MessageInfo;

public class UDPClient {

    private DatagramSocket sendSoc;

    public static void main(String[] args) {
	InetAddress serverAddr = null;
	int recvPort;
	int countTo;
	String message;

	// Get the parameters
	if (args.length < 3) {
	    System.err.println("Arguments required: server name/IP, recv port, message count");
	    System.exit(-1);
	}

	try {
	    serverAddr = InetAddress.getByName(args[0]);
	} catch (UnknownHostException e) {
	    System.out.println("Bad server address in UDPClient, " + args[0] + " caused an unknown host exception " + e);
	    System.exit(-1);
	}
	recvPort = Integer.parseInt(args[1]);
	countTo = Integer.parseInt(args[2]);
	
	UDPClient client = new UDPClient();
	client.testLoop(serverAddr, recvPort, countTo);
	client.sendSoc.close();
	// TO-DO: Construct UDP client class and try to send messages
    }

    public UDPClient() {
	// TO-DO: Initialise the UDP socket for sending data
	try {
	    sendSoc = new DatagramSocket();
	} catch (SocketException e) {
	    System.out.println("Unable to create UDP sending socket, throwing exception" + e);
	    System.exit(-1);
	}
    }

    private void testLoop(InetAddress serverAddr, int recvPort, int countTo) {
	int tries = 0;
	//System.out.println(countTo);
	while (tries < countTo)	{
	    //  System.out.println(tries);
	    String message = Integer.toString(countTo) + ';' + Integer.toString(tries+1);
	    send(message, serverAddr, recvPort);
	    tries++;
	}

	
	// TO-DO: Send the messages to the server
    }

    private void send(String payload, InetAddress destAddr, int destPort) {
	int payloadSize = payload.length();
	byte[] pktData;
	DatagramPacket pkt;

	pktData = new byte[payloadSize];
	pktData = payload.getBytes();
	pkt = new DatagramPacket(pktData, pktData.length, destAddr, destPort);
	try {
	    sendSoc.send(pkt);
	} catch(IOException e) {
	    System.out.println("Error sending packet, throwing exception " + e);
	    System.exit(-1);
	}
	
    }
}
