SET SCHEMA APP;

INSERT INTO users VALUES (DEFAULT, 'test.user.enabled@home.co.uk', 'Test', 'User (Enabled)', 'pw', TRUE, DEFAULT );
INSERT INTO users VALUES (DEFAULT, 'test.user.disabled@home.co.uk', 'Test', 'User (Enabled)', 'pw', FALSE, DEFAULT );

INSERT INTO purchases VALUES (DEFAULT, 'test.user.enabled@home.co.uk', DEFAULT , 8);
INSERT INTO purchases VALUES (DEFAULT, 'test.user.enabled@home.co.uk', DEFAULT , -1);
INSERT INTO purchases VALUES (DEFAULT, 'test.user.disabled@home.co.uk', DEFAULT , 2);

INSERT INTO events VALUES (DEFAULT, TIMESTAMP('2017-08-31 12:20:00'), FALSE);
INSERT INTO events VALUES (DEFAULT, TIMESTAMP('2017-07-14 13:25:00'), FALSE );
INSERT INTO events VALUES (DEFAULT, TIMESTAMP('2017-08-11 14:30:00'), TRUE );