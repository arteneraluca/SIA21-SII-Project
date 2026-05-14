package org.j4di.access.views.ratings;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface USER_RATINGS_VIEW_Repository extends JpaRepository<USER_RATINGS_VIEW, Long> {

	@Query("SELECT o FROM USER_RATINGS_VIEW o")
	List<USER_RATINGS_VIEW> get_USER_RATINGS_VIEW();

	@Query("SELECT o FROM USER_RATINGS_VIEW o WHERE o.bggId=:bggId")
	List<USER_RATINGS_VIEW> get_USER_RATINGS_VIEW_ByBggId(Long bggId);
}
