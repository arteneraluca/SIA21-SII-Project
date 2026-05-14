package org.datasource.jdbc.views.userratings;

import lombok.Value;

import java.math.BigDecimal;

@Value
public class UserAverageRatingView {
	private String username;
	private Long ratingCount;
	private BigDecimal averageRating;
}
