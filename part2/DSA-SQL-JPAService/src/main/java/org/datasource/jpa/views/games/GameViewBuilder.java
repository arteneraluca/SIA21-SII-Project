package org.datasource.jpa.views.games;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.datasource.jpa.JPADataSourceConnector;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GameViewBuilder {
	protected String JPQL_GAMES_SELECT =
			"SELECT NEW org.datasource.jpa.views.games.GameView("
					+ "g.bggId, g.name, g.yearPublished, g.minPlayers, g.maxPlayers, g.avgRating) "
					+ "FROM GameView g "
					+ "ORDER BY g.avgRating DESC";

	protected List<GameView> gameViewList = new ArrayList<>();

	public List<GameView> getGameViewList() {
		return gameViewList;
	}

	public GameViewBuilder build() {
		return this.select();
	}

	protected GameViewBuilder select() {
		EntityManager em = dataSourceConnector.getEntityManager();
		Query viewQuery = em.createQuery(JPQL_GAMES_SELECT);
		this.gameViewList = viewQuery.getResultList();

		return this;
	}

	protected JPADataSourceConnector dataSourceConnector;

	public GameViewBuilder(JPADataSourceConnector dataSourceConnector) {
		this.dataSourceConnector = dataSourceConnector;
	}
}
