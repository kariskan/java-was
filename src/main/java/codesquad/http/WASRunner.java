package codesquad.http;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codesquad.domain.HttpRequest;
import codesquad.domain.HttpResponse;
import codesquad.error.BaseException;
import codesquad.handler.mapping.HandlerMapping;
import codesquad.utils.HttpRequestUtil;
import codesquad.utils.HttpResponseUtil;
import codesquad.utils.ThreadLocalFilter;
import codesquad.utils.UserThreadLocal;

public class WASRunner implements Runnable {

	private final Logger log = LoggerFactory.getLogger(WASRunner.class);
	private final Socket socket;

	public WASRunner(Socket socket) {
		this.socket = socket;
	}

	private static void invokeMethod(Method method, HttpRequest request, HttpResponse response) throws
		IllegalAccessException {
		if (method != null) {
			Object instance = HandlerMapping.getHandlerInstance(method.getDeclaringClass());
			try {
				method.invoke(instance, request, response);
			} catch (InvocationTargetException e) {
				Throwable targetException = e.getCause();
				if (targetException instanceof BaseException) {
					throw (BaseException)targetException;
				} else {
					response.sendRedirect("/error.html?statusCode=500&message=Internal%20Server%20Error");
				}
			}
		} else {
			response.sendRedirect("/error.html?statusCode=404&message=Not%20Found");
		}
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
				Method method = HandlerMapping.getHandler(request);
				invokeMethod(method, request, response);
			} catch (BaseException e) {
				log.error("Error service request", e);
				response.sendRedirect("/error.html?statusCode=" + e.getStatus() + "&message=" + e.getMessage());
			} finally {
				HttpResponseUtil.writeResponse(bo, response);
				UserThreadLocal.remove();
				socket.close();
			}
		} catch (IOException | IllegalAccessException e) {
			log.error("io exception", e);
		}
	}
}
