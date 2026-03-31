CREATE TABLE user_ratings
(
    bgg_id   INTEGER,
    rating   NUMERIC(3, 1),
    username VARCHAR(100)
);

GRANT SELECT ON TABLE user_ratings TO web_anon;
