package codesquad.http;

import codesquad.domain.HttpRequest;
import codesquad.domain.HttpResponse;
import codesquad.handler.CommonHandler;
import codesquad.utils.HttpRequestParser;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WASRunner implements Runnable {

	private final Logger log = LoggerFactory.getLogger(WASRunner.class);
	private final Socket socket;

	public WASRunner(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		try (
			var bi = new BufferedInputStream(socket.getInputStream());
			var bo = new BufferedOutputStream(socket.getOutputStream());
		) {
			HttpRequest httpRequest = HttpRequestParser.parseRequest(bi);
			HttpResponse httpResponse = new CommonHandler().doService(httpRequest);
			log.debug("{}", httpRequest);
			socket.close();
		} catch (IOException | IllegalArgumentException e) {
			log.error(e.getMessage());
		}
	}
}
