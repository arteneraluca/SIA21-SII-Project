package org.web.analytics.service;

import org.springframework.stereotype.Service;
import org.web.analytics.config.AnalyticsProperties;
import org.web.analytics.dto.AverageRatingByThemeDto;
import org.web.analytics.dto.BoardgameByThemeDto;
import org.web.analytics.dto.BoardgameRatingSummaryDto;
import org.web.analytics.dto.ComplexityByThemeDto;
import org.web.analytics.dto.TopRatedBoardgameDto;
import org.web.analytics.dto.YearlyTrendDto;
import org.web.analytics.repository.SparkAnalyticsRepository;

import java.util.List;

@Service
public class AnalyticsService {
    private final SparkAnalyticsRepository sparkAnalyticsRepository;
    private final AnalyticsProperties analyticsProperties;

    public AnalyticsService(SparkAnalyticsRepository sparkAnalyticsRepository, AnalyticsProperties analyticsProperties) {
        this.sparkAnalyticsRepository = sparkAnalyticsRepository;
        this.analyticsProperties = analyticsProperties;
    }

    public List<BoardgameRatingSummaryDto> getBoardgameRatingSummary(Integer limit) {
        return sparkAnalyticsRepository.getBoardgameRatingSummary(normalizeLimit(limit));
    }

    public List<TopRatedBoardgameDto> getTopRatedBoardgames(Integer limit) {
        return sparkAnalyticsRepository.getTopRatedBoardgames(normalizeLimit(limit));
    }

    public List<BoardgameByThemeDto> getBoardgamesByTheme() {
        return sparkAnalyticsRepository.getBoardgamesByTheme();
    }

    public List<AverageRatingByThemeDto> getAverageRatingByTheme() {
        return sparkAnalyticsRepository.getAverageRatingByTheme();
    }

    public List<ComplexityByThemeDto> getComplexityByTheme() {
        return sparkAnalyticsRepository.getComplexityByTheme();
    }

    public List<YearlyTrendDto> getYearlyTrends() {
        return sparkAnalyticsRepository.getYearlyTrends();
    }

    private int normalizeLimit(Integer requestedLimit) {
        int defaultLimit = analyticsProperties.defaultLimit() == null ? 20 : analyticsProperties.defaultLimit();
        int limit = requestedLimit == null ? defaultLimit : requestedLimit;
        return Math.max(1, Math.min(limit, 100));
    }
}
