----------------------------------------------------------------------------------
--- DS2_ORACLE_SparkSQL_Views.sql
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
-- 1. Get Data Source JSON Schema
SELECT schema_of_json('[{"bggId":375,"name":"6 Billion","yearPublished":1999,"minPlayers":2,"maxPlayers":5,"avgRating":5.34493}]');

-- 2. Create Remote View
-- DROP VIEW games_view;
CREATE OR REPLACE VIEW games_view AS
WITH json_view AS (
    SELECT from_json(json_raw.data,
                     'ARRAY<STRUCT<bggId: BIGINT, name: STRING, yearPublished: INT, minPlayers: INT,
                      maxPlayers: INT, avgRating: DECIMAL(8,5)>>') array
    FROM (SELECT java_method('org.spark.service.rest.QueryRESTDataService', 'getRESTDataDocument',
        'http://localhost:8091/DSA_SQL_JPAService/rest/games/GamesView')
        as data) json_raw
)
select v.*
FROM json_view LATERAL VIEW explode(json_view.array) AS v;

-- 3. Test Remote View
select * FROM games_view;

----------------------------------------------------------------------------------
-- 1. Get Data Source JSON Schema
SELECT schema_of_json('[{"bggId":378,"name":"Lancashire Railways","yearPublished":1998,"minPlayers":3,"maxPlayers":6,"avgRating":6.94087}]');

-- 2. Create Remote View
-- DROP VIEW top_games_view;
CREATE OR REPLACE VIEW top_games_view AS
WITH json_view AS (
    SELECT from_json(json_raw.data,
                     'ARRAY<STRUCT<bggId: BIGINT, name: STRING, yearPublished: INT, minPlayers: INT,
                      maxPlayers: INT, avgRating: DECIMAL(8,5)>>') array
    FROM (SELECT java_method('org.spark.service.rest.QueryRESTDataService', 'getRESTDataDocument',
        'http://localhost:8091/DSA_SQL_JPAService/rest/games/TopGamesView')
        as data) json_raw
)
select v.*
FROM json_view LATERAL VIEW explode(json_view.array) AS v;

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
-- 1. Get Data Source JSON Schema
SELECT schema_of_json('[{"gameCount":107,"averageRating":5.8641,"minimumRating":4.83304,"maximumRating":7.10578}]');

-- 2. Create Remote View
-- DROP VIEW game_rating_stats_view;
CREATE OR REPLACE VIEW game_rating_stats_view AS
WITH json_view AS (
    SELECT from_json(json_raw.data,
                     'ARRAY<STRUCT<gameCount: BIGINT, averageRating: DECIMAL(8,4),
                      minimumRating: DECIMAL(8,5), maximumRating: DECIMAL(8,5)>>') array
    FROM (SELECT java_method('org.spark.service.rest.QueryRESTDataService', 'getRESTDataDocument',
        'http://localhost:8091/DSA_SQL_JPAService/rest/games/GameRatingStatsView')
        as data) json_raw
)
select v.*
FROM json_view LATERAL VIEW explode(json_view.array) AS v;

-- 3. Test Remote View
select * FROM game_rating_stats_view;
