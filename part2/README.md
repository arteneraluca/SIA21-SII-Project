## 1. Overview

This root contains these Part 3 services:

| Service | Purpose |
| --- | --- |
| `DSA-DOC-CSVService` | Spring Boot REST service that reads `src/main/resources/datasource/themes.csv` and exposes board game theme data as JSON/XML. |
| `DSA-SQL-JDBCService` | Spring Boot REST service that connects to PostgreSQL through JDBC and exposes `user_ratings` views. |
| `DSA-SQL-JPAService` | Spring Boot REST service that connects to Oracle through Spring Data JPA and exposes board game views from the `GAMES` table. |
| `DSA-SparkSQL-Service` | Spring Boot service that starts Spark SQL with Hive support, registers CSV/PostgreSQL/Oracle sources as SparkSQL views, exposes SparkSQL views over REST, and starts Hive Thrift Server on port `10000`. |
| `DSA-WEB-RESTService` | Spring Boot REST service that connects to Hive/SparkSQL through JDBC and exposes aggregated OLAP views. |

## 2. Prerequisites

- Java: Java 21 is configured in every Part 3 `pom.xml`.
- Maven: all services are Maven projects. Use `mvn spring-boot:run` from each service folder.
- Gradle: Not found in project.
- Docker / Docker Compose: required for PostgreSQL and Oracle local databases.
- DataGrip or any SQL client: useful for PostgreSQL, Oracle, and Hive/SparkSQL verification.

## 3. How to start infrastructure

Assumption for all commands in this guide: the current directory is the Part 3 root, the directory that contains `DSA-DOC-CSVService`, `DSA-SQL-JDBCService`, `DSA-SQL-JPAService`, `DSA-SparkSQL-Service`, and `DSA-WEB-RESTService`.

Start PostgreSQL only:

```bash
(cd DSA-SQL-JDBCService && docker compose up -d postgres)
```

Start Oracle only:

```bash
(cd DSA-SQL-JPAService && docker compose up -d oracle-db)
```

Start Hive/Spark infrastructure:

```bash
(cd DSA-SparkSQL-Service && MAVEN_OPTS="--add-opens=java.base/java.net=ALL-UNNAMED --add-opens=java.base/sun.util.calendar=ALL-UNNAMED" mvn spring-boot:run)
```

`DSA-SparkSQL-Service` starts Spark SQL locally and starts Hive Thrift Server on `localhost:10000`. A separate Hive Docker Compose file was not found in project.

Start everything required for Part 3:

```bash
(cd DSA-SQL-JDBCService && docker compose up -d postgres)
(cd DSA-SQL-JPAService && docker compose up -d oracle-db)
```

Then run the Java services locally as described below.

## 4. How to run each Java service locally

Run each service in a separate terminal.

### `DSA-DOC-CSVService`

- Folder: `DSA-DOC-CSVService`
- Command:

```bash
cd DSA-DOC-CSVService
mvn spring-boot:run
```

- Expected port: `8097`
- Context path: `/DSA-DOC-CSVService/rest`
- Required environment variables: none found.
- Configured data file: `csv.data.source.file.path=datasource/themes.csv`

### `DSA-SQL-JDBCService`

- Folder: `DSA-SQL-JDBCService`
- Command:

```bash
cd DSA-SQL-JDBCService
mvn spring-boot:run
```

- Expected port: `8090`
- Context path: `/DSA-SQL-JDBCService/rest`
- Required environment variables: none required when using defaults.
- Optional environment variables:
  - `JDBC_DATA_SOURCE_DB_URL`, default `jdbc:postgresql://localhost:55432/sia21`
  - `JDBC_DATA_SOURCE_USER`, default `sia`
  - `JDBC_DATA_SOURCE_PASS`, default `sia`

### `DSA-SQL-JPAService`

- Folder: `DSA-SQL-JPAService`
- Command:

```bash
cd DSA-SQL-JPAService
mvn spring-boot:run
```

- Expected port: `8091`
- Context path: `/DSA_SQL_JPAService/rest`
- Required environment variables: none found.
- Oracle connection is hardcoded in `application.properties` as `jdbc:oracle:thin:@//localhost:1521/FREEPDB1`, user `games`, password `games`.

