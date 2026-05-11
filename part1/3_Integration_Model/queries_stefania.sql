-- =========================================================
-- 1. ROLLUP: Analiza pe An de publicare si Tema
-- =========================================================
-- Afiseaza numarul de jocuri si scorul mediu al evaluarilor
-- pe fiecare combinatie dintre anul publicarii si tema.
-- ROLLUP adauga subtotaluri pe fiecare an si totalul general.
-- =========================================================

SELECT
    CASE
        WHEN GROUPING(year_published) = 1 THEN '--- TOTAL GENERAL ---'
        ELSE TO_CHAR(year_published)
        END AS year_published,
    NVL(theme, '--- TOTAL PE AN ---') AS theme,
    COUNT(DISTINCT bgg_id) AS total_games,
    ROUND(AVG(rating), 2) AS avg_user_rating
FROM SYSTEM.BOARDGAMES_FULL_V
WHERE rating IS NOT NULL
  AND year_published IS NOT NULL
  AND theme IS NOT NULL
GROUP BY ROLLUP(year_published, theme)
ORDER BY year_published, theme;

-- =========================================================
-- 2. CUBE: Matrice pe Tema si Categoria de Accesibilitate
-- =========================================================
-- Analizeaza jocurile pe:
-- 1) tema
-- 2) categoria de accesibilitate, calculata dupa numarul minim de jucatori
-- =========================================================
SELECT
    NVL(theme, '--- TOTAL TOATE TEMELE ---') AS theme,
    NVL(accessibility_category, '--- TOTAL TOATE CATEGORIILE ---') AS accessibility_category,
    COUNT(DISTINCT bgg_id) AS total_games,
    ROUND(AVG(avg_rating), 2) AS avg_boardgame_rating
FROM (
         SELECT
             bgg_id,
             theme,
             avg_rating,
             CASE
                 WHEN min_players = 1 THEN 'SOLO_OR_FLEXIBLE'
                 WHEN min_players = 2 THEN 'PAIR_OR_SMALL_GROUP'
                 ELSE 'GROUP_ONLY'
                 END AS accessibility_category
         FROM SYSTEM.BOARDGAMES_FULL_V
         WHERE theme IS NOT NULL
           AND avg_rating IS NOT NULL
     )
GROUP BY CUBE(theme, accessibility_category)
HAVING COUNT(*) > 0
ORDER BY theme, accessibility_category;

-- =========================================================
-- 3. GROUPING SETS: Comparatie intre Tema, Numar maxim de jucatori si An
-- =========================================================
-- Compara mai multe perspective ale jocurilor:
-- 1) tema jocului
-- 2) grupa de numar maxim de jucatori
-- 3) anul publicarii
-- =========================================================

SELECT
    CASE
        WHEN GROUPING(theme) = 0 THEN theme
        WHEN GROUPING(max_players_group) = 0 THEN max_players_group
        WHEN GROUPING(year_published) = 0 THEN TO_CHAR(year_published)
        ELSE 'TOTAL GENERAL'
        END AS dimension_value,
    CASE
        WHEN GROUPING(theme) = 0 THEN 'THEME'
        WHEN GROUPING(max_players_group) = 0 THEN 'MAX_PLAYERS_GROUP'
        WHEN GROUPING(year_published) = 0 THEN 'YEAR_PUBLISHED'
        ELSE 'TOTAL'
        END AS dimension_type,
    COUNT(DISTINCT bgg_id) AS total_games,
    ROUND(AVG(rating), 2) AS avg_user_rating
FROM (
         SELECT
             bgg_id,
             theme,
             year_published,
             rating,
             CASE
                 WHEN max_players <= 2 THEN '2_OR_LESS'
                 WHEN max_players BETWEEN 3 AND 4 THEN '3_TO_4'
                 WHEN max_players BETWEEN 5 AND 6 THEN '5_TO_6'
                 ELSE '7_PLUS'
                 END AS max_players_group
         FROM SYSTEM.BOARDGAMES_FULL_V
         WHERE rating IS NOT NULL
     )
GROUP BY GROUPING SETS (
    (theme),
    (max_players_group),
    (year_published),
    ()
    )
ORDER BY dimension_type, dimension_value;

