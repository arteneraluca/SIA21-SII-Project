BEGIN
EXECUTE IMMEDIATE 'DROP VIEW TOP_GAMES_V';
EXCEPTION WHEN OTHERS THEN
    IF SQLCODE != -942 THEN RAISE; END IF;
END;
/

BEGIN
EXECUTE IMMEDIATE 'DROP VIEW THEME_RATING_STATS_V';
EXCEPTION WHEN OTHERS THEN
    IF SQLCODE != -942 THEN RAISE; END IF;
END;
/

BEGIN
EXECUTE IMMEDIATE 'DROP VIEW GAME_RATING_STATS_V';
EXCEPTION WHEN OTHERS THEN
    IF SQLCODE != -942 THEN RAISE; END IF;
END;
/

BEGIN
EXECUTE IMMEDIATE 'DROP VIEW BOARDGAMES_FULL_V';
EXCEPTION WHEN OTHERS THEN
    IF SQLCODE != -942 THEN RAISE; END IF;
END;
/

BEGIN
EXECUTE IMMEDIATE 'DROP VIEW USER_RATINGS_V';
EXCEPTION WHEN OTHERS THEN
    IF SQLCODE != -942 THEN RAISE; END IF;
END;
/

BEGIN
EXECUTE IMMEDIATE 'DROP VIEW GAMES_V';
EXCEPTION WHEN OTHERS THEN
    IF SQLCODE != -942 THEN RAISE; END IF;
END;
/

BEGIN
EXECUTE IMMEDIATE 'DROP VIEW THEMES_V';
EXCEPTION WHEN OTHERS THEN
    IF SQLCODE != -942 THEN RAISE; END IF;
END;
/

BEGIN
EXECUTE IMMEDIATE 'DROP TABLE USER_RATINGS_JSON PURGE';
EXCEPTION WHEN OTHERS THEN
    IF SQLCODE != -942 THEN RAISE; END IF;
END;
/

BEGIN
EXECUTE IMMEDIATE 'DROP TABLE THEMES_EXT';
EXCEPTION WHEN OTHERS THEN
    IF SQLCODE != -942 THEN RAISE; END IF;
END;
/

BEGIN
EXECUTE IMMEDIATE 'DROP TABLE GAMES PURGE';
EXCEPTION WHEN OTHERS THEN
    IF SQLCODE != -942 THEN RAISE; END IF;
END;
/

-- =========================================================
-- MAIN TABLE
-- =========================================================

DROP TABLE GAMES;

CREATE TABLE GAMES
(
    BGG_ID         INT PRIMARY KEY,
    NAME           VARCHAR(255),
    YEAR_PUBLISHED INT,
    MIN_PLAYERS    INT,
    MAX_PLAYERS    INT,
    AVG_RATING     DOUBLE PRECISION
);

-- =========================================================
-- EXTERNAL TABLE: READ THEMES FROM CSV
-- =========================================================

DROP TABLE THEMES_EXT;

CREATE TABLE THEMES_EXT (
                            BGG_ID NUMBER,
                            Adventure NUMBER,
                            Fantasy NUMBER,
                            Environmental NUMBER,
                            Economic NUMBER,
                            Transportation NUMBER,
                            Science_Fiction NUMBER,
                            Space_Exploration NUMBER,
                            Civilization NUMBER,
                            Horror NUMBER,
                            Medieval NUMBER,
                            Ancient NUMBER,
                            Pirates NUMBER,
                            Zombies NUMBER,
                            Sports NUMBER,
                            Music NUMBER,
                            Political NUMBER,
                            Math NUMBER,
                            City_Building NUMBER
)
    ORGANIZATION EXTERNAL (
    TYPE ORACLE_LOADER
    DEFAULT DIRECTORY DATA_DIR
    ACCESS PARAMETERS (
    RECORDS DELIMITED BY NEWLINE
        SKIP 1
    FIELDS TERMINATED BY ','
        OPTIONALLY ENCLOSED BY '"'
        MISSING FIELD VALUES ARE NULL
        (
        BGG_ID,
        Adventure,
        Fantasy,
        Environmental,
        Economic,
        Transportation,
        Science_Fiction,
        Space_Exploration,
        Civilization,
        Horror,
        Medieval,
        Ancient,
        Pirates,
        Zombies,
        Sports,
        Music,
        Political,
        Math,
        City_Building
        )
    )
    LOCATION ('themes_small_clean.csv')
    )
    REJECT LIMIT UNLIMITED;

-- =========================================================
-- VIEWS
-- =========================================================

CREATE OR REPLACE VIEW THEMES_V AS
SELECT BGG_ID, 'Adventure' AS THEME FROM THEMES_EXT WHERE Adventure = 1
UNION ALL
SELECT BGG_ID, 'Fantasy' FROM THEMES_EXT WHERE Fantasy = 1
UNION ALL
SELECT BGG_ID, 'Environmental' FROM THEMES_EXT WHERE Environmental = 1
UNION ALL
SELECT BGG_ID, 'Economic' FROM THEMES_EXT WHERE Economic = 1
UNION ALL
SELECT BGG_ID, 'Transportation' FROM THEMES_EXT WHERE Transportation = 1
UNION ALL
SELECT BGG_ID, 'Science_Fiction' FROM THEMES_EXT WHERE Science_Fiction = 1
UNION ALL
SELECT BGG_ID, 'Space_Exploration' FROM THEMES_EXT WHERE Space_Exploration = 1
UNION ALL
SELECT BGG_ID, 'Civilization' FROM THEMES_EXT WHERE Civilization = 1
UNION ALL
SELECT BGG_ID, 'Horror' FROM THEMES_EXT WHERE Horror = 1
UNION ALL
SELECT BGG_ID, 'Medieval' FROM THEMES_EXT WHERE Medieval = 1
UNION ALL
SELECT BGG_ID, 'Ancient' FROM THEMES_EXT WHERE Ancient = 1
UNION ALL
SELECT BGG_ID, 'Pirates' FROM THEMES_EXT WHERE Pirates = 1
UNION ALL
SELECT BGG_ID, 'Zombies' FROM THEMES_EXT WHERE Zombies = 1
UNION ALL
SELECT BGG_ID, 'Sports' FROM THEMES_EXT WHERE Sports = 1
UNION ALL
SELECT BGG_ID, 'Music' FROM THEMES_EXT WHERE Music = 1
UNION ALL
SELECT BGG_ID, 'Political' FROM THEMES_EXT WHERE Political = 1
UNION ALL
SELECT BGG_ID, 'Math' FROM THEMES_EXT WHERE Math = 1
UNION ALL
SELECT BGG_ID, 'City_Building' FROM THEMES_EXT WHERE City_Building = 1;

-- Simple view over the main games table
CREATE OR REPLACE VIEW GAMES_V AS
SELECT
    BGG_ID,
    NAME,
    YEAR_PUBLISHED,
    MIN_PLAYERS,
    MAX_PLAYERS,
    AVG_RATING
FROM GAMES;

DROP TABLE USER_RATINGS_JSON;

-- STORE RAW USER RATINGS JSON FROM API
CREATE TABLE USER_RATINGS_JSON (
                                   JSON_DATA CLOB
);