### `DSA-SparkSQL-Service`

- Folder: `DSA-SparkSQL-Service`
- Command:

```bash
cd DSA-SparkSQL-Service
MAVEN_OPTS="--add-opens=java.base/java.net=ALL-UNNAMED --add-opens=java.base/sun.util.calendar=ALL-UNNAMED" mvn spring-boot:run
```

- Expected REST port: `9990`
- Spark UI port: `8081`
- Hive Thrift Server port: `10000`
- Context path: `/DSA-SparkSQL-Service`
- Required environment variables: none required when using defaults.
- Optional configuration properties have defaults in code:
  - `SPARK_SQL_MASTER`: default `local[*]`
  - `SPARK_SQL_UI_PORT`: default `8081`
  - `SPARK_SQL_DRIVER_HOST`: default `127.0.0.1`
  - `SPARK_SQL_DRIVER_BIND_ADDRESS`: default `127.0.0.1`
  - `SPARK_SQL_WAREHOUSE_DIR`: default `file:/tmp/sia21-spark-warehouse`
  - `SPARK_SQL_METASTORE_URL`: default `jdbc:derby:;databaseName=/tmp/sia21-spark-metastore;create=true`
  - `SPARK_SQL_THRIFT_HOST`: default `0.0.0.0`
  - `SPARK_SQL_THRIFT_PORT`: default `10000`
  - `SPARK_SQL_HIVE_DATABASE`: default `sia21`
  - `DSA_DOC_CSV_SERVICE_URL`: default `http://localhost:8097/DSA-DOC-CSVService/rest`
  - `DSA_SQL_JDBC_SERVICE_URL`: default `http://localhost:8090/DSA-SQL-JDBCService/rest`
  - `DSA_SQL_JPA_SERVICE_URL`: default `http://localhost:8091/DSA_SQL_JPAService/rest`
  - `JDBC_DATA_SOURCE_DB_URL`: default `jdbc:postgresql://localhost:55432/sia21`
  - `JDBC_DATA_SOURCE_USER`: default `sia`
  - `JDBC_DATA_SOURCE_PASS`: default `sia`
  - `ORACLE_DATA_SOURCE_DB_URL`: default `jdbc:oracle:thin:@//localhost:1521/FREEPDB1`
  - `ORACLE_DATA_SOURCE_USER`: default `games`
  - `ORACLE_DATA_SOURCE_PASS`: default `games`

### `DSA-WEB-RESTService`

- Folder: `DSA-WEB-RESTService`
- Command:

```bash
cd DSA-WEB-RESTService
mvn spring-boot:run
```

- Expected port: `8096`
- Context path: `/DSA-WEB-RESTService/rest`
- Required environment variables: none found.
- Requires `DSA-SparkSQL-Service` running because it connects to Hive Thrift Server at `jdbc:hive2://localhost:10000/sia21`.

## 5. Database connection details for DataGrip

### PostgreSQL

- Host: `localhost`
- Port: `55432`
- Database: `sia21`
- Username: `sia`
- Password: `sia`
- JDBC URL: `jdbc:postgresql://localhost:55432/sia21`
- Driver: PostgreSQL JDBC driver, class `org.postgresql.Driver`

### Oracle

- Host: `localhost`
- Port: `1521`
- Service name: `FREEPDB1`
- SID: Not found in project.
- Username: `games`
- Password: `games`
- JDBC URL: `jdbc:oracle:thin:@//localhost:1521/FREEPDB1`
- Driver: Oracle JDBC driver, class `oracle.jdbc.OracleDriver`

### Hive / SparkSQL

- Host: `localhost`
- Port: `10000`
- Database/schema: `sia21`
- Username: `hive`
- Password: no password is configured in `DSA-WEB-RESTService`; the property is empty.
- JDBC URL: `jdbc:hive2://localhost:10000/sia21`
- Driver: Hive JDBC driver, class `org.apache.hive.jdbc.HiveDriver`

## 6. SQL scripts to run

### PostgreSQL

