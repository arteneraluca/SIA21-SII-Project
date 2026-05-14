package org.datasource.springdata.views;

import java.math.BigDecimal;

public interface TopGameView {
	Long getBggId();

	String getName();

	Integer getYearPublished();

	Integer getMinPlayers();

	Integer getMaxPlayers();

	BigDecimal getAvgRating();
}
