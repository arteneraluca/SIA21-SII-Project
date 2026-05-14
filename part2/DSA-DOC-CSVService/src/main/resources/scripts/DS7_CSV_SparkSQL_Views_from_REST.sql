----------------------------------------------------------------------------------
--- DSA_CSV_SparkSQL_Views_from_REST.sql
----------------------------------------------------------------------------------
SELECT java_method(
               'org.spark.service.rest.QueryRESTDataService',
               'getRESTDataDocument',
               'http://localhost:8097/DSA-DOC-CSVService/rest/themes');

----------------------------------------------------------------------------------
-- 1. Create JSON View
SELECT java_method(
               'org.spark.service.rest.RESTEnabledSQLService',
               'createJSONViewFromREST',
               'THEMES_CSV_JSON_VIEW',
               'http://localhost:8097/DSA-DOC-CSVService/rest/themes');

SELECT * FROM THEMES_CSV_JSON_VIEW;

-- 2. Create Remote View
-- DROP VIEW THEMES_CSV_VIEW;
CREATE OR REPLACE VIEW THEMES_CSV_VIEW AS
select
    v.bggId AS BGGId,
    v.adventure AS Adventure,
    v.fantasy AS Fantasy,
    v.environmental AS Environmental,
    v.economic AS Economic,
    v.transportation AS Transportation,
    v.scienceFiction AS Science_Fiction,
    v.spaceExploration AS Space_Exploration,
    v.civilization AS Civilization,
    v.horror AS Horror,
    v.medieval AS Medieval,
    v.ancient AS Ancient,
    v.pirates AS Pirates,
    v.zombies AS Zombies,
    v.sports AS Sports,
    v.music AS Music,
    v.political AS Political,
    v.math AS Math,
    v.cityBuilding AS City_Building
FROM THEMES_CSV_JSON_VIEW as json_view LATERAL VIEW explode(json_view.array) AS v;

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
