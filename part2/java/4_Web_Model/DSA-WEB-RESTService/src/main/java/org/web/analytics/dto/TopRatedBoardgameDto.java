package org.web.analytics.dto;

public record TopRatedBoardgameDto(
        Long boardgameId,
        String name,
        Integer yearPublished,
        Long ratingCount,
        Double avgUserRating,
        Double sourceAvgRating
) {
}
