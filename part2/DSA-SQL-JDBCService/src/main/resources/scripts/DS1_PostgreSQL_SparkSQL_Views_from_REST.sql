--------------------------------------------------------------------------------
--- DS1_PostgreSQL_SparkSQL_Views_from_REST.sql
--- REST views expose user_ratings(bgg_id, rating, username)
--------------------------------------------------------------------------------
SELECT java_method(
               'org.spark.service.rest.QueryRESTDataService',
               'getRESTDataDocument',
               'http://localhost:8090/DSA-SQL-JDBCService/rest/ratings/UserRatingView');

SELECT java_method(
               'org.spark.service.rest.QueryRESTDataService',
               'getRESTDataDocument',
               'http://localhost:8090/DSA-SQL-JDBCService/rest/ratings/UserRatingSummaryView');

SELECT java_method(
               'org.spark.service.rest.QueryRESTDataService',
               'getRESTDataDocument',
               'http://localhost:8090/DSA-SQL-JDBCService/rest/ratings/UserAverageRatingView');
--------------------------------------------------------------------------------

--------------------------------------------------------------------------------
--- JDBC Data Source Access Model ----------------------------------------------
SELECT java_method(
               'org.spark.service.rest.RESTEnabledSQLService',
               'createJSONViewFromREST',
               'USER_RATINGS_JSON_VIEW',
               'http://localhost:8090/DSA-SQL-JDBCService/rest/ratings/UserRatingView');

SELECT * FROM USER_RATINGS_JSON_VIEW;
---
-- DROP VIEW user_ratings_rest_view;
CREATE OR REPLACE VIEW user_ratings_rest_view AS
SELECT v.bggId AS bgg_id,
       v.rating AS rating,
       v.username AS username
FROM USER_RATINGS_JSON_VIEW as json_view LATERAL VIEW explode(json_view.array) AS v;

SELECT * FROM user_ratings_rest_view;
--------------------------------------------------------------------------------
SELECT java_method(
               'org.spark.service.rest.RESTEnabledSQLService',
               'createJSONViewFromREST',
               'USER_RATING_SUMMARY_JSON_VIEW',
               'http://localhost:8090/DSA-SQL-JDBCService/rest/ratings/UserRatingSummaryView');

SELECT * FROM USER_RATING_SUMMARY_JSON_VIEW;
---
-- DROP VIEW user_rating_summary_rest_view;
CREATE OR REPLACE VIEW user_rating_summary_rest_view AS
SELECT v.bggId AS bgg_id,
       v.ratingCount AS rating_count,
       v.averageRating AS average_rating,
       v.minimumRating AS minimum_rating,
       v.maximumRating AS maximum_rating
FROM USER_RATING_SUMMARY_JSON_VIEW as json_view LATERAL VIEW explode(json_view.array) AS v;

SELECT * FROM user_rating_summary_rest_view;
--------------------------------------------------------------------------------
SELECT java_method(
               'org.spark.service.rest.RESTEnabledSQLService',
               'createJSONViewFromREST',
               'USER_AVERAGE_RATING_JSON_VIEW',
               'http://localhost:8090/DSA-SQL-JDBCService/rest/ratings/UserAverageRatingView');

SELECT * FROM USER_AVERAGE_RATING_JSON_VIEW;
---
-- DROP VIEW average_rating_by_user_rest_view;
CREATE OR REPLACE VIEW average_rating_by_user_rest_view AS
SELECT v.username AS username,
       v.ratingCount AS rating_count,
       v.averageRating AS average_rating
FROM USER_AVERAGE_RATING_JSON_VIEW as json_view LATERAL VIEW explode(json_view.array) AS v;

SELECT * FROM average_rating_by_user_rest_view;
--------------------------------------------------------------------------------

-- With AUTHENTICATION
SELECT java_method(
               'org.spark.service.rest.RESTEnabledSQLService',
               'createJSONViewFromREST',
               'USER_RATINGS_JSON_VIEW',
               'http://developer:iis@localhost:8090/DSA-SQL-JDBCService/rest/ratings/UserRatingView');

SELECT * FROM USER_RATINGS_JSON_VIEW;
---
-- DROP VIEW user_ratings_rest_view;
CREATE OR REPLACE VIEW user_ratings_rest_view AS
SELECT v.bggId AS bgg_id,
       v.rating AS rating,
       v.username AS username
FROM USER_RATINGS_JSON_VIEW as json_view LATERAL VIEW explode(json_view.array) AS v;

SELECT * FROM user_ratings_rest_view;
