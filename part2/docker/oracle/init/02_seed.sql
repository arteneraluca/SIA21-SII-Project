WHENEVER SQLERROR EXIT SQL.SQLCODE

ALTER SESSION SET CONTAINER = FREEPDB1;
ALTER SESSION SET CURRENT_SCHEMA = SIA_ORACLE;

TRUNCATE TABLE USER_RATINGS;
TRUNCATE TABLE THEMES;
TRUNCATE TABLE GAMES;

INSERT INTO GAMES (BGG_ID, NAME, YEAR_PUBLISHED, MIN_PLAYERS, MAX_PLAYERS, AVG_RATING)
SELECT BGG_ID, NAME, YEAR_PUBLISHED, MIN_PLAYERS, MAX_PLAYERS, AVG_RATING
FROM GAMES_EXT;

INSERT INTO THEMES (BGG_ID, THEME)
SELECT BGG_ID, THEME
FROM THEMES_EXT
UNPIVOT
(
    FLAG FOR THEME IN
    (
        Adventure AS 'Adventure',
        Fantasy AS 'Fantasy',
        Environmental AS 'Environmental',
        Economic AS 'Economic',
        Transportation AS 'Transportation',
        Science_Fiction AS 'Science_Fiction',
        Space_Exploration AS 'Space_Exploration',
        Civilization AS 'Civilization',
        Horror AS 'Horror',
        Medieval AS 'Medieval',
        Ancient AS 'Ancient',
        Pirates AS 'Pirates',
        Zombies AS 'Zombies',
        Sports AS 'Sports',
        Music AS 'Music',
        Political AS 'Political',
        Math AS 'Math',
        City_Building AS 'City_Building'
    )
)
WHERE FLAG = 1;

INSERT INTO USER_RATINGS (BGG_ID, RATING, USERNAME)
SELECT BGG_ID, RATING, USERNAME
FROM USER_RATINGS_EXT;

COMMIT;
