-- =========================================================
-- 1. ROLLUP: Analiza pe An de publicare si Numar de jucatori
-- =========================================================
-- Grupeaza jocurile dupa anul publicarii si categoria de numar de jucatori.
-- =========================================================
SELECT
    CASE
        WHEN GROUPING(year_published) = 1 THEN '--- TOTAL GENERAL ---'
        ELSE TO_CHAR(year_published)
        END AS year_published,
    NVL(max_players_group, '--- TOTAL PE AN ---') AS max_players_group,
    COUNT(DISTINCT bgg_id) AS total_games,
    ROUND(AVG(rating), 2) AS avg_user_rating
FROM (
         SELECT
             bgg_id,
             year_published,
             rating,
             CASE
                 WHEN max_players <= 2 THEN 'SMALL'
                 WHEN max_players <= 4 THEN 'MEDIUM'
                 ELSE 'LARGE'
                 END AS max_players_group
         FROM SYSTEM.BOARDGAMES_FULL_V
         WHERE year_published IS NOT NULL
           AND rating IS NOT NULL
     )
GROUP BY ROLLUP(year_published, max_players_group)
ORDER BY year_published, max_players_group;

-- =========================================================
-- 2. CUBE: Analiza pe Tema si An de publicare
-- =========================================================
-- Afiseaza distributia jocurilor dupa doua dimensiuni:
-- 1) tema jocului
-- 2) anul publicarii
-- CUBE genereaza toate combinarile posibile:
-- - pe fiecare tema si an
-- - subtotaluri pe tema
-- - subtotaluri pe an
-- - totalul general
-- =========================================================
SELECT
    NVL(theme, '--- TOTAL TOATE TEMELE ---') AS theme,
    NVL(TO_CHAR(year_published), '--- TOTAL PE TEMA ---') AS year_published,
    COUNT(DISTINCT bgg_id) AS total_games,
    ROUND(AVG(rating), 2) AS avg_user_rating,
    MIN(rating) AS min_user_rating,
    MAX(rating) AS max_user_rating
FROM SYSTEM.BOARDGAMES_FULL_V
WHERE theme IS NOT NULL
  AND year_published IS NOT NULL
  AND rating IS NOT NULL
GROUP BY CUBE(theme, year_published)
HAVING COUNT(*) > 0
ORDER BY theme, year_published;

-- =========================================================
-- 3. GROUPING SETS: Comparatie pe Tema, An si Categoria de Jucatori
-- =========================================================
-- Analizeaza jocurile din trei perspective diferite:
-- 1) tema jocului
-- 2) anul publicarii
-- 3) categoria de jucatori in functie de numarul maxim de jucatori
-- =========================================================
SELECT
    CASE
        WHEN GROUPING(theme) = 0 THEN theme
        WHEN GROUPING(year_published) = 0 THEN TO_CHAR(year_published)
        WHEN GROUPING(player_category) = 0 THEN player_category
        ELSE 'TOTAL GENERAL'
        END AS entity_name,
    CASE
        WHEN GROUPING(theme) = 0 THEN 'THEME'
        WHEN GROUPING(year_published) = 0 THEN 'PUBLICATION_YEAR'
        WHEN GROUPING(player_category) = 0 THEN 'PLAYER_CATEGORY'
        ELSE 'TOTAL'
        END AS entity_type,
    COUNT(DISTINCT bgg_id) AS total_games,
    ROUND(AVG(rating), 2) AS avg_user_rating
FROM (
         SELECT
             bgg_id,
             theme,
             year_published,
             rating,
             CASE
                 WHEN max_players <= 2 THEN 'SMALL_GROUP'
                 WHEN max_players <= 4 THEN 'MEDIUM_GROUP'
                 ELSE 'LARGE_GROUP'
                 END AS player_category
         FROM SYSTEM.BOARDGAMES_FULL_V
         WHERE rating IS NOT NULL
           AND theme IS NOT NULL
     )
GROUP BY GROUPING SETS (
    (theme),
    (year_published),
    (player_category),
    ()
    )
ORDER BY entity_type, entity_name;