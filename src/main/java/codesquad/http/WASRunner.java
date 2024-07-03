package codesquad.http;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codesquad.domain.HttpRequest;
import codesquad.domain.HttpResponse;
import codesquad.handler.mapping.HandlerMapping;
import codesquad.utils.HttpRequestUtil;
import codesquad.utils.HttpResponseUtil;

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
			var bo = new BufferedOutputStream(socket.getOutputStream())
		) {
			HttpRequest httpRequest = HttpRequestUtil.parseRequest(bi);
			log.debug("{}", httpRequest);

			HttpResponse httpResponse = HandlerMapping.getHandler(httpRequest).doService();
			HttpResponseUtil.writeResponse(bo, httpResponse);
			socket.close();
		} catch (IOException | IllegalArgumentException e) {
			log.error("io exception", e);
		}
	}
}
