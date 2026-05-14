----------------------------------------------------------------------------------
--- DS2_ORACLE_SparkSQL_Views_from_REST.sql
----------------------------------------------------------------------------------
SELECT java_method(
               'org.spark.service.rest.QueryRESTDataService',
               'getRESTDataDocument',
               'http://localhost:8091/DSA_SQL_JPAService/rest/games/GamesView');

SELECT java_method(
               'org.spark.service.rest.QueryRESTDataService',
               'getRESTDataDocument',
               'http://localhost:8091/DSA_SQL_JPAService/rest/games/TopGamesView');

SELECT java_method(
               'org.spark.service.rest.QueryRESTDataService',
               'getRESTDataDocument',
               'http://localhost:8091/DSA_SQL_JPAService/rest/games/GameRatingStatsView');

----------------------------------------------------------------------------------
-- 1. CREATE JSON View
SELECT java_method(
               'org.spark.service.rest.RESTEnabledSQLService',
               'createJSONViewFromREST',
               'GAMES_JSON_VIEW',
               'http://localhost:8091/DSA_SQL_JPAService/rest/games/GamesView');

SELECT * FROM GAMES_JSON_VIEW;

-- 2. Create SQL View
-- DROP VIEW games_view;
CREATE OR REPLACE VIEW games_view AS
select v.*
FROM GAMES_JSON_VIEW as json_view LATERAL VIEW explode(json_view.array) AS v;

-- 3. Test Remote View
select * FROM games_view;

----------------------------------------------------------------------------------
-- 1. CREATE JSON View
SELECT java_method(
               'org.spark.service.rest.RESTEnabledSQLService',
               'createJSONViewFromREST',
               'TOP_GAMES_JSON_VIEW',
               'http://localhost:8091/DSA_SQL_JPAService/rest/games/TopGamesView');

SELECT * FROM TOP_GAMES_JSON_VIEW;

-- 2. Create SQL View
-- DROP VIEW top_games_view;
CREATE OR REPLACE VIEW top_games_view AS
select v.*
FROM TOP_GAMES_JSON_VIEW as json_view LATERAL VIEW explode(json_view.array) AS v;

-- 3. Presentation queries
select * FROM top_games_view;

select yearPublished, count(*) as gameCount, round(avg(avgRating), 4) as averageRating
from games_view
group by yearPublished
order by yearPublished;

select minPlayers, maxPlayers, count(*) as gameCount, round(avg(avgRating), 4) as averageRating
from games_view
group by minPlayers, maxPlayers
order by averageRating desc;

----------------------------------------------------------------------------------
-- 1. CREATE JSON View
SELECT java_method(
               'org.spark.service.rest.RESTEnabledSQLService',
               'createJSONViewFromREST',
               'GAME_RATING_STATS_JSON_VIEW',
               'http://localhost:8091/DSA_SQL_JPAService/rest/games/GameRatingStatsView');

SELECT * FROM GAME_RATING_STATS_JSON_VIEW;

-- 2. Create SQL View
-- DROP VIEW game_rating_stats_view;
CREATE OR REPLACE VIEW game_rating_stats_view AS
select v.*
FROM GAME_RATING_STATS_JSON_VIEW as json_view LATERAL VIEW explode(json_view.array) AS v;

-- 3. Test Remote View
select * FROM game_rating_stats_view;
