package org.web.analytics.dto;

public record BoardgameRatingSummaryDto(
        Long boardgameId,
        String name,
        Integer yearPublished,
        Integer minPlayers,
        Integer maxPlayers,
        Double sourceAvgRating,
        Long ratingCount,
        Double avgUserRating,
        Double minUserRating,
        Double maxUserRating
) {
}
