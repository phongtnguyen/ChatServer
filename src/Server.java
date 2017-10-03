/* 
	* Program Name: Lab 4 - Server
	* 
	* Student Name: Phong Nguyen
	* Semester: Spring 2016
	* Class-Section: COSC 20203
	* Instructor: Dr. Rinewalt
	* 
	* Program Overview:
	* 	This is a server program connecting clients and allowing them
	* to exchange messages.
	* 
	* Input:
	* 	None.
	* 
	* Output:
	* 	Messages from clients.
	* 
	* Program Limitations:
	* 	It has no GUI.
	* 
	* Significant Program Variables:
	* 	ServerSocket, Socket, Thread, Vector, BufferedReader, PrintWriter.
	*/

import java.net.*;
import java.util.*;
import java.io.*;

public class Server {
	final static int PORT = 9876;
	static Vector vector = new Vector();
	
	public static void main(String[] args) {
		ServerSocket serverSocket = null;
		System.out.println("Server is running.");
		try {
			serverSocket = new ServerSocket(PORT);
		} catch (IOException io) {
			System.out.println("Could not listen on port " + PORT + ", " + io);
			System.exit(1);
		}
		Socket clientSocket = null;
		while (true) {
			try {
				clientSocket = serverSocket.accept();
				//System.out.println("Connected to a client.");
				
				Chatter client = new Chatter(clientSocket, vector);
				vector.add(client);
				client.start();
				System.out.println("There are "+vector.size()+" connecting");
			} catch (IOException io) {
				System.out.println("Accept fail " + PORT + ", " + io);
				continue;
			}
		}
	}
	
	public static void removeClient(int pos){
		vector.remove(pos);
		System.out.println("removeClient now "+vector.size()+" clients");
	}
}

class Chatter extends Thread {
	private Socket socket;
	private Vector vec;
	private BufferedReader reader;
	
	public Chatter(Socket s, Vector v) {
		socket = s;
		vec = v;
	}
	
	public void run() {
		int pos = vec.indexOf(this);
		try {
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String msg;
			while ((msg=reader.readLine())!=null) {
				for (int i=0; i<vec.size(); i++) {
					Chatter ct = (Chatter) vec.get(i);
					Socket s = ct.getSocket();
					PrintWriter writer = new PrintWriter(new BufferedOutputStream(s.getOutputStream()));
					writer.println(msg);
					writer.flush();
				}
			}
			
			//while loop done = Disconnect button IS pressed. Only close client = doesn't work.
			socket.close();
			reader.close();
			Server.removeClient(pos);
		} catch (IOException e) { //Client is closed. Disconnect button is NOT pressed
			try {
				socket.close();
				reader.close();
			} catch (IOException io) {}
			Server.removeClient(pos);
		}
	}
	
	public Socket getSocket() {
		return socket;
	}
}