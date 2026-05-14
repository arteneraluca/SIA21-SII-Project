package org.j4di.analytical.views;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RATINGS_GAMES_VIEW_Repository extends JpaRepository<RATINGS_GAMES_VIEW, Long> {

	@Query("SELECT o FROM RATINGS_GAMES_VIEW o")
	List<RATINGS_GAMES_VIEW> get_RATINGS_GAMES_VIEW();
}
