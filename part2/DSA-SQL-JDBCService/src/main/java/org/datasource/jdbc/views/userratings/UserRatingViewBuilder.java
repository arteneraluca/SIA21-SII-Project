package org.datasource.jdbc.views.userratings;

import org.datasource.jdbc.JDBCDataSourceConnector;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Service
public class UserRatingViewBuilder {
	private static final Logger logger = Logger.getLogger(UserRatingViewBuilder.class.getName());
	private static final String SQL_USER_RATINGS_SELECT =
			"SELECT bgg_id, rating, username FROM user_ratings ORDER BY bgg_id, username";
	private static final String SQL_FETCH_SELECT = """
			SELECT *
			  FROM (
			       SELECT Q_.*,
			              ROW_NUMBER() OVER(
			                      ORDER BY 1
			              ) RN___
			         FROM (
			              %s
			       ) Q_
			) Q__
			 WHERE RN___ BETWEEN ? AND ?
			""";

	private final JDBCDataSourceConnector jdbcConnector;
	private Integer fetchOffset = -1;
	private Integer fetchSize = 25;
	private List<UserRatingView> userRatingViewList = new ArrayList<>();

	public UserRatingViewBuilder(JDBCDataSourceConnector jdbcConnector) {
		this.jdbcConnector = jdbcConnector;
	}

	public List<UserRatingView> getViewList() {
		return this.userRatingViewList;
	}

	public UserRatingViewBuilder build() {
		logger.info(">>> Building UserRatingView: fetchOffset=" + fetchOffset + ", fetchSize=" + fetchSize);
		try (Connection jdbcConnection = jdbcConnector.getConnection()) {
			String sql = SQL_USER_RATINGS_SELECT;
			PreparedStatement selectStmt;
			if (fetchOffset != null && fetchOffset > 0) {
				sql = String.format(SQL_FETCH_SELECT, SQL_USER_RATINGS_SELECT);
				selectStmt = jdbcConnection.prepareStatement(sql);
				selectStmt.setInt(1, fetchOffset);
				selectStmt.setInt(2, fetchOffset + fetchSize);
			} else {
				selectStmt = jdbcConnection.prepareStatement(sql);
			}

			ResultSet rs = selectStmt.executeQuery();
			userRatingViewList = new ArrayList<>();
			while (rs.next()) {
				userRatingViewList.add(new UserRatingView(
						rs.getInt("bgg_id"),
						rs.getBigDecimal("rating"),
						rs.getString("username")));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return this;
	}

	public UserRatingViewBuilder setFetchOffset(Integer fetchOffset) {
		if (fetchOffset != null) {
			this.fetchOffset = fetchOffset;
		}
		return this;
	}

	public UserRatingViewBuilder setFetchSize(Integer fetchSize) {
		if (fetchSize != null) {
			this.fetchSize = fetchSize;
		}
		return this;
	}
}
