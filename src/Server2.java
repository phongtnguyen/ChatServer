/*import java.net.*;
import java.util.*;
import java.io.*;

public class Server2 {
	final static int PORT = 9876;
	static Vector vector = new Vector();
	
	public static void main(String[] args) {
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(PORT);
		} catch (IOException io) {
			System.exit(1);
		}
		Socket clientSocket = null;
		while (true) {
			try {
				clientSocket = serverSocket.accept();
				DataInputStream in = new DataInputStream(clientSocket.getInputStream());
				DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
				vector.add(out);
				Chatter2 client = new Chatter2(clientSocket, in);
				client.start();
			} catch (IOException io) {
				System.out.println("Accept fail " + PORT + ", " + io);
				continue;
			}
		}
	}
}

class Chatter2 extends Thread {
	private Socket socket;
	private DataInputStream in;
	
	public Chatter2(Socket s, DataInputStream i) {
		socket = s;
		in = i;
	}
	
	public void run() {
		try {
			String msg;
			BufferedReader read = new BufferedReader(new InputStreamReader(in));
			while((msg=read.readLine())!=null) { System.out.println("Message in Chatter "+msg);
				 for (int i=0; i<Server.vector.size(); i++) { System.out.println("Print out message "+msg);
				 	DataOutputStream out = (DataOutputStream) Server.vector.get(i);
					 PrintWriter p = new PrintWriter(new BufferedOutputStream(out));
					 p.println(msg);
					 p.flush();
				 }
			}
		} catch (IOException e) {}
		
	}
}*/

import java.net.*;
import java.util.*;
import java.io.*;

public class Server2 {
	final static int PORT = 9876;
	public static void main(String[] args) {
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(PORT);
		} catch (IOException e) {
			System.exit(1);
		}

		while (true) {
			try {
				Socket clientSocket = serverSocket.accept();
				Vector vector = new Vector();
				DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
				vector.add(out);
				//don't pass the vector to Chat class because its size won't change.
				Chat chat = new Chat(clientSocket, out);
				chat.start();
			} catch (IOException e) {}
		}
	}
}

class Chat extends Thread {
	private Socket socket;
	private DataOutputStream out;
	public Chat(Socket s, DataOutputStream o) {
		socket = s;
		out = o;
	}
	public void run() {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String s;
			while ((s=br.readLine())!=null)
				for (int i=0; i<Server.vector.size(); i++) { System.out.println("Print message");
					DataOutputStream out = (DataOutputStream) Server.vector.get(i);
					PrintWriter print = new PrintWriter(new BufferedOutputStream(out));
					print.println(s);
					print.flush();
				}
		} catch (IOException e) {}
	}
}