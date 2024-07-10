package codesquad.http;

import codesquad.domain.HttpRequest;
import codesquad.domain.HttpResponse;
import codesquad.error.BaseException;
import codesquad.handler.HandlerMapping;
import codesquad.utils.HttpRequestUtil;
import codesquad.utils.HttpResponseUtil;
import codesquad.utils.ThreadLocalFilter;
import codesquad.utils.UserThreadLocal;
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
			var bo = new BufferedOutputStream(socket.getOutputStream())
		) {
			HttpResponse response = new HttpResponse();
			try {
				HttpRequest request = HttpRequestUtil.parseRequest(bi);
				ThreadLocalFilter.doFilter(request);
				HandlerMapping.getHandler(request).doService(request, response);
			} catch (BaseException e) {
				log.error("Error service request", e);
				response.sendRedirect("/error.html?statusCode=" + e.getStatus() + "&message=" + e.getMessage());
			} finally {
				HttpResponseUtil.writeResponse(bo, response);
				UserThreadLocal.remove();
				socket.close();
			}
		} catch (IOException e) {
			log.error("io exception", e);
		}
	}
}
