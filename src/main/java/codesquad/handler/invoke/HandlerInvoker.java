package codesquad.handler.invoke;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import codesquad.annotation.LoginCheck;
import codesquad.annotation.PathVariable;
import codesquad.annotation.RequestMapping;
import codesquad.domain.HttpRequest;
import codesquad.domain.HttpResponse;
import codesquad.error.BaseException;
import codesquad.handler.mapping.HandlerMapping;
import codesquad.utils.UserThreadLocal;

public class HandlerInvoker {

	public static void invokeMethod(Method method, HttpRequest request, HttpResponse response) throws
		IllegalAccessException {
		if (method == null) {
			response.sendRedirect("/error.html?statusCode=404&message=Not%20Found");
			return;
		}
		if (isNotLogin(method)) {
			response.sendRedirect("/user/login.html");
			return;
		}

		List<Object> args = getMethodArgs(method, request, response);
		doInvoke(method, args);
	}

	private static List<Object> getMethodArgs(Method method, HttpRequest request, HttpResponse response) {
		List<Object> args = new ArrayList<>();
		Parameter[] parameters = method.getParameters();

		Map<String, String> pathVariables = getPathVariables(method, request);

		for (Parameter parameter : parameters) {
			if (parameter.getType() == HttpRequest.class) {
				args.add(request);
				continue;
			}
			if (parameter.getType() == HttpResponse.class) {
				args.add(response);
				continue;
			}
			applyPathVariable(parameter, pathVariables, args);
		}
		return args;
	}

	private static void doInvoke(Method method, List<Object> args) throws IllegalAccessException {
		Object instance = HandlerMapping.getHandlerInstance(method.getDeclaringClass());
		try {
			method.invoke(instance, args.toArray());
		} catch (InvocationTargetException e) {
			Throwable targetException = e.getCause();
			throw (BaseException)targetException;
		}
	}

	private static void applyPathVariable(Parameter parameter, Map<String, String> pathVariables, List<Object> args) {
		if (!parameter.isAnnotationPresent(PathVariable.class)) {
			return;
		}
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
	}

	private static boolean isNotLogin(Method method) {
		return method.isAnnotationPresent(LoginCheck.class) && !UserThreadLocal.isLogin();
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
}
