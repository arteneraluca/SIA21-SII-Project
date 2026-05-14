package org.j4di.integration.views;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import org.hibernate.annotations.Immutable;

import java.math.BigDecimal;

@Getter
@Entity
@Immutable
@Table(name = "TOP_RATED_GAMES_VIEW")
public class TOP_RATED_GAMES_VIEW {
	@Id
	private Long bggId;
	private String name;
	private Integer yearPublished;
	private Integer minPlayers;
	private Integer maxPlayers;
	private BigDecimal avgRating;
	@Column(name = "rating_count")
	private Long ratingCount;
	@Column(name = "users_average_rating")
	private BigDecimal usersAverageRating;
}
