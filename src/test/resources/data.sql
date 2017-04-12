SET SCHEMA APP;

INSERT INTO users VALUES (0, 'test.user.enabled@home.co.uk', 'Test', 'User (Enabled)', 'pw', TRUE, DEFAULT);
INSERT INTO users VALUES (1, 'test.user.disabled@home.co.uk', 'Test', 'User (Enabled)', 'pw', FALSE, DEFAULT );

INSERT INTO purchases VALUES (DEFAULT, 'test.user.enabled@home.co.uk', DEFAULT , 8);
INSERT INTO purchases VALUES (DEFAULT, 'test.user.enabled@home.co.uk', DEFAULT , -1);
INSERT INTO purchases VALUES (DEFAULT, 'test.user.disabled@home.co.uk', DEFAULT , 2);

INSERT INTO events VALUES (0, TIMESTAMP('2017-07-14 13:25:00'), FALSE);
INSERT INTO events VALUES (1, TIMESTAMP('2017-08-11 14:30:00'), FALSE);
INSERT INTO events VALUES (2, TIMESTAMP('2017-08-31 12:20:00'), TRUE);

INSERT INTO event_participants VALUES (0, 0);
INSERT INTO event_participants VALUES (1, 0);
INSERT INTO event_participants VALUES (1, 1);

INSERT INTO event_info VALUES (0, DEFAULT );
INSERT INTO event_info VALUES (1, DEFAULT );
INSERT INTO event_info VALUES (2, 10.25);