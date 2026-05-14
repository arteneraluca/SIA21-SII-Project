# IIS

## SQL REST Service: JPA-DATA-SOURCE-SERVICE-WRAPPER

### JDBC Spring Boot Service
* Jakarta Persistence - Provider: Hibernate from [spring-boot-starter-data-jpa]
* SpringBoot 3

### SpringBoot service desc
* SBT jar: DSA-SQL-JPAService-2026.1.jar

## Spring Boot Container Doc
* [Docker for SBT](https://spring.io/guides/gs/spring-boot-docker)
  
## DevOp Local Flow
* Java/JPA service runs locally with Maven or the IDE.
* Docker is used only for Oracle DB.

```bash
docker compose up -d oracle-db
mvn spring-boot:run
```

Oracle connection:
* Host: localhost
* Port: 1521
* Service: FREEPDB1
* User: games
* Password: games

REST endpoints:
* http://localhost:8091/DSA_SQL_JPAService/rest/games/GamesView
* http://localhost:8091/DSA_SQL_JPAService/rest/games/TopGamesView
* http://localhost:8091/DSA_SQL_JPAService/rest/games/TwoPlayerGamesView
* http://localhost:8091/DSA_SQL_JPAService/rest/games/GameRatingStatsView

## Clean-up docker
cd DSA-SQL-JPAService
docker compose down
