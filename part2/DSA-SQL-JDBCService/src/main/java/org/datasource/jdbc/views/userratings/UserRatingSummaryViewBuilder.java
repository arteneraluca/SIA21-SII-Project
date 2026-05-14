package org.datasource.jdbc.views.userratings;

import org.datasource.jdbc.JDBCDataSourceConnector;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserRatingSummaryViewBuilder {
	private static final String SQL_USER_RATING_SUMMARY_SELECT = """
			SELECT bgg_id,
			       COUNT(*) AS rating_count,
			       ROUND(AVG(rating), 2) AS average_rating,
			       MIN(rating) AS minimum_rating,
			       MAX(rating) AS maximum_rating
			  FROM user_ratings
			 GROUP BY bgg_id
			 ORDER BY rating_count DESC, average_rating DESC, bgg_id
			""";

	private final JDBCDataSourceConnector jdbcConnector;
	private List<UserRatingSummaryView> userRatingSummaryViewList = new ArrayList<>();

	public UserRatingSummaryViewBuilder(JDBCDataSourceConnector jdbcConnector) {
		this.jdbcConnector = jdbcConnector;
	}

	public List<UserRatingSummaryView> getViewList() {
		return this.userRatingSummaryViewList;
	}

	public UserRatingSummaryViewBuilder build() {
		try (Connection jdbcConnection = jdbcConnector.getConnection();
			 Statement selectStmt = jdbcConnection.createStatement();
			 ResultSet rs = selectStmt.executeQuery(SQL_USER_RATING_SUMMARY_SELECT)) {
			userRatingSummaryViewList = new ArrayList<>();
			while (rs.next()) {
				userRatingSummaryViewList.add(new UserRatingSummaryView(
						rs.getInt("bgg_id"),
						rs.getLong("rating_count"),
						rs.getBigDecimal("average_rating"),
						rs.getBigDecimal("minimum_rating"),
						rs.getBigDecimal("maximum_rating")));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return this;
	}
}
