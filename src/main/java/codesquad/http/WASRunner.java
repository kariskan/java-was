package codesquad.http;

import codesquad.annotation.LoginCheck;
import codesquad.annotation.PathVariable;
import codesquad.annotation.RequestMapping;
import codesquad.domain.HttpRequest;
import codesquad.domain.HttpResponse;
import codesquad.error.BaseException;
import codesquad.filter.ThreadLocalFilter;
import codesquad.handler.mapping.HandlerMapping;
import codesquad.utils.HttpRequestUtil;
import codesquad.utils.HttpResponseUtil;
import codesquad.utils.UserThreadLocal;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WASRunner implements Runnable {

	private final Logger log = LoggerFactory.getLogger(WASRunner.class);
	private final Socket socket;

	public WASRunner(Socket socket) {
		this.socket = socket;
	}

	private static void invokeMethod(Method method, HttpRequest request, HttpResponse response) throws
		IllegalAccessException {
		if (method != null) {
			if (method.isAnnotationPresent(LoginCheck.class) && !UserThreadLocal.isLogin()) {
				response.sendRedirect("/user/login.html");
				return;
			}

			List<Object> args = new ArrayList<>();
			Parameter[] parameters = method.getParameters();

			Map<String, String> pathVariables = getPathVariables(method, request);

			for (Parameter parameter : parameters) {
				if (parameter.isAnnotationPresent(PathVariable.class)) {
					PathVariable pathVariable = parameter.getAnnotation(PathVariable.class);
					String variableName = pathVariable.value();
					String value = pathVariables.get(variableName);

					if (parameter.getType() == Long.class) {
						args.add(Long.valueOf(value));
					} else if (parameter.getType() == Integer.class) {
						args.add(Integer.valueOf(value));
					} else {
						args.add(value);
					}
				} else {
					if (parameter.getType() == HttpRequest.class) {
						args.add(request);
					} else if (parameter.getType() == HttpResponse.class) {
						args.add(response);
					}
				}
			}

			Object instance = HandlerMapping.getHandlerInstance(method.getDeclaringClass());
			try {
				method.invoke(instance, args.toArray());
			} catch (InvocationTargetException e) {
				Throwable targetException = e.getCause();
				if (targetException instanceof BaseException) {
					throw (BaseException) targetException;
				} else {
					response.sendRedirect("/error.html?statusCode=500&message=Internal%20Server%20Error");
				}
			}
		} else {
			response.sendRedirect("/error.html?statusCode=404&message=Not%20Found");
		}
	}

	private static Map<String, String> getPathVariables(Method method, HttpRequest request) {
		String requestPath = request.getUrl();
		String[] requestSegments = requestPath.split("/");

		RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
		String urlPattern = requestMapping.url();
		String[] patternSegments = urlPattern.split("/");

		Map<String, String> pathVariables = new HashMap<>();

		for (int i = 0; i < patternSegments.length; i++) {
			if (patternSegments[i].startsWith("{") && patternSegments[i].endsWith("}")) {
				String variableName = patternSegments[i].substring(1, patternSegments[i].length() - 1);
				pathVariables.put(variableName, requestSegments[i]);
			}
		}
		return pathVariables;
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
