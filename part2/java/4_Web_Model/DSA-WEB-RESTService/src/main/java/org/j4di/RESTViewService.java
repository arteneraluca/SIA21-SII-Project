package org.j4di;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.web.analytics.dto.AverageRatingByThemeDto;
import org.web.analytics.dto.BoardgameByThemeDto;
import org.web.analytics.dto.BoardgameRatingSummaryDto;
import org.web.analytics.dto.ComplexityByThemeDto;
import org.web.analytics.dto.TopRatedBoardgameDto;
import org.web.analytics.dto.YearlyTrendDto;
import org.web.analytics.service.AnalyticsService;

import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/rest/OLAP")
public class RESTViewService {
    private static final Logger logger = Logger.getLogger(RESTViewService.class.getName());

    private final AnalyticsService analyticsService;

    public RESTViewService(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping(value = "/ping", produces = MediaType.TEXT_PLAIN_VALUE)
    public String pingDataSource() {
        logger.info(">>>> DSA-WEB-RESTService:: RESTViewService is Up!");
        return "Ping response from DSA-WEB-RESTService!";
    }

    @GetMapping(value = "/BOARDGAME_RATING_SUMMARY", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<BoardgameRatingSummaryDto> getBoardgameRatingSummary() {
        return analyticsService.getBoardgameRatingSummary(null);
    }

    @GetMapping(value = "/TOP_RATED_BOARDGAMES", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<TopRatedBoardgameDto> getTopRatedBoardgames() {
        return analyticsService.getTopRatedBoardgames(null);
    }

    @GetMapping(value = "/BOARDGAMES_BY_THEME", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<BoardgameByThemeDto> getBoardgamesByTheme() {
        return analyticsService.getBoardgamesByTheme();
    }

    @GetMapping(value = "/AVERAGE_RATING_BY_THEME", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<AverageRatingByThemeDto> getAverageRatingByTheme() {
        return analyticsService.getAverageRatingByTheme();
    }

    @GetMapping(value = "/COMPLEXITY_BY_THEME", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ComplexityByThemeDto> getComplexityByTheme() {
        return analyticsService.getComplexityByTheme();
    }

    @GetMapping(value = "/YEARLY_TRENDS", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<YearlyTrendDto> getYearlyTrends() {
        return analyticsService.getYearlyTrends();
    }
}
