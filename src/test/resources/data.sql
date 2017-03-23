SET SCHEMA APP;

INSERT INTO users VALUES (DEFAULT, 'test.user.enabled@home.co.uk', 'pw', TRUE );
INSERT INTO users VALUES (DEFAULT, 'test.user.disabled@home.co.uk', 'pw', FALSE );

INSERT INTO user_details VALUES ('Test', 'User (Enabled)', 'test.user.enabled@home.co.uk', DEFAULT );
INSERT INTO user_details VALUES ('Test', 'User (Disabled)', 'test.user.disabled@home.co.uk', DEFAULT );

INSERT INTO purchases VALUES (DEFAULT, 'test.user.enabled@home.co.uk', DEFAULT , 8);
INSERT INTO purchases VALUES (DEFAULT, 'test.user.enabled@home.co.uk', DEFAULT , -1);
INSERT INTO purchases VALUES (DEFAULT, 'test.user.disabled@home.co.uk', DEFAULT , 2);