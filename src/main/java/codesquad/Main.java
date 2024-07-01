package codesquad;

import java.io.IOException;
import java.net.ServerSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Main {

	private static final int PORT = 8080;
	private static final Logger LOG = LoggerFactory.getLogger(Main.class);

	public static void main(String[] args) {
		LOG.debug("Listening for connection on port 8080 ....");
		try (ServerSocket serverSocket = new ServerSocket(PORT)) {
			while (true) {
				var listen = serverSocket.accept();
				new WASRunner(listen).start();
			}
		} catch (IOException e) {
			LOG.error("\t* Exception : {} - {}", e.getClass().getName(), e.getMessage());
		}
		LOG.debug("client disconnected");
	}
}
