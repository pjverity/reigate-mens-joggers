CREATE TABLE users
(
  id         SERIAL                                   NOT NULL
    CONSTRAINT users_pkey
    PRIMARY KEY,
  username   VARCHAR(128)                             NOT NULL,
  password   CHAR(60)                                 NOT NULL,
  enabled    BOOLEAN DEFAULT TRUE                     NOT NULL,
  first_name VARCHAR(32),
  last_name  VARCHAR(64),
  last_login TIMESTAMP DEFAULT timezone('UTC', now()) NOT NULL
);

CREATE UNIQUE INDEX users_username_uidx
  ON users (username);

CREATE TABLE groups
(
  id         SERIAL      NOT NULL
    CONSTRAINT groups_pkey
    PRIMARY KEY,
  group_name VARCHAR(50) NOT NULL
    CONSTRAINT groups_group_name_key
    UNIQUE
);

CREATE TABLE group_members
(
  id       SERIAL NOT NULL
    CONSTRAINT group_members_pk
    PRIMARY KEY,
  username VARCHAR(128)
    CONSTRAINT group_members_users_username_fk
    REFERENCES users (username)
    ON DELETE CASCADE,
  group_id INTEGER
    CONSTRAINT group_members_groups_id_fk
    REFERENCES groups
);

CREATE TABLE group_authorities
(
  group_id  INTEGER     NOT NULL
    CONSTRAINT group_authorities_groups_id_fk
    REFERENCES groups,
  authority VARCHAR(50) NOT NULL
);

INSERT INTO groups (id, group_name) VALUES (DEFAULT, 'ADMIN');
INSERT INTO groups (id, group_name) VALUES (DEFAULT, 'ORGANISER');
INSERT INTO groups (id, group_name) VALUES (DEFAULT, 'MEMBER');

INSERT INTO group_authorities (group_id, authority) VALUES (1, 'ROLE_ADMIN');
INSERT INTO group_authorities (group_id, authority) VALUES (1, 'ROLE_ORGANISER');
INSERT INTO group_authorities (group_id, authority) VALUES (1, 'ROLE_MEMBER');
INSERT INTO group_authorities (group_id, authority) VALUES (2, 'ROLE_ORGANISER');
INSERT INTO group_authorities (group_id, authority) VALUES (2, 'ROLE_MEMBER');
INSERT INTO group_authorities (group_id, authority) VALUES (3, 'ROLE_MEMBER');