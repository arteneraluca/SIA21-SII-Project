CREATE DATABASE IF NOT EXISTS sia21_p2;
USE sia21_p2;

CREATE OR REPLACE VIEW vw_boardgames AS
WITH raw AS (
    SELECT java_method(
        'org.spark.service.rest.QueryRESTDataService',
        'getRESTDataDocument',
        'http://jdbc-service:8090/api/oracle/boardgames'
    ) AS data
),
json_view AS (
    SELECT from_json(
        data,
        'ARRAY<STRUCT<boardgameId:BIGINT,name:STRING,yearPublished:INT,minPlayers:INT,maxPlayers:INT,avgRating:DOUBLE>>'
    ) AS array
    FROM raw
)
SELECT
    v.boardgameId AS boardgame_id,
    v.name,
    v.yearPublished AS year_published,
    v.minPlayers AS min_players,
    v.maxPlayers AS max_players,
    v.avgRating AS avg_rating
FROM json_view
LATERAL VIEW explode(array) exploded AS v;
