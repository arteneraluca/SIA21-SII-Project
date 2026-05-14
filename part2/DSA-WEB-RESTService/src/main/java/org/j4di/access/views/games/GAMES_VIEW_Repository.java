package org.j4di.access.views.games;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GAMES_VIEW_Repository extends JpaRepository<GAMES_VIEW, Long> {

	@Query("SELECT o FROM GAMES_VIEW o")
	List<GAMES_VIEW> get_GAMES_VIEW();

	@Query("SELECT o FROM GAMES_VIEW o WHERE o.bggId=:bggId")
	List<GAMES_VIEW> get_GAMES_VIEW_ByBggId(Long bggId);
}
