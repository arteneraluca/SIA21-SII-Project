package org.web.analytics.dto;

public record AverageRatingByThemeDto(String theme, Long boardgameCount, Long ratingCount, Double avgUserRating) {
}
