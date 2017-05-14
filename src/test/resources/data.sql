SET SCHEMA APP;

INSERT INTO groups VALUES (0, 'ADMIN');
INSERT INTO groups VALUES (1, 'ORGANISER');
INSERT INTO groups VALUES (2, 'MEMBER');

INSERT INTO group_authorities VALUES (0, 'ROLE_ADMIN');
INSERT INTO group_authorities VALUES (0, 'ROLE_ORGANISER');
INSERT INTO group_authorities VALUES (0, 'ROLE_MEMBER');
INSERT INTO group_authorities VALUES (1, 'ROLE_ORGANISER');
INSERT INTO group_authorities VALUES (1, 'ROLE_MEMBER');
INSERT INTO group_authorities VALUES (2, 'ROLE_MEMBER');

INSERT INTO users VALUES (0, 'test.user.enabled@home.co.uk', 'Test', 'User (Enabled)', 'pw', TRUE, DEFAULT);
INSERT INTO users VALUES (1, 'test.user.disabled@home.co.uk', 'Test', 'User (Disabled)', 'pw', FALSE, DEFAULT );
INSERT INTO users VALUES (2, 'admin.user@home.co.uk', 'Admin', 'User', 'pw', TRUE, DEFAULT );
INSERT INTO users VALUES (3, 'organiser.user@home.co.uk', 'Organiser', 'User', 'pw', TRUE, DEFAULT );

INSERT INTO group_members VALUES (DEFAULT, 'admin.user@home.co.uk', 0);
INSERT INTO group_members VALUES (DEFAULT, 'organiser.user@home.co.uk', 1);
INSERT INTO group_members VALUES (DEFAULT, 'test.user.enabled@home.co.uk', 2);
INSERT INTO group_members VALUES (DEFAULT, 'test.user.disabled@home.co.uk', 2);

INSERT INTO events VALUES (0, TIMESTAMP('2016-12-31 13:25:00'), FALSE);
INSERT INTO events VALUES (1, TIMESTAMP('2017-01-01 14:30:00'), FALSE);
INSERT INTO events VALUES (2, TIMESTAMP('2017-01-02 00:00:00'), TRUE);
INSERT INTO events VALUES (3, TIMESTAMP('2017-01-02 12:20:00'), TRUE);

INSERT INTO events_participants VALUES (0, 0);
INSERT INTO events_participants VALUES (1, 0);
INSERT INTO events_participants VALUES (1, 1);

INSERT INTO event_info VALUES (0, DEFAULT );
INSERT INTO event_info VALUES (1, DEFAULT );
INSERT INTO event_info VALUES (2, 10.25);