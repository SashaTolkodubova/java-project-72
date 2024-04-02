DROP TABLE IF EXISTS url_check cascade;
DROP TABLE IF EXISTS urls cascade;

CREATE TABLE urls (
id serial PRIMARY KEY NOT NULL,
name varchar(255) UNIQUE NOT NULL,
created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE TABLE url_check (
id serial PRIMARY KEY NOT NULL,
url_id serial REFERENCES urls(id),
status_code INT,
title varchar(255),
h1 varchar(255),
description TEXT,
created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);