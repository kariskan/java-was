package codesquad;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WASRunner extends Thread {

	private final Logger log = LoggerFactory.getLogger(WASRunner.class);

	private final Socket socket;

	public WASRunner(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		try (
			var br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			var bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		) {
			String s;
			while ((s = br.readLine()) != null) {
				log.debug("{}", s);
			}
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}
}