| Script | Location | Run in | Automatic or manual | Purpose |
| --- | --- | --- | --- | --- |
| `user_ratings.sql` | `DSA-SQL-JDBCService/postgresql/user_ratings.sql` | PostgreSQL, Docker init | Automatic when `DSA-SQL-JDBCService/docker-compose.yml` initializes a new PostgreSQL volume | Creates `user_ratings`, indexes, grants read access to `web_anon`, and inserts the sample rating rows directly from SQL `VALUES`. |
| `DS1_PostgreSQL_SparkSQL_Views.sql` | `DSA-SQL-JDBCService/src/main/resources/scripts/DS1_PostgreSQL_SparkSQL_Views.sql` | PostgreSQL / DataGrip | Manual | Creates PostgreSQL views such as `user_ratings_view`, `ratings_by_bgg_id_view`, `top_rated_bgg_ids_view`, `average_rating_by_user_view`, and `rating_distribution_view`. |
| `DS1_PostgreSQL_SparkSQL_Views_from_REST.sql` | `DSA-SQL-JDBCService/src/main/resources/scripts/DS1_PostgreSQL_SparkSQL_Views_from_REST.sql` | SparkSQL | Manual | Builds SparkSQL views from `DSA-SQL-JDBCService` REST endpoints using `java_method`. |

### Oracle

| Script | Location | Run in | Automatic or manual | Purpose |
| --- | --- | --- | --- | --- |
| `games.sql` | `DSA-SQL-JPAService/src/main/resources/scripts/games.sql` | Oracle, Docker init | Automatic when `DSA-SQL-JPAService/docker-compose.yml` initializes a new Oracle volume | Switches to `FREEPDB1`, uses schema `GAMES`, recreates table `GAMES`, inserts sample board game data, and creates `GAMES_V`, `TOP_GAMES_V`, `GAME_RATING_STATS_V`, and `PLAYER_COUNT_STATS_V`. |
| `DS2_ORACLE_SparkSQL_Views.sql` | `DSA-SQL-JPAService/src/main/resources/scripts/DS2_ORACLE_SparkSQL_Views.sql` | SparkSQL | Manual | Creates SparkSQL views from `DSA-SQL-JPAService` REST endpoints. |
| `DS2_ORACLE_SparkSQL_Views_from_REST.sql` | `DSA-SQL-JPAService/src/main/resources/scripts/DS2_ORACLE_SparkSQL_Views_from_REST.sql` | SparkSQL | Manual | Creates JSON-backed SparkSQL views from Oracle REST endpoints using `createJSONViewFromREST`. |

### CSV / SparkSQL

| Script | Location | Run in | Automatic or manual | Purpose |
| --- | --- | --- | --- | --- |
| `DS7_CSV_SparkSQL_Views.sql` | `DSA-DOC-CSVService/src/main/resources/scripts/DS7_CSV_SparkSQL_Views.sql` | SparkSQL | Manual | Creates `THEMES_CSV_JSON_VIEW`, `THEMES_CSV_VIEW`, and `THEMES_CSV_THEME_COUNTS_VIEW` from the CSV REST service. |
| `DS7_CSV_SparkSQL_Views_from_REST.sql` | `DSA-DOC-CSVService/src/main/resources/scripts/DS7_CSV_SparkSQL_Views_from_REST.sql` | SparkSQL | Manual | Creates SparkSQL CSV theme views through REST-enabled SQL helper calls. |
| `SparkSQL_OLAP_Multidimensional_Analytical.sql` | `DSA-SparkSQL-Service/src/main/resources/scripts/SparkSQL_OLAP_Multidimensional_Analytical.sql` | SparkSQL / Hive through DataGrip | Manual, but most core views are also auto-registered by `DataSourceViewInitializer` on service startup | Creates database `sia21`, registers CSV/PostgreSQL/Oracle sources, creates access, integration, and analytical OLAP views, and sets selected `AUTOREST` paths. |

Hive-specific SQL scripts: Not found in project. Hive/SparkSQL views are created by `DSA-SparkSQL-Service` and the SparkSQL scripts above.

## 7. Docker notes

- PostgreSQL and Oracle init scripts run only when Docker initializes a new database volume for the first time.
- Restarting an existing container does not re-run scripts from `/docker-entrypoint-initdb.d` or `/container-entrypoint-initdb.d`.
- Use non-destructive commands for normal work:

