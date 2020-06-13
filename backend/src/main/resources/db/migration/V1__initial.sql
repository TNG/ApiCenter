CREATE TABLE application
(
    id          CHAR(36)     NOT NULL PRIMARY KEY,
    name        VARCHAR(100) NOT NULL,
    description VARCHAR(250) NOT NULL,
    contact     VARCHAR(100) NOT NULL
);

CREATE TABLE interface
(
    id             CHAR(36)     NOT NULL PRIMARY KEY,
    name           VARCHAR(100) NOT NULL,
    description    VARCHAR(250),
    type           VARCHAR(20)  NOT NULL,
    application_id CHAR(36)     NOT NULL,
    FOREIGN KEY (application_id) REFERENCES application (id)
);
