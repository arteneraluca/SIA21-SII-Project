package org.datasource.jpa.views.games;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class GameRatingStatsView {
	private Long gameCount;
	private BigDecimal averageRating;
	private BigDecimal minimumRating;
	private BigDecimal maximumRating;
}
