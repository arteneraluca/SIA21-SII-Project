package org.j4di.integration.views;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RATINGS_BY_BGG_ID_VIEW_Repository extends JpaRepository<RATINGS_BY_BGG_ID_VIEW, Long> {

	@Query("SELECT o FROM RATINGS_BY_BGG_ID_VIEW o")
	List<RATINGS_BY_BGG_ID_VIEW> get_RATINGS_BY_BGG_ID_VIEW();
}
