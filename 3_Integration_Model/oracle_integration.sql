-- FETCH USER RATINGS FROM EXTERNAL REST ENDPOINT
DECLARE
l_req   UTL_HTTP.req;
    l_resp  UTL_HTTP.resp;
    l_chunk VARCHAR2(32767);
    l_clob  CLOB;
BEGIN
    DBMS_LOB.CREATETEMPORARY(l_clob, TRUE);

    l_req := UTL_HTTP.BEGIN_REQUEST('http://host.docker.internal:3000/user_ratings');
    l_resp := UTL_HTTP.GET_RESPONSE(l_req);

BEGIN
        LOOP
UTL_HTTP.READ_TEXT(l_resp, l_chunk, 32767);
            DBMS_LOB.WRITEAPPEND(l_clob, LENGTH(l_chunk), l_chunk);
END LOOP;
EXCEPTION
        WHEN UTL_HTTP.END_OF_BODY THEN
            NULL;
END;

    UTL_HTTP.END_RESPONSE(l_resp);

INSERT INTO USER_RATINGS_JSON (JSON_DATA)
VALUES (l_clob);

COMMIT;
END;
/

-- =========================================================
-- PARSE JSON DATA INTO A RELATIONAL VIEW
-- =========================================================

CREATE OR REPLACE VIEW USER_RATINGS_V AS
SELECT
    jt.BGG_ID,
    jt.RATING,
    jt.USERNAME
FROM USER_RATINGS_JSON urj,
     JSON_TABLE(
             urj.JSON_DATA,
             '$[*]'
                 COLUMNS (
                 BGG_ID   NUMBER        PATH '$.bgg_id',
                 RATING   NUMBER        PATH '$.rating',
                 USERNAME VARCHAR2(100) PATH '$.username'
                 )
     ) jt;

-- =========================================================
-- FINAL COMBINED VIEW FOR ANALYTICAL QUERIES
-- =========================================================

CREATE OR REPLACE VIEW BOARDGAMES_FULL_V AS
SELECT
    g.BGG_ID,
    g.NAME,
    g.YEAR_PUBLISHED,
    g.MIN_PLAYERS,
    g.MAX_PLAYERS,
    g.AVG_RATING,
    t.THEME,
    r.USERNAME,
    r.RATING
FROM GAMES_V g
         LEFT JOIN THEMES_V t
                   ON g.BGG_ID = t.BGG_ID
         LEFT JOIN USER_RATINGS_V r
                   ON g.BGG_ID = r.BGG_ID;

CREATE OR REPLACE VIEW GAME_RATING_STATS_V AS
SELECT
    g.BGG_ID,
    g.NAME,
    COUNT(r.RATING) AS NUM_RATINGS,
    ROUND(AVG(r.RATING), 2) AS AVG_USER_RATING
FROM GAMES_V g
         LEFT JOIN USER_RATINGS_V r
                   ON g.BGG_ID = r.BGG_ID
GROUP BY
    g.BGG_ID,
    g.NAME;

CREATE OR REPLACE VIEW THEME_RATING_STATS_V AS
SELECT
    t.THEME,
    COUNT(r.RATING) AS NUM_RATINGS,
    ROUND(AVG(r.RATING), 2) AS AVG_RATING
FROM THEMES_V t
         JOIN USER_RATINGS_V r
              ON t.BGG_ID = r.BGG_ID
GROUP BY
    t.THEME;

CREATE OR REPLACE VIEW TOP_GAMES_V AS
SELECT
    g.BGG_ID,
    g.NAME,
    COUNT(r.RATING) AS NUM_RATINGS,
    ROUND(AVG(r.RATING), 2) AS AVG_USER_RATING
FROM GAMES_V g
         LEFT JOIN USER_RATINGS_V r
                   ON g.BGG_ID = r.BGG_ID
GROUP BY
    g.BGG_ID,
    g.NAME
HAVING COUNT(r.RATING) > 0;