package org.web.analytics.dto;

public record ComplexityByThemeDto(String theme, Double avgMinPlayers, Double avgMaxPlayers, Long boardgameCount) {
}
