CREATE TABLE user_details
(
  balance  DECIMAL DEFAULT 0 NOT NULL,
  users_id INTEGER           NOT NULL
    CONSTRAINT user_details_pkey
    PRIMARY KEY
    CONSTRAINT user_details_users_id_fk
    REFERENCES users
    ON UPDATE CASCADE ON DELETE CASCADE,
    first_name VARCHAR(32),
    last_name  VARCHAR(64)
);

CREATE UNIQUE INDEX user_details_users_id_uindex
  ON user_details (users_id);

ALTER TABLE purchases RENAME TO orders;

ALTER TABLE orders ADD COLUMN unit_cost DECIMAL NOT NULL DEFAULT 1;

-- MOVE first, last name to user_details
INSERT INTO user_details (users_id, first_name, last_name)
  SELECT id, first_name, last_name FROM users;

-- Calculate the total orders placed by each member and set their balance accordingly
UPDATE user_details ud
SET balance = (
  SELECT sum(o.quantity)
  FROM orders o
  WHERE o.users_id = ud.users_id
  GROUP BY (ud.users_id))
WHERE ud.users_id
      IN (SELECT users_id FROM orders);

-- Drop first, last name from users
ALTER TABLE users DROP COLUMN first_name;
ALTER TABLE users DROP COLUMN last_name;

ALTER TABLE events_participants RENAME CONSTRAINT events_participants_events_id_fkey TO events_users_events_id_fkey;
ALTER TABLE events_participants RENAME CONSTRAINT events_participants_users_id_fkey TO events_users_users_id_fkey;
ALTER TABLE events_participants RENAME CONSTRAINT events_participants_event_id_user_id_key TO events_users_event_id_user_id_key;
ALTER TABLE events_participants RENAME TO events_users;

ALTER TABLE event_info RENAME TO event_details;