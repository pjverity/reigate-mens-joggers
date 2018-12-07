CREATE UNIQUE INDEX users_id_uidx
  ON users (id);

CREATE UNIQUE INDEX groups_id_uidx
  ON groups (id);

CREATE UNIQUE INDEX groups_group_name_uidx
  ON groups (group_name);

CREATE UNIQUE INDEX group_members_id_uidx
  ON group_members (id);

ALTER TABLE group_members
  RENAME CONSTRAINT group_members_pk TO group_members_pkey;

ALTER TABLE group_members
  RENAME CONSTRAINT group_members_users_username_fk TO group_members_users_username_fkey;

ALTER TABLE group_members
  RENAME CONSTRAINT group_members_groups_id_fk TO group_members_groups_id_fkey;

ALTER TABLE group_members
  ADD UNIQUE (username, group_id);

CREATE UNIQUE INDEX group_members_username_group_id_uidx
  ON group_members (username, group_id);

ALTER TABLE group_authorities
  RENAME CONSTRAINT group_authorities_groups_id_fk TO group_authorities_groups_id_fkey;

ALTER TABLE group_authorities
  ADD UNIQUE (group_id, authority);

CREATE UNIQUE INDEX group_authorities_group_id_authority_uidx
  ON group_authorities (group_id, authority);