```bash
docker compose up -d
docker compose ps
docker compose logs
docker compose down
```

Optional reset commands, only when you intentionally want to recreate database data and re-run init scripts. Run only inside the specific database service folder you want to reset:

```bash
docker compose down --volumes
```

These optional reset commands remove the service-specific database volumes declared in the compose files, so data in those volumes is deleted.

## 8. Endpoint list with curl commands

Security note: the applications define `spring.security.user.*` properties, but `spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration` is configured and the custom `BasicConfiguration` classes are commented out as configuration. The endpoints below are documented without Basic Auth because that is what the active configuration indicates.

### `DSA-DOC-CSVService` endpoints

Base URL: `http://localhost:8097/DSA-DOC-CSVService/rest`

| Method | URL | Purpose | curl | Expected response |
| --- | --- | --- | --- | --- |
| GET | `/customers/CustomerEmployeesCategoryViewCSV` | Compatibility endpoint returning the CSV theme view. | `curl http://localhost:8097/DSA-DOC-CSVService/rest/customers/CustomerEmployeesCategoryViewCSV` | JSON array of theme records. |
| GET | `/themes` | Return all theme records from `themes.csv`. | `curl http://localhost:8097/DSA-DOC-CSVService/rest/themes` | JSON array of theme records. |
| GET | `/themes/{id}` | Return one theme record by `bggId`. | `curl http://localhost:8097/DSA-DOC-CSVService/rest/themes/375` | JSON object if found, HTTP 404 if not found. |
| GET | `/themes/search?theme={theme}` | Search themes by theme name. | `curl "http://localhost:8097/DSA-DOC-CSVService/rest/themes/search?theme=Adventure"` | JSON array of matching theme records. |

### `DSA-SQL-JDBCService` endpoints

Base URL: `http://localhost:8090/DSA-SQL-JDBCService/rest`

| Method | URL | Purpose | curl | Expected response |
| --- | --- | --- | --- | --- |
| GET | `/ratings/ping` | Health check. | `curl http://localhost:8090/DSA-SQL-JDBCService/rest/ratings/ping` | Plain text ping response. |
| GET | `/ratings/UserRatingView` | Return user rating rows. | `curl http://localhost:8090/DSA-SQL-JDBCService/rest/ratings/UserRatingView` | JSON array with `bggId`, `rating`, and `username` data. |
| GET | `/ratings/UserRatingViewData?fetch_offset={offset}&fetch_size={size}` | Return paged user rating rows. | `curl "http://localhost:8090/DSA-SQL-JDBCService/rest/ratings/UserRatingViewData?fetch_offset=0&fetch_size=10"` | JSON array with up to `fetch_size` rows. |
| GET | `/ratings/UserRatingSummaryView` | Return rating summary by board game id. | `curl http://localhost:8090/DSA-SQL-JDBCService/rest/ratings/UserRatingSummaryView` | JSON array of rating summary records. |
| GET | `/ratings/UserAverageRatingView` | Return average rating by user. | `curl http://localhost:8090/DSA-SQL-JDBCService/rest/ratings/UserAverageRatingView` | JSON array of user average rating records. |

### `DSA-SQL-JPAService` endpoints

Base URL: `http://localhost:8091/DSA_SQL_JPAService/rest`

| Method | URL | Purpose | curl | Expected response |
| --- | --- | --- | --- | --- |
| GET | `/games/ping` | Health check. | `curl http://localhost:8091/DSA_SQL_JPAService/rest/games/ping` | Plain text ping response. |
| GET | `/games/GamesView` | Return all game rows from Oracle. | `curl http://localhost:8091/DSA_SQL_JPAService/rest/games/GamesView` | JSON array of games. |
| GET | `/games/TopGamesView` | Return top games from Oracle repository query. | `curl http://localhost:8091/DSA_SQL_JPAService/rest/games/TopGamesView` | JSON array of top games. |
| GET | `/games/TwoPlayerGamesView` | Return two-player games from Oracle repository query. | `curl http://localhost:8091/DSA_SQL_JPAService/rest/games/TwoPlayerGamesView` | JSON array of games. |
| GET | `/games/GameRatingStatsView` | Return game rating statistics. | `curl http://localhost:8091/DSA_SQL_JPAService/rest/games/GameRatingStatsView` | JSON array with aggregate rating statistics. |

