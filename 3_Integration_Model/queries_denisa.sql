
-- =========================================================
-- 1. ROLLUP: Analiza ierarhica pe Tema si Popularitate
-- =========================================================
-- Grupeaza jocurile dupa Tema si o categorie de popularitate
-- folosind ROLLUP pentru a genera subtotaluri pe fiecare tema
-- si total general
-- =========================================================
SELECT
    NVL(theme, '--- TOTAL TOATE TEMELE ---') AS theme,
    NVL(popularity_category, '--- TOTAL PE TEMA ---') AS popularity_category,
    COUNT(*) AS number_of_games,
    ROUND(AVG(avg_user_rating), 2) AS avg_rating_score
FROM (
         SELECT
             bgg_id,
             theme,
             COUNT(rating) AS total_user_ratings,
             AVG(rating) AS avg_user_rating,
             CASE
                 WHEN COUNT(rating) >= 20 THEN 'VERY POPULAR'
                 WHEN COUNT(rating) >= 10 THEN 'POPULAR'
                 ELSE 'NICHE'
                 END AS popularity_category
         FROM GAMES.BOARDGAMES_FULL_V
         WHERE rating IS NOT NULL
           AND theme IS NOT NULL
         GROUP BY
             bgg_id,
             theme
     )
GROUP BY ROLLUP(theme, popularity_category)
ORDER BY theme, popularity_category;

-- =========================================================
-- 2. CUBE: Matrice OLAP Tema - Numar de Jucatori
-- =========================================================
-- Analizeaza simultan doua dimensiuni:
-- 1) tema jocului
-- 2) categoria de jucatori in functie de numarul maxim de jucatori
-- =========================================================
SELECT
    NVL(theme, '--- TOTAL TOATE TEMELE ---') AS theme,
    NVL(player_category, '--- TOTAL TOATE CATEGORIILE ---') AS player_category,
    COUNT(DISTINCT bgg_id) AS total_games,
    ROUND(AVG(rating), 2) AS avg_user_rating,
    MIN(rating) AS min_user_rating,
    MAX(rating) AS max_user_rating
FROM (
         SELECT
             bgg_id,
             theme,
             rating,
             CASE
                 WHEN max_players <= 2 THEN 'SMALL_GROUP'
                 WHEN max_players <= 4 THEN 'MEDIUM_GROUP'
                 ELSE 'LARGE_GROUP'
                 END AS player_category
         FROM SYSTEM.BOARDGAMES_FULL_V
         WHERE theme IS NOT NULL
           AND rating IS NOT NULL
     )
GROUP BY CUBE(theme, player_category)
HAVING COUNT(*) > 0
ORDER BY theme, player_category;

-- =========================================================
-- 3. GROUPING SETS: Comparatie pe Tema, Jucatori Minimi si Jucatori Maximi
-- =========================================================
-- Compara jocurile dupa:
-- 1) tema jocului
-- 2) numarul minim de jucatori
-- 3) numarul maxim de jucatori
-- =========================================================
SELECT
    CASE
        WHEN GROUPING(theme) = 0 THEN theme
        WHEN GROUPING(min_players_group) = 0 THEN min_players_group
        WHEN GROUPING(max_players_group) = 0 THEN max_players_group
        ELSE 'TOTAL GENERAL'
        END AS dimension_value,
    CASE
        WHEN GROUPING(theme) = 0 THEN 'THEME'
        WHEN GROUPING(min_players_group) = 0 THEN 'MIN_PLAYERS_GROUP'
        WHEN GROUPING(max_players_group) = 0 THEN 'MAX_PLAYERS_GROUP'
        ELSE 'TOTAL'
        END AS dimension_type,
    COUNT(DISTINCT bgg_id) AS total_games,
    ROUND(AVG(rating), 2) AS avg_user_rating
FROM (
         SELECT
             bgg_id,
             theme,
             rating,
             CASE
                 WHEN min_players = 1 THEN 'SOLO'
                 WHEN min_players = 2 THEN 'PAIR'
                 ELSE 'GROUP'
                 END AS min_players_group,
             CASE
                 WHEN max_players <= 2 THEN 'SMALL'
                 WHEN max_players BETWEEN 3 AND 4 THEN 'MEDIUM'
                 ELSE 'LARGE'
                 END AS max_players_group
         FROM SYSTEM.BOARDGAMES_FULL_V
         WHERE rating IS NOT NULL
           AND theme IS NOT NULL
     )
GROUP BY GROUPING SETS (
    (theme),
    (min_players_group),
    (max_players_group),
    ()
    )
ORDER BY dimension_type, dimension_value;