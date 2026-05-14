package org.spark.service;

import jakarta.annotation.PostConstruct;
import org.apache.spark.sql.SparkSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class DataSourceViewInitializer {
    private static final Logger logger = Logger.getLogger(DataSourceViewInitializer.class.getName());

    private final SparkSQLService sparkSQLService;

    @Value("${sparksql.sources.auto-register.enabled:true}")
    private boolean autoRegisterEnabled;

    @Value("${sparksql.hive.database:sia21}")
    private String hiveDatabase;

    @Value("${sparksql.sources.csv.themes-url:http://localhost:8097/DSA-DOC-CSVService/rest/themes}")
    private String themesUrl;

    @Value("${sparksql.sources.jdbc.postgresql.url:jdbc:postgresql://localhost:55432/sia21}")
    private String postgresqlUrl;

    @Value("${sparksql.sources.jdbc.postgresql.user:sia}")
    private String postgresqlUser;

    @Value("${sparksql.sources.jdbc.postgresql.password:sia}")
    private String postgresqlPassword;

    @Value("${sparksql.sources.jdbc.postgresql.table:user_ratings}")
    private String postgresqlUserRatingsTable;

    @Value("${sparksql.sources.jdbc.oracle.url:jdbc:oracle:thin:@//localhost:1521/FREEPDB1}")
    private String oracleUrl;

    @Value("${sparksql.sources.jdbc.oracle.user:games}")
    private String oracleUser;

    @Value("${sparksql.sources.jdbc.oracle.password:games}")
    private String oraclePassword;

    @Value("${sparksql.sources.jdbc.oracle.table:GAMES}")
    private String oracleGamesTable;

    public DataSourceViewInitializer(SparkSQLService sparkSQLService) {
        this.sparkSQLService = sparkSQLService;
    }

    @PostConstruct
    public void init() {
        if (!autoRegisterEnabled) {
            logger.info("SparkSQL source view auto-registration is disabled.");
            return;
        }

        SparkSession spark = sparkSQLService.getSpark();
        createDatabase(spark);
        registerThemesViews(spark);
        registerPostgreSQLViews(spark);
        registerOracleViews(spark);
        registerDemoViews(spark);
    }

    private void createDatabase(SparkSession spark) {
        spark.sql("CREATE DATABASE IF NOT EXISTS " + hiveDatabase);
        spark.sql("USE " + hiveDatabase);
    }

    private void registerThemesViews(SparkSession spark) {
        try {
            spark.sql("""
                    CREATE OR REPLACE VIEW THEMES_CSV_JSON_VIEW AS
                    WITH json_view AS (
                        SELECT from_json(json_raw.data,
                            'ARRAY<STRUCT<bggId: INT, adventure: INT, fantasy: INT, environmental: INT,
                            economic: INT, transportation: INT, scienceFiction: INT, spaceExploration: INT,
                            civilization: INT, horror: INT, medieval: INT, ancient: INT, pirates: INT,
                            zombies: INT, sports: INT, music: INT, political: INT, math: INT,
                            cityBuilding: INT>>') array
                        FROM (SELECT java_method('org.spark.service.rest.QueryRESTDataService',
                            'getRESTDataDocument', '%s') AS data) json_raw
                    )
                    SELECT v.*
                    FROM json_view LATERAL VIEW explode(json_view.array) AS v
                    """.formatted(escapeSql(themesUrl)));

            spark.sql("""
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
                      FROM THEMES_CSV_JSON_VIEW
                    """);

            spark.sql("ALTER VIEW THEMES_CSV_VIEW SET TBLPROPERTIES('AUTOREST' = 'themes')");
        } catch (Throwable exception) {
            logger.log(Level.WARNING, "Could not register CSV themes views.", exception);
        }
    }

    private void registerPostgreSQLViews(SparkSession spark) {
        try {
            spark.sql("""
                    CREATE TABLE IF NOT EXISTS USER_RATINGS_JDBC_TABLE
                    USING jdbc
                    OPTIONS (
                        url '%s',
                        dbtable '%s',
                        user '%s',
                        password '%s',
                        driver 'org.postgresql.Driver'
                    )
                    """.formatted(
                    escapeSql(postgresqlUrl),
                    escapeSql(postgresqlUserRatingsTable),
                    escapeSql(postgresqlUser),
                    escapeSql(postgresqlPassword)));

            spark.sql("""
                    CREATE OR REPLACE VIEW USER_RATINGS_VIEW AS
                    SELECT bgg_id,
                           rating,
                           username
                      FROM USER_RATINGS_JDBC_TABLE
                    """);

            spark.sql("ALTER VIEW USER_RATINGS_VIEW SET TBLPROPERTIES('AUTOREST' = 'ratings')");
        } catch (Throwable exception) {
            logger.log(Level.WARNING, "Could not register PostgreSQL user ratings views.", exception);
        }
    }

    private void registerOracleViews(SparkSession spark) {
        try {
            spark.sql("""
                    CREATE TABLE IF NOT EXISTS GAMES_ORACLE_TABLE
                    USING jdbc
                    OPTIONS (
                        url '%s',
                        dbtable '%s',
                        user '%s',
                        password '%s',
                        driver 'oracle.jdbc.OracleDriver'
                    )
                    """.formatted(
                    escapeSql(oracleUrl),
                    escapeSql(oracleGamesTable),
                    escapeSql(oracleUser),
                    escapeSql(oraclePassword)));

            spark.sql("""
                    CREATE OR REPLACE VIEW GAMES_VIEW AS
                    SELECT BGG_ID AS bggId,
                           NAME AS name,
                           YEAR_PUBLISHED AS yearPublished,
                           MIN_PLAYERS AS minPlayers,
                           MAX_PLAYERS AS maxPlayers,
                           AVG_RATING AS avgRating
                      FROM GAMES_ORACLE_TABLE
                    """);

            spark.sql("""
                    CREATE OR REPLACE VIEW TOP_GAMES_VIEW AS
                    SELECT bggId,
                           name,
                           yearPublished,
                           minPlayers,
                           maxPlayers,
                           avgRating
                      FROM GAMES_VIEW
                     WHERE avgRating >= 6
                    """);

            spark.sql("ALTER VIEW GAMES_VIEW SET TBLPROPERTIES('AUTOREST' = 'games')");
            spark.sql("ALTER VIEW TOP_GAMES_VIEW SET TBLPROPERTIES('AUTOREST' = 'games/top')");
        } catch (Throwable exception) {
            logger.log(Level.WARNING, "Could not register Oracle games views.", exception);
        }
    }

    private void registerDemoViews(SparkSession spark) {
        executeIfPossible(spark, """
                CREATE OR REPLACE VIEW THEMES_CSV_THEME_COUNTS_VIEW AS
                SELECT themeName, count(*) AS gamesCount
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
                """);

        executeIfPossible(spark, """
                CREATE OR REPLACE VIEW RATINGS_BY_BGG_ID_VIEW AS
                SELECT bgg_id,
                       COUNT(*) AS rating_count,
                       ROUND(AVG(rating), 2) AS average_rating,
                       MIN(rating) AS minimum_rating,
                       MAX(rating) AS maximum_rating
                  FROM USER_RATINGS_VIEW
                 GROUP BY bgg_id
                """);

        executeIfPossible(spark, """
                CREATE OR REPLACE VIEW AVERAGE_RATING_BY_USER_VIEW AS
                SELECT username,
                       COUNT(*) AS rating_count,
                       ROUND(AVG(rating), 2) AS average_rating
                  FROM USER_RATINGS_VIEW
                 GROUP BY username
                """);

        executeIfPossible(spark, """
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
                  LEFT JOIN RATINGS_BY_BGG_ID_VIEW r ON g.bggId = r.bgg_id
                """);

        executeIfPossible(spark, """
                CREATE OR REPLACE VIEW RATINGS_GAMES_VIEW AS
                SELECT r.bgg_id,
                       g.name,
                       r.rating,
                       r.username,
                       g.yearPublished,
                       g.avgRating
                  FROM USER_RATINGS_VIEW r
                  INNER JOIN GAMES_VIEW g ON r.bgg_id = g.bggId
                """);
    }

    private void executeIfPossible(SparkSession spark, String query) {
        try {
            spark.sql(query);
        } catch (Throwable exception) {
            logger.log(Level.WARNING, "Could not register demo view with query: " + query, exception);
        }
    }

    private String escapeSql(String value) {
        return value.replace("'", "''");
    }
}
