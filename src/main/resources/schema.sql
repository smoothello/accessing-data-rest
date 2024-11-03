CREATE TABLE person (
    first_name VARCHAR(50) NOT NULL,
    middle_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    gender VARCHAR(10),
    address VARCHAR(255),
    PRIMARY KEY (first_name, middle_name, last_name)
);
