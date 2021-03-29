/**
 * IntermediateHost.java
 *
 * the intermediatehost thread will just act as a middle man in the connection between the client and the server. it doesn't do too much
 */

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class IntermediateHost implements Runnable {

    private DatagramSocket socket = null;
    private DatagramSocket socketToSendReceive = null;
    private InetAddress address = null;


    public IntermediateHost() throws Exception{
        socket = new DatagramSocket(23);
        socketToSendReceive = new DatagramSocket();
        address = InetAddress.getLocalHost();
    }

    public synchronized void run(){
    	int counter = 0;					// counter used to help terminate the thread, since i don't want it to run forever right now.
        System.out.println("Intermediate Host Running");
        while(true) {
        	/**
        	 * the code below just accepts messages from the client, sends them to the server, accepts replies from the server, and sends them back to the client
        	 */
            try {
                byte[] buffer = new byte[256];
                DatagramPacket packetToReceive = new DatagramPacket(buffer, buffer.length);
                socket.receive(packetToReceive);							// received from client

                String convertedMessage = new String(packetToReceive.getData(), 0, packetToReceive.getLength());
                buffer = convertedMessage.getBytes();

                convertedMessage = new String(buffer, 0, buffer.length);
                System.out.println("Intermediate Host is receiving this message " + convertedMessage);
                System.out.print("Intermediate Host is receiving these bits ");
                for(int i = 0; i < buffer.length; i += 1){
                    System.out.print(Integer.toBinaryString(buffer[i]) + " ");
                }
                System.out.println();

                int port = packetToReceive.getPort();
                InetAddress backAddress = packetToReceive.getAddress();

                packetToReceive = new DatagramPacket(buffer, buffer.length, address, 69);
                socketToSendReceive.send(packetToReceive);							// sent to server

                convertedMessage = new String(buffer, 0, buffer.length);
                System.out.println("Intermediate Host is sending this message " + convertedMessage);
                System.out.print("Intermediate Host is sending these bits ");
                for(int i = 0; i < buffer.length; i += 1){
                    System.out.print(Integer.toBinaryString(buffer[i]) + " ");
                }
                System.out.println();
                
                if(counter == 10) {			// this if statement is placed here since i am expecting no return on my 11th run on this thread. i want this thread to end 
                	break;
                }

                DatagramPacket packetToSend = new DatagramPacket(buffer, buffer.length);
                socketToSendReceive.receive(packetToSend);								// received from server

                convertedMessage = new String(packetToSend.getData(), 0, packetToSend.getLength());
                buffer = convertedMessage.getBytes();

                convertedMessage = new String(buffer, 0, buffer.length);
                System.out.println("Intermediate Host is receiving this message " + convertedMessage);
                System.out.print("Intermediate Host is receiving these bits ");
                for(int i = 0; i < buffer.length; i += 1){
                    System.out.print(Integer.toBinaryString(buffer[i]) + " ");
                }
                System.out.println();

                packetToSend = new DatagramPacket(buffer, buffer.length, backAddress, port);
                socketToSendReceive.send(packetToSend);									// sent to client

                convertedMessage = new String(buffer, 0, buffer.length);
                System.out.println("Intermediate Host is sending this message " + convertedMessage);
                System.out.print("Intermediate Host is sending these bits ");
                for(int i = 0; i < buffer.length; i += 1){
                    System.out.print(Integer.toBinaryString(buffer[i]) + " ");
                }
                System.out.println();
                //socket.close();
                counter += 1;

            } catch (Exception E) {

            }
        }
    }
}
