package codesquad.handler.mapping;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codesquad.annotation.RequestMapping;
import codesquad.domain.HttpRequest;
import codesquad.domain.HttpStatus;
import codesquad.error.BaseException;

public class HandlerMapping {

	private static final Logger log = LoggerFactory.getLogger(HandlerMapping.class);
	private static final Map<Pattern, Method> handlerMethods = new HashMap<>();
	private static final Map<Class<?>, Object> handlerInstances = new HashMap<>();
	private static Method staticResourceHandler;

	static {
		initialize();
	}

	private HandlerMapping() {
	}

	public static void initialize() {
		try {
			List<Class<?>> classes = getClasses("codesquad.handler");
			for (Class<?> clazz : classes) {
				Object instance = instantiateHandler(clazz);
				handlerInstances.put(clazz, instance);
				registerHandlerMethods(clazz, instance);
			}
		} catch (Exception e) {
			log.error("Error initializing handler mapping", e);
		}
	}

	private static Object instantiateHandler(Class<?> clazz) throws ReflectiveOperationException {
		Constructor<?> constructor = clazz.getDeclaredConstructor();
		constructor.setAccessible(true);
		return constructor.newInstance();
	}

	private static void registerHandlerMethods(Class<?> clazz, Object instance) {
		for (Method method : clazz.getDeclaredMethods()) {
			if (method.isAnnotationPresent(RequestMapping.class)) {
				RequestMapping mapping = method.getAnnotation(RequestMapping.class);
				String urlPattern = mapping.url();
				if ("/static".equals(urlPattern)) {
					staticResourceHandler = method;
				} else {
					String regex = urlPattern.replaceAll("\\{[^/]+\\}", "[^/]+");
					Pattern pattern = Pattern.compile(mapping.httpMethod() + " " + regex);
					handlerMethods.put(pattern, method);
				}
			}
		}
	}

	public static Method getHandler(HttpRequest request) {
		String key = request.getMethod() + " " + request.getUrl();
		boolean urlMatched = false;

		for (Map.Entry<Pattern, Method> entry : handlerMethods.entrySet()) {
			Pattern pattern = entry.getKey();
			if (pattern.matcher(key).matches()) {
				return entry.getValue();
			}
			if (pattern.matcher(".* " + request.getUrl()).matches()) {
				urlMatched = true;
			}
		}

		if (request.getUrl().matches(".*\\.(html|css|js|png|jpg|jpeg|gif|ico|svg)$")) {
			return staticResourceHandler;
		}

		if (urlMatched) {
			throw new BaseException(HttpStatus.METHOD_NOT_ALLOWED, "Method not allowed");
		}

		throw new BaseException(HttpStatus.NOT_FOUND, "Not Found");
	}

	public static Object getHandlerInstance(Class<?> clazz) {
		return handlerInstances.get(clazz);
	}

	private static List<Class<?>> getClasses(String packageName) throws IOException, ClassNotFoundException {
		List<Class<?>> classes = new ArrayList<>();
		String path = packageName.replace('.', '/');
		Enumeration<URL> resources = Thread.currentThread().getContextClassLoader().getResources(path);
		while (resources.hasMoreElements()) {
			URL resource = resources.nextElement();
			String protocol = resource.getProtocol();
			if ("jar".equals(protocol)) {
				classes.addAll(getClassesFromJar(resource, path));
			} else if ("file".equals(protocol)) {
				File directory = new File(URLDecoder.decode(resource.getFile(), "UTF-8"));
				if (directory.exists()) {
					classes.addAll(findClasses(directory, packageName));
				}
			}
		}
		return classes;
	}

	private static List<Class<?>> getClassesFromJar(URL resource, String path) throws
		IOException,
		ClassNotFoundException {
		List<Class<?>> classes = new ArrayList<>();
		JarURLConnection jarURLConnection = (JarURLConnection)resource.openConnection();
		try (JarFile jarFile = jarURLConnection.getJarFile()) {
			Enumeration<JarEntry> entries = jarFile.entries();
			while (entries.hasMoreElements()) {
				JarEntry entry = entries.nextElement();
				String entryName = entry.getName();
				if (entryName.startsWith(path) && entryName.endsWith(".class") && !entryName.contains("$")) {
					String className = entryName.replace('/', '.').substring(0, entryName.length() - 6);
					classes.add(Class.forName(className));
				}
			}
		}
		return classes;
	}

	private static List<Class<?>> findClasses(File directory, String packageName) throws ClassNotFoundException {
		List<Class<?>> classes = new ArrayList<>();
		File[] files = directory.listFiles();
		if (files != null) {
			for (File file : files) {
				String fileName = file.getName();
				if (file.isDirectory()) {
					classes.addAll(findClasses(file, packageName + "." + fileName));
				} else if (fileName.endsWith(".class") && !fileName.contains("$")) {
					classes.add(Class.forName(packageName + '.' + fileName.substring(0, fileName.length() - 6)));
				}
			}
		}
		return classes;
	}
}
