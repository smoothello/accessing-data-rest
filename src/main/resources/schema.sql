-- schema.sql
CREATE TABLE person (
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    gender VARCHAR(255),
    address VARCHAR(255),
    PRIMARY KEY (first_name, last_name)
);
