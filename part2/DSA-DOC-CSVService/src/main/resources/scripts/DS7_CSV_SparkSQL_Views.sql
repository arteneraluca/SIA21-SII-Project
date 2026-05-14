----------------------------------------------------------------------------------
--- DSA_CSV_SparkSQL_Views.sql
----------------------------------------------------------------------------------
SELECT java_method(
               'org.spark.service.rest.QueryRESTDataService',
               'getRESTDataDocument',
               'http://localhost:8097/DSA-DOC-CSVService/rest/themes');

----------------------------------------------------------------------------------
-- 1. Get Data Source JSON Schema
SELECT java_method(
               'org.spark.service.rest.QueryRESTDataService',
               'getRESTDataDocument',
               'http://localhost:8097/DSA-DOC-CSVService/rest/themes');

SELECT schema_of_json('[
    {"bggId":375,"adventure":0,"fantasy":0,"environmental":0,"economic":0,"transportation":0,"scienceFiction":1,"spaceExploration":0,"civilization":0,"horror":0,"medieval":0,"ancient":0,"pirates":0,"zombies":0,"sports":0,"music":0,"political":0,"math":0,"cityBuilding":0}
    ]');

-- 2. Create Remote View
-- DROP VIEW THEMES_CSV_JSON_VIEW;
CREATE OR REPLACE VIEW THEMES_CSV_JSON_VIEW AS
WITH json_view AS (
    SELECT from_json(json_raw.data,
                     'ARRAY<STRUCT<bggId: INT, adventure: INT, fantasy: INT, environmental: INT, economic: INT, transportation: INT, scienceFiction: INT, spaceExploration: INT, civilization: INT, horror: INT, medieval: INT, ancient: INT, pirates: INT, zombies: INT, sports: INT, music: INT, political: INT, math: INT, cityBuilding: INT>>') array
    FROM (SELECT java_method('org.spark.service.rest.QueryRESTDataService', 'getRESTDataDocument',
        'http://localhost:8097/DSA-DOC-CSVService/rest/themes')
        as data) json_raw
)
select v.*
FROM json_view LATERAL VIEW explode(json_view.array) AS v;

-- DROP VIEW THEMES_CSV_VIEW;
CREATE OR REPLACE VIEW THEMES_CSV_VIEW AS
select
    bggId AS BGGId,
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

-- DROP VIEW THEMES_CSV_THEME_COUNTS_VIEW;
CREATE OR REPLACE VIEW THEMES_CSV_THEME_COUNTS_VIEW AS
select themeName, count(*) AS gamesCount
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
GROUP BY themeName
ORDER BY gamesCount DESC, themeName;

-- 3. Test Remote View
select * FROM THEMES_CSV_VIEW;
select count(*) AS themesCount FROM THEMES_CSV_VIEW;
select * FROM THEMES_CSV_VIEW WHERE BGGId = 375;
select * FROM THEMES_CSV_VIEW WHERE Science_Fiction = 1;
select * FROM THEMES_CSV_THEME_COUNTS_VIEW;
