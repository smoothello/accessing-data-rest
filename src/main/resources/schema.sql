-- schema.sql
CREATE TABLE person (
    id SERIAL PRIMARY KEY,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    gender VARCHAR(255),
    address VARCHAR(255)
);
