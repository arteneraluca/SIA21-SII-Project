package org.datasource.doc.views.games;

public record GameView(
        Integer bggId,
        String name,
        Integer yearPublished,
        Integer minPlayers,
        Integer maxPlayers,
        Double avgRating
) {
}
