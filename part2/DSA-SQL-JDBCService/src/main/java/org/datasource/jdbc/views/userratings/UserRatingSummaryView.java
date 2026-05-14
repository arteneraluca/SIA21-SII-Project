package org.datasource.jdbc.views.userratings;

import lombok.Value;

import java.math.BigDecimal;

@Value
public class UserRatingSummaryView {
	private Integer bggId;
	private Long ratingCount;
	private BigDecimal averageRating;
	private BigDecimal minimumRating;
	private BigDecimal maximumRating;
}
