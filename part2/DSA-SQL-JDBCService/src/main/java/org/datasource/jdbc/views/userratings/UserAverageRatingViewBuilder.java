package org.datasource.jdbc.views.userratings;

import org.datasource.jdbc.JDBCDataSourceConnector;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserAverageRatingViewBuilder {
	private static final String SQL_USER_AVERAGE_RATINGS_SELECT = """
			SELECT username,
			       COUNT(*) AS rating_count,
			       ROUND(AVG(rating), 2) AS average_rating
			  FROM user_ratings
			 GROUP BY username
			 ORDER BY rating_count DESC, average_rating DESC, username
			""";

	private final JDBCDataSourceConnector jdbcConnector;
	private List<UserAverageRatingView> userAverageRatingViewList = new ArrayList<>();

	public UserAverageRatingViewBuilder(JDBCDataSourceConnector jdbcConnector) {
		this.jdbcConnector = jdbcConnector;
	}

	public List<UserAverageRatingView> getViewList() {
		return this.userAverageRatingViewList;
	}

	public UserAverageRatingViewBuilder build() {
		try (Connection jdbcConnection = jdbcConnector.getConnection();
			 Statement selectStmt = jdbcConnection.createStatement();
			 ResultSet rs = selectStmt.executeQuery(SQL_USER_AVERAGE_RATINGS_SELECT)) {
			userAverageRatingViewList = new ArrayList<>();
			while (rs.next()) {
				userAverageRatingViewList.add(new UserAverageRatingView(
						rs.getString("username"),
						rs.getLong("rating_count"),
						rs.getBigDecimal("average_rating")));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return this;
	}
}
