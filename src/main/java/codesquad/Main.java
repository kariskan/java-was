package codesquad;

import codesquad.http.WASRunner;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

	private static final int PORT = 8080;
	private static final Logger log = LoggerFactory.getLogger(Main.class);

	public static void main(String[] args) {
		log.debug("Listening for connection on port 8080 ....");
		ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(10, 30, 10, TimeUnit.SECONDS,
			new LinkedBlockingQueue<>(10));
		threadPoolExecutor.prestartAllCoreThreads();
		try (ServerSocket listen = new ServerSocket(PORT)) {
			while (true) {
				var connection = listen.accept();
				threadPoolExecutor.execute(new WASRunner(connection));
			}
		} catch (IOException e) {
			log.error("\t* Exception : {} - {}", e.getClass().getName(), e.getMessage());
		}
	}
}
