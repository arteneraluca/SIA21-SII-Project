package org.datasource.jdbc.views.userratings;

import lombok.Value;

import java.math.BigDecimal;

@Value
public class UserRatingView {
	private Integer bggId;
	private BigDecimal rating;
	private String username;
}
