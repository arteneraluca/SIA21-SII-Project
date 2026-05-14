package org.j4di.analytical.views;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface THEMES_CSV_THEME_COUNTS_VIEW_Repository extends JpaRepository<THEMES_CSV_THEME_COUNTS_VIEW, String> {

	@Query("SELECT o FROM THEMES_CSV_THEME_COUNTS_VIEW o")
	List<THEMES_CSV_THEME_COUNTS_VIEW> get_THEMES_CSV_THEME_COUNTS_VIEW();
}
