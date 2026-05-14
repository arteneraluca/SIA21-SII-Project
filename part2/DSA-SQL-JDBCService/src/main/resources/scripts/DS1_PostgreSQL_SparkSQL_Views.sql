--------------------------------------------------------------------------------
--- DS1_PostgreSQL_SparkSQL_Views.sql
--------------------------------------------------------------------------------
--- PostgreSQL source table created by postgresql/user_ratings.sql:
--- user_ratings(bgg_id INTEGER, rating NUMERIC(3, 1), username VARCHAR(100))
--------------------------------------------------------------------------------

-- Base view over the PostgreSQL table.
-- DROP VIEW user_ratings_view;
CREATE OR REPLACE VIEW user_ratings_view AS
SELECT bgg_id,
       rating,
       username
  FROM user_ratings;

SELECT * FROM user_ratings_view;

--------------------------------------------------------------------------------
-- Ratings count and average by board game id.
-- DROP VIEW ratings_by_bgg_id_view;
CREATE OR REPLACE VIEW ratings_by_bgg_id_view AS
SELECT bgg_id,
       COUNT(*) AS rating_count,
       ROUND(AVG(rating), 2) AS average_rating,
       MIN(rating) AS minimum_rating,
       MAX(rating) AS maximum_rating
  FROM user_ratings
 GROUP BY bgg_id;

SELECT *
  FROM ratings_by_bgg_id_view
 ORDER BY rating_count DESC, average_rating DESC, bgg_id;

--------------------------------------------------------------------------------
-- Top rated board game ids with at least 10 ratings.
-- DROP VIEW top_rated_bgg_ids_view;
CREATE OR REPLACE VIEW top_rated_bgg_ids_view AS
SELECT bgg_id,
       COUNT(*) AS rating_count,
       ROUND(AVG(rating), 2) AS average_rating
  FROM user_ratings
 GROUP BY bgg_id
HAVING COUNT(*) >= 10;

SELECT *
  FROM top_rated_bgg_ids_view
 ORDER BY average_rating DESC, rating_count DESC, bgg_id;

--------------------------------------------------------------------------------
-- Ratings count and average by user.
-- DROP VIEW average_rating_by_user_view;
CREATE OR REPLACE VIEW average_rating_by_user_view AS
SELECT username,
       COUNT(*) AS rating_count,
       ROUND(AVG(rating), 2) AS average_rating
  FROM user_ratings
 GROUP BY username;

SELECT *
  FROM average_rating_by_user_view
 ORDER BY rating_count DESC, average_rating DESC, username;

--------------------------------------------------------------------------------
-- Rating distribution.
-- DROP VIEW rating_distribution_view;
CREATE OR REPLACE VIEW rating_distribution_view AS
SELECT rating,
       COUNT(*) AS rating_count
  FROM user_ratings
 GROUP BY rating;

SELECT *
  FROM rating_distribution_view
 ORDER BY rating;
