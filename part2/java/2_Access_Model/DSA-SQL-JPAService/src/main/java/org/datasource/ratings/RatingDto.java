package org.datasource.ratings;

import java.math.BigDecimal;

public record RatingDto(Long id, Integer boardgameId, BigDecimal rating, String username) {
    static RatingDto from(Rating rating) {
        return new RatingDto(rating.getId(), rating.getBoardgameId(), rating.getRating(), rating.getUsername());
    }
}
