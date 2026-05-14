FROM gvenzl/oracle-free:slim-faststart

USER root

COPY src/main/resources/scripts/games.sql /container-entrypoint-initdb.d/01_games.sql

RUN chown oracle:oinstall /container-entrypoint-initdb.d/01_games.sql

USER oracle
