/uCREATE DATABASE IF NOT EXISTS sia21_p2;
USE sia21_p2;

CREATE OR REPLACE VIEW vw_boardgame_rating_summary AS
SELECT
    b.boardgame_id,
    b.name,
    b.year_published,
    b.min_players,
    b.max_players,
    b.avg_rating AS source_avg_rating,
    COUNT(r.id) AS rating_count,
    ROUND(AVG(r.rating), 2) AS avg_user_rating,
    MIN(r.rating) AS min_user_rating,
    MAX(r.rating) AS max_user_rating
FROM vw_boardgames b
LEFT JOIN vw_ratings r ON b.boardgame_id = r.boardgame_id
GROUP BY b.boardgame_id, b.name, b.year_published, b.min_players, b.max_players, b.avg_rating;

CREATE OR REPLACE VIEW vw_top_rated_boardgames AS
SELECT
    boardgame_id,
    name,
    year_published,
    rating_count,
    avg_user_rating,
    source_avg_rating
FROM vw_boardgame_rating_summary
WHERE rating_count > 0
ORDER BY avg_user_rating DESC, rating_count DESC;

CREATE OR REPLACE VIEW vw_boardgames_by_theme AS
SELECT
    t.theme,
    COUNT(DISTINCT b.boardgame_id) AS boardgame_count
FROM vw_themes t
JOIN vw_boardgames b ON t.boardgame_id = b.boardgame_id
GROUP BY t.theme;

CREATE OR REPLACE VIEW vw_average_rating_by_theme AS
SELECT
    t.theme,
    COUNT(DISTINCT b.boardgame_id) AS boardgame_count,
    COUNT(r.id) AS rating_count,
    ROUND(AVG(r.rating), 2) AS avg_user_rating
FROM vw_themes t
JOIN vw_boardgames b ON t.boardgame_id = b.boardgame_id
LEFT JOIN vw_ratings r ON b.boardgame_id = r.boardgame_id
GROUP BY t.theme;

CREATE OR REPLACE VIEW vw_complexity_by_theme AS
SELECT
    t.theme,
    ROUND(AVG(b.min_players), 2) AS avg_min_players,
    ROUND(AVG(b.max_players), 2) AS avg_max_players,
    COUNT(DISTINCT b.boardgame_id) AS boardgame_count
FROM vw_themes t
JOIN vw_boardgames b ON t.boardgame_id = b.boardgame_id
GROUP BY t.theme;

CREATE OR REPLACE VIEW vw_yearly_boardgame_trends AS
SELECT
    b.year_published,
    COUNT(DISTINCT b.boardgame_id) AS boardgame_count,
    ROUND(AVG(b.avg_rating), 2) AS avg_source_rating,
    ROUND(AVG(r.rating), 2) AS avg_user_rating
FROM vw_boardgames b
LEFT JOIN vw_ratings r ON b.boardgame_id = r.boardgame_id
WHERE b.year_published IS NOT NULL
GROUP BY b.year_published;
