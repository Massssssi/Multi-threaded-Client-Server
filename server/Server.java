import java.net.*;
import java.io.*;
import java.util.Arrays;
import java.util.concurrent.*;
public class Server {

	public ServerSocket server = null;
	public static String[][] matrix;

	//number of lists and number of members
	public Server(int nlist,int nMembers) {
		matrix = new String[nlist][nMembers];
		for (String[] strings : matrix) {
			Arrays.fill(strings, null);
		}
	}

	public void runServer() throws IOException {
		// Try to open up the listening port
		try {
			server = new ServerSocket(9999);
		} catch (IOException e) {
			System.err.println("Could not listen to port: 9999.");
			System.exit(-1);
		}

		// Initialise the executor.
		ExecutorService service = Executors.newFixedThreadPool(25);

		// For each new client, submit a new handler to the thread pool.
		while( true )
		{
			Socket client = server.accept();
			service.submit( new ClientHandler(client) );
		}
	}


	public static void main(String[] args) throws IOException {
			Server server = new Server(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
			server.runServer();
	}
}