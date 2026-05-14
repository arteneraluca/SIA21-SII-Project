package org.j4di.access.views.themes;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface THEMES_CSV_VIEW_Repository extends JpaRepository<THEMES_CSV_VIEW, Long> {

	@Query("SELECT o FROM THEMES_CSV_VIEW o")
	List<THEMES_CSV_VIEW> get_THEMES_CSV_VIEW();

	@Query("SELECT o FROM THEMES_CSV_VIEW o WHERE o.bggId=:bggId")
	List<THEMES_CSV_VIEW> get_THEMES_CSV_VIEW_ByBggId(Long bggId);
}
