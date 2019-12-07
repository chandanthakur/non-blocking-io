import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 
 * Test client for Blocking IO server
 *
 */
public class TestClient {
	public static void main(String[] args) throws IOException {
		Runnable client = new Runnable() {
			@Override
			public void run() {
				try {
					new TestClient().startClient();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		};

		int maxClients = 4;
		for(int kk = 0; kk < maxClients; kk++) {
			new Thread(client, "client-" + String.valueOf(kk)).start();
		}
	}

	public void startClient() throws IOException, InterruptedException {

		String hostName = "localhost";
		int portNumber = 4444;
		String threadName = Thread.currentThread().getName();
		List<String> messages = new LinkedList<String>();
		for(int kk = 0; kk < 10000000; kk++) {
			String msg = threadName + ": msg " + kk;
			messages.add(msg);
		}

		try {
			Socket echoSocket = new Socket(hostName, portNumber);
			PrintWriter out = new PrintWriter(echoSocket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
			for (int i = 0; i < messages.size(); i++) {
				BufferedReader stdIn = new BufferedReader(new StringReader(messages.get(i)));
				String userInput;
				while ((userInput = stdIn.readLine()) != null) {
					out.println(userInput); // write to server
					System.out.println("echo: " + in.readLine()); // Wait for the server to
				}
			}
		} catch (UnknownHostException e) {
			System.err.println("Unknown host " + hostName);
			System.exit(1);
		} catch (IOException e) {
			System.err.println("Couldn't get I/O for the connection to " + hostName + ".." + e.toString());
			System.exit(1);
		}
	}

}