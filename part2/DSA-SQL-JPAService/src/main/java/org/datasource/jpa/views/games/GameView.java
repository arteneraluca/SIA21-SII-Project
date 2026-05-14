package org.datasource.jpa.views.games;

import jakarta.persistence.Column;
import jakarta.persistence.ColumnResult;
import jakarta.persistence.ConstructorResult;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.SqlResultSetMapping;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.io.Serializable;
import java.math.BigDecimal;

@SqlResultSetMapping(
		name = "GameRatingStatsViewMapping",
		classes = {
				@ConstructorResult(
						columns = {
								@ColumnResult(name = "gameCount", type = Long.class),
								@ColumnResult(name = "averageRating", type = BigDecimal.class),
								@ColumnResult(name = "minimumRating", type = BigDecimal.class),
								@ColumnResult(name = "maximumRating", type = BigDecimal.class)
						},
						targetClass = GameRatingStatsView.class
				)
		}
)
@Value
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Entity
@Table(name = "GAMES")
@NamedQuery(name = "GameView.findAll", query = "SELECT g FROM GameView g ORDER BY g.avgRating DESC")
public class GameView implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "BGG_ID")
	private Long bggId;

	@Column(name = "NAME")
	private String name;

	@Column(name = "YEAR_PUBLISHED")
	private Integer yearPublished;

	@Column(name = "MIN_PLAYERS")
	private Integer minPlayers;

	@Column(name = "MAX_PLAYERS")
	private Integer maxPlayers;

	@Column(name = "AVG_RATING")
	private BigDecimal avgRating;
}
