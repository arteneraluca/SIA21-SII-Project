CREATE DATABASE IF NOT EXISTS sia21_p2;
USE sia21_p2;

CREATE OR REPLACE VIEW vw_themes AS
WITH raw AS (
    SELECT java_method(
        'org.spark.service.rest.QueryRESTDataService',
        'getRESTDataDocument',
        'http://xls-service:8092/DSA-DOC-XLSService/api/doc/themes'
    ) AS data
),
json_view AS (
    SELECT from_json(
        data,
        'ARRAY<STRUCT<bggId:INT,theme:STRING>>'
    ) AS array
    FROM raw
)
SELECT
    v.bggId AS boardgame_id,
    v.theme
FROM json_view
LATERAL VIEW explode(array) exploded AS v;
