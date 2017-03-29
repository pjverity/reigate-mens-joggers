SET SCHEMA APP;

INSERT INTO users VALUES (DEFAULT, 'test.user.enabled@home.co.uk', 'Test', 'User (Enabled)', 'pw', TRUE, DEFAULT );
INSERT INTO users VALUES (DEFAULT, 'test.user.disabled@home.co.uk', 'Test', 'User (Enabled)', 'pw', FALSE, DEFAULT );

INSERT INTO purchases VALUES (DEFAULT, 'test.user.enabled@home.co.uk', DEFAULT , 8);
INSERT INTO purchases VALUES (DEFAULT, 'test.user.enabled@home.co.uk', DEFAULT , -1);
INSERT INTO purchases VALUES (DEFAULT, 'test.user.disabled@home.co.uk', DEFAULT , 2);