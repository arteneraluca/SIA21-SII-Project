package org.datasource.oracle;

public record BoardgameDto(
        Long boardgameId,
        String name,
        Integer yearPublished,
        Integer minPlayers,
        Integer maxPlayers,
        Double avgRating
) {
}
