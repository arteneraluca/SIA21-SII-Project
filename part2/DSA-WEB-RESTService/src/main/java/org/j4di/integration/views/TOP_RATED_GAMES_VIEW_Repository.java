package org.j4di.integration.views;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TOP_RATED_GAMES_VIEW_Repository extends JpaRepository<TOP_RATED_GAMES_VIEW, Long> {

	@Query("SELECT o FROM TOP_RATED_GAMES_VIEW o")
	List<TOP_RATED_GAMES_VIEW> get_TOP_RATED_GAMES_VIEW();
}
