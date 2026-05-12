package org.web.analytics.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.web.analytics.dto.AverageRatingByThemeDto;
import org.web.analytics.dto.BoardgameByThemeDto;
import org.web.analytics.dto.BoardgameRatingSummaryDto;
import org.web.analytics.dto.ComplexityByThemeDto;
import org.web.analytics.dto.TopRatedBoardgameDto;
import org.web.analytics.dto.YearlyTrendDto;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class SparkAnalyticsRepository {
    private final JdbcTemplate jdbcTemplate;

    public SparkAnalyticsRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<BoardgameRatingSummaryDto> getBoardgameRatingSummary(int limit) {
        return jdbcTemplate.query("""
                SELECT boardgame_id, name, year_published, min_players, max_players,
                       source_avg_rating, rating_count, avg_user_rating, min_user_rating, max_user_rating
                FROM vw_boardgame_rating_summary
                ORDER BY boardgame_id
                LIMIT ?
                """, (rs, rowNum) -> new BoardgameRatingSummaryDto(
                longValue(rs, "boardgame_id"),
                rs.getString("name"),
                intValue(rs, "year_published"),
                intValue(rs, "min_players"),
                intValue(rs, "max_players"),
                doubleValue(rs, "source_avg_rating"),
                longValue(rs, "rating_count"),
                doubleValue(rs, "avg_user_rating"),
                doubleValue(rs, "min_user_rating"),
                doubleValue(rs, "max_user_rating")
        ), limit);
    }

    public List<TopRatedBoardgameDto> getTopRatedBoardgames(int limit) {
        return jdbcTemplate.query("""
                SELECT boardgame_id, name, year_published, rating_count, avg_user_rating, source_avg_rating
                FROM vw_top_rated_boardgames
                LIMIT ?
                """, (rs, rowNum) -> new TopRatedBoardgameDto(
                longValue(rs, "boardgame_id"),
                rs.getString("name"),
                intValue(rs, "year_published"),
                longValue(rs, "rating_count"),
                doubleValue(rs, "avg_user_rating"),
                doubleValue(rs, "source_avg_rating")
        ), limit);
    }

    public List<BoardgameByThemeDto> getBoardgamesByTheme() {
        return jdbcTemplate.query("""
                SELECT theme, boardgame_count
                FROM vw_boardgames_by_theme
                ORDER BY boardgame_count DESC, theme
                """, (rs, rowNum) -> new BoardgameByThemeDto(
                rs.getString("theme"),
                longValue(rs, "boardgame_count")
        ));
    }

    public List<AverageRatingByThemeDto> getAverageRatingByTheme() {
        return jdbcTemplate.query("""
                SELECT theme, boardgame_count, rating_count, avg_user_rating
                FROM vw_average_rating_by_theme
                ORDER BY avg_user_rating DESC NULLS LAST, theme
                """, (rs, rowNum) -> new AverageRatingByThemeDto(
                rs.getString("theme"),
                longValue(rs, "boardgame_count"),
                longValue(rs, "rating_count"),
                doubleValue(rs, "avg_user_rating")
        ));
    }

    public List<ComplexityByThemeDto> getComplexityByTheme() {
        return jdbcTemplate.query("""
                SELECT theme, avg_min_players, avg_max_players, boardgame_count
                FROM vw_complexity_by_theme
                ORDER BY avg_max_players DESC, theme
                """, (rs, rowNum) -> new ComplexityByThemeDto(
                rs.getString("theme"),
                doubleValue(rs, "avg_min_players"),
                doubleValue(rs, "avg_max_players"),
                longValue(rs, "boardgame_count")
        ));
    }

    public List<YearlyTrendDto> getYearlyTrends() {
        return jdbcTemplate.query("""
                SELECT year_published, boardgame_count, avg_source_rating, avg_user_rating
                FROM vw_yearly_boardgame_trends
                ORDER BY year_published
                """, (rs, rowNum) -> new YearlyTrendDto(
                intValue(rs, "year_published"),
                longValue(rs, "boardgame_count"),
                doubleValue(rs, "avg_source_rating"),
                doubleValue(rs, "avg_user_rating")
        ));
    }

    private Long longValue(ResultSet rs, String column) throws SQLException {
        long value = rs.getLong(column);
        return rs.wasNull() ? null : value;
    }

    private Integer intValue(ResultSet rs, String column) throws SQLException {
        int value = rs.getInt(column);
        return rs.wasNull() ? null : value;
    }

    private Double doubleValue(ResultSet rs, String column) throws SQLException {
        double value = rs.getDouble(column);
        return rs.wasNull() ? null : value;
    }
}
