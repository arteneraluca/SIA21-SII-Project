package org.datasource.jdbc.importer;

import org.datasource.jdbc.JDBCDataSourceConnector;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.logging.Logger;

@Service
public class UserRatingsDataInitializer implements ApplicationRunner {
	private static final Logger logger = Logger.getLogger(UserRatingsDataInitializer.class.getName());
	private static final String CREATE_USER_RATINGS_TABLE = """
			CREATE TABLE IF NOT EXISTS user_ratings
			(
			    bgg_id   INTEGER,
			    rating   NUMERIC(3, 1),
			    username VARCHAR(100)
			)
			""";
	private static final String COUNT_USER_RATINGS = "SELECT COUNT(*) FROM user_ratings";

	private final JDBCDataSourceConnector jdbcConnector;

	public UserRatingsDataInitializer(JDBCDataSourceConnector jdbcConnector) {
		this.jdbcConnector = jdbcConnector;
	}

	@Override
	public void run(ApplicationArguments args) {
		try (Connection jdbcConnection = jdbcConnector.getConnection()) {
			createSchemaIfMissing(jdbcConnection);
			logger.info(">>> user_ratings rows available: " + countRatings(jdbcConnection));
		} catch (Exception ex) {
			logger.warning(">>> user_ratings initialization failed: " + ex.getMessage());
			ex.printStackTrace();
		}
	}

	private void createSchemaIfMissing(Connection jdbcConnection) throws Exception {
		try (Statement statement = jdbcConnection.createStatement()) {
			statement.execute(CREATE_USER_RATINGS_TABLE);
		}
	}

	private int countRatings(Connection jdbcConnection) throws Exception {
		try (Statement statement = jdbcConnection.createStatement();
			 ResultSet rs = statement.executeQuery(COUNT_USER_RATINGS)) {
			return rs.next() ? rs.getInt(1) : 0;
		}
	}
}
