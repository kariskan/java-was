package codesquad;

import codesquad.error.BaseException;
import codesquad.http.WASRunner;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

	private static final int PORT = 8080;
	private static final Logger log = LoggerFactory.getLogger(Main.class);

	static {
		String jdbcUrl = "jdbc:h2:~/testdb"; // 메모리 데이터베이스
		String username = "sa";
		String password = "";

		try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
			 Statement statement = connection.createStatement()) {

			InputStream inputStream = Main.class.getResourceAsStream("/schema.sql");
			if (inputStream == null) {
				throw new IllegalArgumentException("schema.sql 파일을 찾을 수 없습니다.");
			}

			try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
				StringBuilder sql = new StringBuilder();
				String line;
				while ((line = reader.readLine()) != null) {
					sql.append(line);
					if (line.trim().endsWith(";")) {
						statement.execute(sql.toString());
						sql.setLength(0);
					}
				}
			}

		} catch (SQLException | IOException e) {
			throw BaseException.serverException(e);
		}
	}

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
