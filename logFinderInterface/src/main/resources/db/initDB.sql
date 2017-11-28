DROP TABLE IF EXISTS groupmembers;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS groups;

CREATE TABLE users
(
  U_NAME                     VARCHAR(50)              NOT NULL,
  U_PASSWORD                 VARCHAR(50)              NOT NULL,
  U_EMAIL                    VARCHAR(50)              NOT NULL,
  registration_time          TIMESTAMP DEFAULT now()  NOT NULL,
  U_DESCRIPTION              VARCHAR(255)             NOT NULL,
  PRIMARY KEY (U_NAME)
);


CREATE TABLE groups
(
  G_NAME             VARCHAR(20)   NOT NULL PRIMARY KEY,
  G_DESCRIPTION      VARCHAR(255)  NOT NULL
);

CREATE TABLE groupmembers
(
  G_NAME             VARCHAR(20)   NOT NULL,
  G_MEMBER           VARCHAR(50)  NOT NULL,
  primary key (G_NAME, G_MEMBER),
  FOREIGN KEY (G_NAME) REFERENCES groups (G_NAME),
  FOREIGN KEY (G_MEMBER) REFERENCES users (U_NAME)

);