### `DSA-SparkSQL-Service` endpoints

Base URL: `http://localhost:9990/DSA-SparkSQL-Service`

| Method | URL | Purpose | curl | Expected response |
| --- | --- | --- | --- | --- |
| GET | `/rest/ping` | AutoREST health check. | `curl http://localhost:9990/DSA-SparkSQL-Service/rest/ping` | Plain text ping response. |
| GET | `/rest/view/**` | Return all rows from a SparkSQL view. | `curl http://localhost:9990/DSA-SparkSQL-Service/rest/view/GAMES_VIEW` | JSON-like list of rows from the view. |
| GET | `/rest/view/**?redef=true` | Reload AutoREST view definitions, then return view rows. | `curl "http://localhost:9990/DSA-SparkSQL-Service/rest/view/TOP_GAMES_VIEW?redef=true"` | JSON-like list of rows from the view. |
| GET | `/rest/STRUCT/**` | Return Spark SQL schema for a view. | `curl http://localhost:9990/DSA-SparkSQL-Service/rest/STRUCT/GAMES_VIEW` | Text schema for the view. |
| GET | `/rest/auto?redef=true` | Reload and list AutoREST view mappings. | `curl "http://localhost:9990/DSA-SparkSQL-Service/rest/auto?redef=true"` | JSON map of view names to AutoREST paths. |
| GET | `/_sqlrest/query?sql={sql}` | Execute SparkSQL query from query string. | `curl -G "http://localhost:9990/DSA-SparkSQL-Service/_sqlrest/query" --data-urlencode "sql=SELECT * FROM GAMES_VIEW"` | `SQLResponse` JSON with query results or error details. |
| POST | `/_sqlrest/query` | Execute SparkSQL query from request body. | `curl -X POST -H "Content-Type: text/plain" --data "SELECT * FROM GAMES_VIEW" http://localhost:9990/DSA-SparkSQL-Service/_sqlrest/query` | `SQLResponse` JSON with query results or error details. |
| POST | `/_sqlrest/create-json-view-from-rest?view_name={view_name}` | Create a SparkSQL JSON view from a REST endpoint URL in the request body. | `curl -X POST -H "Content-Type: text/plain" --data "http://localhost:8097/DSA-DOC-CSVService/rest/themes" "http://localhost:9990/DSA-SparkSQL-Service/_sqlrest/create-json-view-from-rest?view_name=THEMES_FROM_REST"` | `SQLViewDefinition` JSON containing the generated view definition. |

Useful AutoREST view examples created by startup code or scripts:

```bash
curl http://localhost:9990/DSA-SparkSQL-Service/rest/view/THEMES_CSV_VIEW
curl http://localhost:9990/DSA-SparkSQL-Service/rest/view/USER_RATINGS_VIEW
curl http://localhost:9990/DSA-SparkSQL-Service/rest/view/GAMES_VIEW
curl http://localhost:9990/DSA-SparkSQL-Service/rest/view/TOP_GAMES_VIEW
curl http://localhost:9990/DSA-SparkSQL-Service/rest/view/RATINGS_BY_BGG_ID_VIEW
curl http://localhost:9990/DSA-SparkSQL-Service/rest/view/AVERAGE_RATING_BY_USER_VIEW
curl http://localhost:9990/DSA-SparkSQL-Service/rest/view/TOP_RATED_GAMES_VIEW
curl http://localhost:9990/DSA-SparkSQL-Service/rest/view/RATINGS_GAMES_VIEW
```

### `DSA-WEB-RESTService` endpoints

Base URL: `http://localhost:8096/DSA-WEB-RESTService/rest`

