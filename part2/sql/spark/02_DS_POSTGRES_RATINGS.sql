CREATE DATABASE IF NOT EXISTS sia21_p2;
USE sia21_p2;

CREATE OR REPLACE VIEW vw_ratings AS
WITH raw AS (
    SELECT java_method(
        'org.spark.service.rest.QueryRESTDataService',
        'getRESTDataDocument',
        'http://jpa-service:8091/api/ratings'
    ) AS data
),
json_view AS (
    SELECT from_json(
        data,
        'ARRAY<STRUCT<id:BIGINT,boardgameId:INT,rating:DOUBLE,username:STRING>>'
    ) AS array
    FROM raw
)
SELECT
    v.id,
    v.boardgameId AS boardgame_id,
    v.rating,
    v.username
FROM json_view
LATERAL VIEW explode(array) exploded AS v;
