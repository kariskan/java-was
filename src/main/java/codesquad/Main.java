package codesquad;

import codesquad.db.csv.driver.CsvJdbcDriver;
import codesquad.error.BaseException;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codesquad.http.WASRunner;

public class Main {

	private static final int PORT = 8080;
	private static final Logger log = LoggerFactory.getLogger(Main.class);

	static {
		String dataDir = "./data"; // 데이터 파일이 저장될 경로를 설정
		String jdbcUrl = "jdbc:csv:" + dataDir;

		// 데이터 디렉토리를 생성합니다.
		File directory = new File(dataDir);
		if (!directory.exists()) {
			if (directory.mkdirs()) {
				log.info("Data directory created successfully.");
			} else {
				throw new RuntimeException("Failed to create data directory.");
			}
		}

		try {
			DriverManager.registerDriver(new CsvJdbcDriver());
		} catch (SQLException e) {
			throw BaseException.serverException(e);
		}

		try (Connection connection = DriverManager.getConnection(jdbcUrl)) {
			InputStream inputStream = Main.class.getResourceAsStream("/schema.sql");
			if (inputStream == null) {
				throw BaseException.serverException();
			}
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
				StringBuilder sql = new StringBuilder();
				String line;
				while ((line = reader.readLine()) != null) {
					sql.append(line);
					if (line.trim().endsWith(";")) {
						try (PreparedStatement pstmt = connection.prepareStatement(sql.toString())) {
							pstmt.executeUpdate();
							sql.setLength(0);
						}
					}
				}
			}
		} catch (SQLException | IOException e) {
			e.printStackTrace();
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