| Method | URL | Purpose | curl | Expected response |
| --- | --- | --- | --- | --- |
| GET | `/OLAP/ping` | Health check. | `curl http://localhost:8096/DSA-WEB-RESTService/rest/OLAP/ping` | Plain text ping response. |
| GET | `/OLAP/GAMES_VIEW` | Return Spark/Hive `GAMES_VIEW`. | `curl http://localhost:8096/DSA-WEB-RESTService/rest/OLAP/GAMES_VIEW` | JSON array of game rows. |
| GET | `/OLAP/GAMES_VIEW/{bggId}` | Return `GAMES_VIEW` rows for one board game id. | `curl http://localhost:8096/DSA-WEB-RESTService/rest/OLAP/GAMES_VIEW/375` | JSON array, empty if no row matches. |
| GET | `/OLAP/USER_RATINGS_VIEW` | Return Spark/Hive `USER_RATINGS_VIEW`. | `curl http://localhost:8096/DSA-WEB-RESTService/rest/OLAP/USER_RATINGS_VIEW` | JSON array of user ratings. |
| GET | `/OLAP/USER_RATINGS_VIEW/{bggId}` | Return user ratings for one board game id. | `curl http://localhost:8096/DSA-WEB-RESTService/rest/OLAP/USER_RATINGS_VIEW/375` | JSON array, empty if no row matches. |
| GET | `/OLAP/THEMES_CSV_VIEW` | Return Spark/Hive `THEMES_CSV_VIEW`. | `curl http://localhost:8096/DSA-WEB-RESTService/rest/OLAP/THEMES_CSV_VIEW` | JSON array of theme rows. |
| GET | `/OLAP/THEMES_CSV_VIEW/{bggId}` | Return theme row for one board game id. | `curl http://localhost:8096/DSA-WEB-RESTService/rest/OLAP/THEMES_CSV_VIEW/375` | JSON array, empty if no row matches. |
| GET | `/OLAP/RATINGS_BY_BGG_ID_VIEW` | Return aggregated rating stats by board game id. | `curl http://localhost:8096/DSA-WEB-RESTService/rest/OLAP/RATINGS_BY_BGG_ID_VIEW` | JSON array of aggregate rows. |
| GET | `/OLAP/TOP_RATED_GAMES_VIEW` | Return integrated top-rated games view. | `curl http://localhost:8096/DSA-WEB-RESTService/rest/OLAP/TOP_RATED_GAMES_VIEW` | JSON array of integrated game/rating rows. |
| GET | `/OLAP/THEMES_CSV_THEME_COUNTS_VIEW` | Return game counts by theme. | `curl http://localhost:8096/DSA-WEB-RESTService/rest/OLAP/THEMES_CSV_THEME_COUNTS_VIEW` | JSON array of theme count rows. |
| GET | `/OLAP/AVERAGE_RATING_BY_USER_VIEW` | Return average rating by user. | `curl http://localhost:8096/DSA-WEB-RESTService/rest/OLAP/AVERAGE_RATING_BY_USER_VIEW` | JSON array of user aggregate rows. |
| GET | `/OLAP/RATINGS_GAMES_VIEW` | Return joined ratings and games view. | `curl http://localhost:8096/DSA-WEB-RESTService/rest/OLAP/RATINGS_GAMES_VIEW` | JSON array of joined rating/game rows. |

## 9. Suggested demo flow

1. Start PostgreSQL:

```bash
(cd DSA-SQL-JDBCService && docker compose up -d postgres)
```

2. Start Oracle:

```bash
(cd DSA-SQL-JPAService && docker compose up -d oracle-db)
```

3. Run access/document services locally:

```bash
cd DSA-DOC-CSVService
mvn spring-boot:run
```

```bash
cd DSA-SQL-JDBCService
mvn spring-boot:run
```

```bash
cd DSA-SQL-JPAService
mvn spring-boot:run
```

4. Verify PostgreSQL data in DataGrip:

```sql
SELECT COUNT(*) FROM user_ratings;
SELECT * FROM user_ratings LIMIT 10;
```

5. Verify Oracle data in DataGrip:

```sql
SELECT COUNT(*) FROM GAMES;
SELECT * FROM GAMES FETCH FIRST 10 ROWS ONLY;
```

6. Start SparkSQL/Hive:

```bash
cd DSA-SparkSQL-Service
MAVEN_OPTS="--add-opens=java.base/java.net=ALL-UNNAMED --add-opens=java.base/sun.util.calendar=ALL-UNNAMED" mvn spring-boot:run
```

