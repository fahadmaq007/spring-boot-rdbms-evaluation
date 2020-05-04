DROP TABLE IF EXISTS movie;
CREATE TABLE movie (
    id bigint auto_increment,
    genres VARCHAR(255),
    title VARCHAR(255) NOT NULL
);

DROP TABLE IF EXISTS rating;
CREATE TABLE rating (
    id bigint auto_increment,
    movie_id INT,
    rated_on DATE,
    rating DECIMAL,
    user_id INT
);

--CREATE INDEX rating_movie_id ON rating USING btree (movie_id);

--CREATE INDEX rating_user_id ON rating USING btree (user_id);