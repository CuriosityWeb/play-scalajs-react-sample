-- Users schema

-- !Ups

CREATE TABLE USERS(
    ID VARCHAR(20) PRIMARY KEY,
    SALT VARCHAR(172),
    HASH VARCHAR(44)
)

-- !Downs

DROP TABLE USERS