7. Verify Hive/SparkSQL in DataGrip using `jdbc:hive2://localhost:10000/sia21`:

```sql
SHOW TABLES;
SELECT * FROM GAMES_VIEW LIMIT 10;
SELECT * FROM USER_RATINGS_VIEW LIMIT 10;
SELECT * FROM THEMES_CSV_VIEW LIMIT 10;
```

8. Call individual services:

```bash
curl http://localhost:8097/DSA-DOC-CSVService/rest/themes
curl http://localhost:8090/DSA-SQL-JDBCService/rest/ratings/UserRatingSummaryView
curl http://localhost:8091/DSA_SQL_JPAService/rest/games/TopGamesView
curl http://localhost:9990/DSA-SparkSQL-Service/rest/view/GAMES_VIEW
```

9. Start and call the aggregated REST service:

```bash
cd DSA-WEB-RESTService
mvn spring-boot:run
```

```bash
curl http://localhost:8096/DSA-WEB-RESTService/rest/OLAP/GAMES_VIEW
curl http://localhost:8096/DSA-WEB-RESTService/rest/OLAP/TOP_RATED_GAMES_VIEW
curl http://localhost:8096/DSA-WEB-RESTService/rest/OLAP/RATINGS_GAMES_VIEW
```

## 10. Troubleshooting

### Port already in use

Check the service ports:

- `8097`: `DSA-DOC-CSVService`
- `8090`: `DSA-SQL-JDBCService`
- `8091`: `DSA-SQL-JPAService`
- `9990`: `DSA-SparkSQL-Service`
- `8081`: Spark UI
- `10000`: Hive Thrift Server
- `8096`: `DSA-WEB-RESTService`
- `55432`: PostgreSQL mapped port
- `1521`: Oracle mapped port

Use `docker compose ps` inside the relevant compose folder to see container port mappings. Stop the conflicting process or change the service port in `application.properties`.

### DB init scripts did not run

Docker database init scripts run only on first volume initialization. If the container was already created with an existing volume, the init scripts will not run again.

### Docker volume already exists

For normal restart, keep the volume:

```bash
docker compose down
docker compose up -d
```

Optional reset only when you intentionally want to delete DB data and re-run init scripts:

```bash
docker compose down --volumes
docker compose up -d
```

Run the reset command only inside the specific database compose folder you want to reset.

### Service cannot connect to DB

- For `DSA-SQL-JDBCService`, verify PostgreSQL is running and reachable at `jdbc:postgresql://localhost:55432/sia21`.
- For `DSA-SQL-JPAService`, verify Oracle is running and reachable at `jdbc:oracle:thin:@//localhost:1521/FREEPDB1`.
- For `DSA-SparkSQL-Service`, start `DSA-DOC-CSVService`, PostgreSQL, and Oracle first if you want all auto-registered views to succeed.
- For `DSA-WEB-RESTService`, start `DSA-SparkSQL-Service` first because it provides Hive Thrift Server on `localhost:10000`.

### Hive/DataGrip connection issues

- Start `DSA-SparkSQL-Service` and wait until logs say Hive Thrift Server started.
- Use JDBC URL `jdbc:hive2://localhost:10000/sia21`.
- Use user `hive`.
- Leave password empty.
- If Spark startup fails on Java module access, run it with:

```bash
MAVEN_OPTS="--add-opens=java.base/java.net=ALL-UNNAMED --add-opens=java.base/sun.util.calendar=ALL-UNNAMED" mvn spring-boot:run
```

### Downstream service unavailable

`DSA-SparkSQL-Service` reads from downstream services and databases:

- `DSA-DOC-CSVService`: `http://localhost:8097/DSA-DOC-CSVService/rest/themes`
- PostgreSQL: `jdbc:postgresql://localhost:55432/sia21`
- Oracle: `jdbc:oracle:thin:@//localhost:1521/FREEPDB1`

If a downstream source is unavailable during Spark startup, related views may fail to register. Start the missing service/database and restart `DSA-SparkSQL-Service`, or call:

```bash
curl "http://localhost:9990/DSA-SparkSQL-Service/rest/auto?redef=true"
```

Then retry the affected view endpoint.
