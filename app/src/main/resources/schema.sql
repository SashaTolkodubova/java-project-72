DROP TABLE IF EXISTS url_check;
DROP TABLE IF EXISTS urls;

CREATE TABLE urls
(
id          BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
name        VARCHAR(255) NOT NULL,
created_at  TIMESTAMP NOT NULL
);


CREATE TABLE url_check
(
id          BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
url_id      BIGINT REFERENCES urls(id),
status_code INTEGER NOT NULL,
title       VARCHAR(255),
h1          VARCHAR(255),
description TEXT,
created_at  TIMESTAMP NOT NULL
);