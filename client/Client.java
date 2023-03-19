import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
	private Socket socket;
	private static BufferedReader in;
	private static PrintWriter out;

	public Client(Socket client) {
		try {
			this.socket = client;
			in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			out = new PrintWriter(client.getOutputStream(), true);
		} catch (IOException e) {
			try {
				socket.close();
				in.close();
				out.close();
				System.err.println("error");
				e.printStackTrace();

			} catch (IOException ioException) {
				ioException.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		try {
			Socket client = new Socket("Localhost", 9999);
			Client c = new Client(client);
			StringBuilder s = new StringBuilder();
			String input;

			//Parse the command line arguments.
			for (String arg : args) {
				s.append(arg).append(" ");
			}
			out.println(s.toString());

			//read from server.
			try {
				while ((input = in.readLine()) != null) {
					if (input.equals("exit")) {
						break;
					}
					System.out.println(input);
				}
			}catch (IOException e){
				System.err.println("cannot read from server");
			}
		}catch (IOException e) {
			System.err.println("Cannot connect to server");
		}
	}
}
