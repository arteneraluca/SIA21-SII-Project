--------------------------------------------------------------------------------
--- SparkSQL Integration Views
--- Sources:
---   DSA-DOC-CSVService  -> themes.csv over REST
---   DSA-SQL-JDBCService -> PostgreSQL user_ratings table over Spark JDBC
---   DSA-SQL-JPAService  -> Oracle GAMES table over Spark JDBC
--------------------------------------------------------------------------------

CREATE DATABASE IF NOT EXISTS sia21;
USE sia21;

--------------------------------------------------------------------------------
--- DSA-DOC-CSVService: themes.csv
--- Endpoint: http://localhost:8097/DSA-DOC-CSVService/rest/themes
--- CSV columns:
--- BGGId, Adventure, Fantasy, Environmental, Economic, Transportation,
--- Science_Fiction, Space_Exploration, Civilization, Horror, Medieval, Ancient,
--- Pirates, Zombies, Sports, Music, Political, Math, City_Building
--------------------------------------------------------------------------------

SELECT java_method(
    'org.spark.service.rest.QueryRESTDataService',
    'getRESTDataDocument',
    'http://localhost:8097/DSA-DOC-CSVService/rest/themes');

-- DROP VIEW THEMES_CSV_JSON_VIEW;
CREATE OR REPLACE VIEW THEMES_CSV_JSON_VIEW AS
WITH json_view AS (
    SELECT from_json(json_raw.data,
        'ARRAY<STRUCT<bggId: INT, adventure: INT, fantasy: INT, environmental: INT,
        economic: INT, transportation: INT, scienceFiction: INT, spaceExploration: INT,
        civilization: INT, horror: INT, medieval: INT, ancient: INT, pirates: INT,
        zombies: INT, sports: INT, music: INT, political: INT, math: INT,
        cityBuilding: INT>>') array
    FROM (SELECT java_method('org.spark.service.rest.QueryRESTDataService',
        'getRESTDataDocument',
        'http://localhost:8097/DSA-DOC-CSVService/rest/themes') AS data) json_raw
)
SELECT v.*
FROM json_view LATERAL VIEW explode(json_view.array) AS v;

-- DROP VIEW THEMES_CSV_VIEW;
CREATE OR REPLACE VIEW THEMES_CSV_VIEW AS
SELECT bggId AS BGGId,
       adventure AS Adventure,
       fantasy AS Fantasy,
       environmental AS Environmental,
       economic AS Economic,
       transportation AS Transportation,
       scienceFiction AS Science_Fiction,
       spaceExploration AS Space_Exploration,
       civilization AS Civilization,
       horror AS Horror,
       medieval AS Medieval,
       ancient AS Ancient,
       pirates AS Pirates,
       zombies AS Zombies,
       sports AS Sports,
       music AS Music,
       political AS Political,
       math AS Math,
       cityBuilding AS City_Building
  FROM THEMES_CSV_JSON_VIEW;

ALTER VIEW THEMES_CSV_VIEW SET TBLPROPERTIES('AUTOREST' = 'themes');

-- DROP VIEW THEMES_CSV_THEME_COUNTS_VIEW;
CREATE OR REPLACE VIEW THEMES_CSV_THEME_COUNTS_VIEW AS
SELECT themeName,
       COUNT(*) AS gamesCount
  FROM THEMES_CSV_VIEW
  LATERAL VIEW stack(18,
    'Adventure', Adventure,
    'Fantasy', Fantasy,
    'Environmental', Environmental,
    'Economic', Economic,
    'Transportation', Transportation,
    'Science_Fiction', Science_Fiction,
    'Space_Exploration', Space_Exploration,
    'Civilization', Civilization,
    'Horror', Horror,
    'Medieval', Medieval,
    'Ancient', Ancient,
    'Pirates', Pirates,
    'Zombies', Zombies,
    'Sports', Sports,
    'Music', Music,
    'Political', Political,
    'Math', Math,
    'City_Building', City_Building
  ) themes AS themeName, isSelected
 WHERE isSelected = 1
 GROUP BY themeName;

--------------------------------------------------------------------------------
--- DSA-SQL-JDBCService: PostgreSQL user_ratings
--- Container/local JDBC URL: jdbc:postgresql://localhost:55432/sia21
--- Docker-network JDBC URL: jdbc:postgresql://postgres:5432/sia21
--- User/password: sia/sia
--- Table: user_ratings(bgg_id INTEGER, rating NUMERIC(3,1), username VARCHAR(100))
--------------------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS USER_RATINGS_JDBC_TABLE
USING jdbc
OPTIONS (
    url 'jdbc:postgresql://localhost:55432/sia21',
    dbtable 'user_ratings',
    user 'sia',
    password 'sia',
    driver 'org.postgresql.Driver'
);

-- DROP VIEW USER_RATINGS_VIEW;
CREATE OR REPLACE VIEW USER_RATINGS_VIEW AS
SELECT bgg_id,
       rating,
       username
  FROM USER_RATINGS_JDBC_TABLE;

ALTER VIEW USER_RATINGS_VIEW SET TBLPROPERTIES('AUTOREST' = 'ratings');

-- DROP VIEW RATINGS_BY_BGG_ID_VIEW;
CREATE OR REPLACE VIEW RATINGS_BY_BGG_ID_VIEW AS
SELECT bgg_id,
       COUNT(*) AS rating_count,
       ROUND(AVG(rating), 2) AS average_rating,
       MIN(rating) AS minimum_rating,
       MAX(rating) AS maximum_rating
  FROM USER_RATINGS_VIEW
 GROUP BY bgg_id;

-- DROP VIEW AVERAGE_RATING_BY_USER_VIEW;
CREATE OR REPLACE VIEW AVERAGE_RATING_BY_USER_VIEW AS
SELECT username,
       COUNT(*) AS rating_count,
       ROUND(AVG(rating), 2) AS average_rating
  FROM USER_RATINGS_VIEW
 GROUP BY username;

--------------------------------------------------------------------------------
--- DSA-SQL-JPAService: Oracle GAMES
--- Container/local JDBC URL: jdbc:oracle:thin:@//localhost:1521/FREEPDB1
--- User/password: games/games
--- Table: GAMES(BGG_ID, NAME, YEAR_PUBLISHED, MIN_PLAYERS, MAX_PLAYERS, AVG_RATING)
--------------------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS GAMES_ORACLE_TABLE
USING jdbc
OPTIONS (
    url 'jdbc:oracle:thin:@//localhost:1521/FREEPDB1',
    dbtable 'GAMES',
    user 'games',
    password 'games',
    driver 'oracle.jdbc.OracleDriver'
);

-- DROP VIEW GAMES_VIEW;
CREATE OR REPLACE VIEW GAMES_VIEW AS
SELECT BGG_ID AS bggId,
       NAME AS name,
       YEAR_PUBLISHED AS yearPublished,
       MIN_PLAYERS AS minPlayers,
       MAX_PLAYERS AS maxPlayers,
       AVG_RATING AS avgRating
  FROM GAMES_ORACLE_TABLE;

ALTER VIEW GAMES_VIEW SET TBLPROPERTIES('AUTOREST' = 'games');

-- DROP VIEW TOP_GAMES_VIEW;
CREATE OR REPLACE VIEW TOP_GAMES_VIEW AS
SELECT bggId,
       name,
       yearPublished,
       minPlayers,
       maxPlayers,
       avgRating
  FROM GAMES_VIEW
 WHERE avgRating >= 6;

ALTER VIEW TOP_GAMES_VIEW SET TBLPROPERTIES('AUTOREST' = 'games/top');

--------------------------------------------------------------------------------
--- Cross-source demo views
--------------------------------------------------------------------------------

-- DROP VIEW TOP_RATED_GAMES_VIEW;
CREATE OR REPLACE VIEW TOP_RATED_GAMES_VIEW AS
SELECT g.bggId,
       g.name,
       g.yearPublished,
       g.minPlayers,
       g.maxPlayers,
       g.avgRating,
       r.rating_count,
       r.average_rating AS users_average_rating
  FROM GAMES_VIEW g
  LEFT JOIN RATINGS_BY_BGG_ID_VIEW r ON g.bggId = r.bgg_id;

-- DROP VIEW RATINGS_GAMES_VIEW;
CREATE OR REPLACE VIEW RATINGS_GAMES_VIEW AS
SELECT r.bgg_id,
       g.name,
       r.rating,
       r.username,
       g.yearPublished,
       g.avgRating
  FROM USER_RATINGS_VIEW r
  INNER JOIN GAMES_VIEW g ON r.bgg_id = g.bggId;

--------------------------------------------------------------------------------
--- Presentation queries
--------------------------------------------------------------------------------

SELECT COUNT(*) AS themes_count FROM THEMES_CSV_VIEW;

SELECT COUNT(*) AS ratings_count FROM USER_RATINGS_VIEW;

SELECT username,
       rating_count,
       average_rating
  FROM AVERAGE_RATING_BY_USER_VIEW
 ORDER BY rating_count DESC, average_rating DESC, username
 LIMIT 20;

SELECT bggId,
       name,
       avgRating,
       rating_count,
       users_average_rating
  FROM TOP_RATED_GAMES_VIEW
 ORDER BY avgRating DESC, rating_count DESC, bggId
 LIMIT 20;

SELECT themeName,
       gamesCount
  FROM THEMES_CSV_THEME_COUNTS_VIEW
 ORDER BY gamesCount DESC, themeName;

SELECT bgg_id,
       name,
       COUNT(*) AS ratings_count,
       ROUND(AVG(rating), 2) AS average_user_rating
  FROM RATINGS_GAMES_VIEW
 GROUP BY bgg_id, name
 ORDER BY average_user_rating DESC, ratings_count DESC, bgg_id
 LIMIT 20;
