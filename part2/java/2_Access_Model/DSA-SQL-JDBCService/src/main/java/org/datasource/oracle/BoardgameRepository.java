package org.datasource.oracle;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class BoardgameRepository {
    private final JdbcTemplate jdbcTemplate;

    public BoardgameRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<BoardgameDto> findAll() {
        return jdbcTemplate.query("""
                SELECT BOARDGAME_ID, NAME, YEAR_PUBLISHED, MIN_PLAYERS, MAX_PLAYERS, AVG_RATING
                FROM SIA_ORACLE.BOARDGAMES
                ORDER BY BOARDGAME_ID
                """, this::mapRow);
    }

    public Optional<BoardgameDto> findById(Long id) {
        return jdbcTemplate.query("""
                SELECT BOARDGAME_ID, NAME, YEAR_PUBLISHED, MIN_PLAYERS, MAX_PLAYERS, AVG_RATING
                FROM SIA_ORACLE.BOARDGAMES
                WHERE BOARDGAME_ID = ?
                """, this::mapRow, id).stream().findFirst();
    }

    private BoardgameDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new BoardgameDto(
                rs.getLong("BOARDGAME_ID"),
                rs.getString("NAME"),
                intValue(rs, "YEAR_PUBLISHED"),
                intValue(rs, "MIN_PLAYERS"),
                intValue(rs, "MAX_PLAYERS"),
                doubleValue(rs, "AVG_RATING")
        );
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
