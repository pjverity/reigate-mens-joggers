-- Simply represents some event taking place at a given point in time and whether the event has been completed
-- Todo: No two events can take place at the same point in time. Needs to change
-- Todo: 'completed' should be be removed? Perhaps implicit in that it occurred in the past, incomplete past events should be 'cancelled'?
-- Each event has an associated event_info providing more details specific to the event
CREATE TABLE events
(
  id             SERIAL                       NOT NULL
    CONSTRAINT events_pkey
    PRIMARY KEY,
  event_datetime TIMESTAMP UNIQUE             NOT NULL,
  completed      BOOLEAN DEFAULT FALSE        NOT NULL
);

CREATE UNIQUE INDEX events_event_datetime_uidx
  ON events (event_datetime);


-- Further details about the associated event
CREATE TABLE event_info
(
  events_id INTEGER UNIQUE NOT NULL
    CONSTRAINT events_id_fkey
    REFERENCES events
    ON DELETE CASCADE,
  distance  DOUBLE PRECISION
);

CREATE UNIQUE INDEX event_info_events_id_uidx
  ON event_info (events_id);


-- Join table for events and users
CREATE TABLE events_participants
(
  events_id INTEGER NOT NULL
    CONSTRAINT events_participants_events_id_fkey
    REFERENCES events,
  users_id  INTEGER NOT NULL
    CONSTRAINT events_participants_users_id_fkey
    REFERENCES users
    ON DELETE CASCADE,
  CONSTRAINT events_participants_event_id_user_id_key
  UNIQUE (events_id, users_id)
);

CREATE UNIQUE INDEX events_participants_events_id_users_id_uidx
  ON events_participants (events_id, users_id);


-- Records purchases in terms of quantities of 'tokens'
CREATE TABLE purchases
(
  id            SERIAL                                              NOT NULL
    CONSTRAINT purchases_pkey
    PRIMARY KEY,
  purchase_time TIMESTAMP DEFAULT timezone('UTC', now())            NOT NULL,
  quantity      INTEGER                                             NOT NULL,
  users_id      INTEGER                                             NOT NULL
    CONSTRAINT purchases_users_id_fkey
    REFERENCES users
    ON DELETE CASCADE
);

CREATE UNIQUE INDEX purchases_users_id_purchase_time_uidx
  ON purchases (users_id, purchase_time);

CREATE INDEX purchases_users_id_idx
  ON purchases (users_id);
