package org.datasource.jpa.views.games;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.datasource.jpa.JPADataSourceConnector;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GameRatingStatsViewBuilderSQL {
	private String SQL_GAME_RATING_STATS_SELECT =
			"""
					SELECT COUNT(*) AS gameCount,
					       ROUND(AVG(AVG_RATING), 4) AS averageRating,
					       MIN(AVG_RATING) AS minimumRating,
					       MAX(AVG_RATING) AS maximumRating
					FROM GAMES
			""";

	protected List<GameRatingStatsView> gameRatingStatsViewList = new ArrayList<>();

	public List<GameRatingStatsView> getGameRatingStatsViewList() {
		return gameRatingStatsViewList;
	}

	public GameRatingStatsViewBuilderSQL build() {
		return this.select();
	}

	protected GameRatingStatsViewBuilderSQL select() {
		EntityManager em = dataSourceConnector.getEntityManager();
		Query viewQuery = em.createNativeQuery(SQL_GAME_RATING_STATS_SELECT, "GameRatingStatsViewMapping");
		this.gameRatingStatsViewList = viewQuery.getResultList();

		return this;
	}

	protected JPADataSourceConnector dataSourceConnector;

	public GameRatingStatsViewBuilderSQL(JPADataSourceConnector dataSourceConnector) {
		this.dataSourceConnector = dataSourceConnector;
	}
}
