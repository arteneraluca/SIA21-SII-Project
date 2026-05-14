package org.j4di.analytical.views;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AVERAGE_RATING_BY_USER_VIEW_Repository extends JpaRepository<AVERAGE_RATING_BY_USER_VIEW, String> {

	@Query("SELECT o FROM AVERAGE_RATING_BY_USER_VIEW o")
	List<AVERAGE_RATING_BY_USER_VIEW> get_AVERAGE_RATING_BY_USER_VIEW();
}
