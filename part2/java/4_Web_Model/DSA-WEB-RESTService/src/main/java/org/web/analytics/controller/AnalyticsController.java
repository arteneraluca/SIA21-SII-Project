package org.web.analytics.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.web.analytics.dto.AverageRatingByThemeDto;
import org.web.analytics.dto.BoardgameByThemeDto;
import org.web.analytics.dto.BoardgameRatingSummaryDto;
import org.web.analytics.dto.ComplexityByThemeDto;
import org.web.analytics.dto.TopRatedBoardgameDto;
import org.web.analytics.dto.YearlyTrendDto;
import org.web.analytics.service.AnalyticsService;

import java.util.List;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {
    private final AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping("/boardgame-rating-summary")
    public List<BoardgameRatingSummaryDto> getBoardgameRatingSummary(@RequestParam(required = false) Integer limit) {
        return analyticsService.getBoardgameRatingSummary(limit);
    }

    @GetMapping("/top-rated-boardgames")
    public List<TopRatedBoardgameDto> getTopRatedBoardgames(@RequestParam(required = false) Integer limit) {
        return analyticsService.getTopRatedBoardgames(limit);
    }

    @GetMapping("/boardgames-by-theme")
    public List<BoardgameByThemeDto> getBoardgamesByTheme() {
        return analyticsService.getBoardgamesByTheme();
    }

    @GetMapping("/average-rating-by-theme")
    public List<AverageRatingByThemeDto> getAverageRatingByTheme() {
        return analyticsService.getAverageRatingByTheme();
    }

    @GetMapping("/complexity-by-theme")
    public List<ComplexityByThemeDto> getComplexityByTheme() {
        return analyticsService.getComplexityByTheme();
    }

    @GetMapping("/yearly-trends")
    public List<YearlyTrendDto> getYearlyTrends() {
        return analyticsService.getYearlyTrends();
    }
}
