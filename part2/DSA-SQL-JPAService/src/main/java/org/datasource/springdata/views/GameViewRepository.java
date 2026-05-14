package org.datasource.springdata.views;

import org.datasource.jpa.views.games.GameView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GameViewRepository extends JpaRepository<GameView, Long> {
	@Query("SELECT g FROM GameView g WHERE g.minPlayers <= 2 AND g.maxPlayers >= 2 ORDER BY g.avgRating DESC")
	List<GameView> getTwoPlayerGamesViewList();

	@Query(nativeQuery = true,
			value = """
					SELECT BGG_ID AS bggId,
					       NAME AS name,
					       YEAR_PUBLISHED AS yearPublished,
					       MIN_PLAYERS AS minPlayers,
					       MAX_PLAYERS AS maxPlayers,
					       AVG_RATING AS avgRating
					FROM TOP_GAMES_V
					ORDER BY avgRating DESC
			""")
	List<TopGameView> getTopGamesViewList();
}
