INSERT INTO groups VALUES (0, 'ADMIN');
INSERT INTO groups VALUES (1, 'ORGANISER');
INSERT INTO groups VALUES (2, 'MEMBER');

INSERT INTO group_authorities VALUES (0, 'ROLE_ADMIN');
INSERT INTO group_authorities VALUES (0, 'ROLE_ORGANISER');
INSERT INTO group_authorities VALUES (0, 'ROLE_MEMBER');
INSERT INTO group_authorities VALUES (1, 'ROLE_ORGANISER');
INSERT INTO group_authorities VALUES (1, 'ROLE_MEMBER');
INSERT INTO group_authorities VALUES (2, 'ROLE_MEMBER');

INSERT INTO users VALUES (0, 'test.user.enabled@home.co.uk', 'pw', TRUE, DEFAULT);
INSERT INTO users VALUES (1, 'test.user.disabled@home.co.uk', 'pw', FALSE, DEFAULT );
INSERT INTO users VALUES (2, 'admin.user@home.co.uk', 'pw', TRUE, DEFAULT );
INSERT INTO users VALUES (3, 'organiser.user@home.co.uk', 'pw', TRUE, DEFAULT );

INSERT INTO user_details VALUES (0, 0, 'Test', 'User (Enabled)');
INSERT INTO user_details VALUES (1, 0, 'Test', 'User (Disabled)');
INSERT INTO user_details VALUES (2, 0, 'Admin', 'User' );
INSERT INTO user_details VALUES (3, 0, 'Organiser', 'User' );

INSERT INTO group_members VALUES (DEFAULT, 'admin.user@home.co.uk', 0);
INSERT INTO group_members VALUES (DEFAULT, 'organiser.user@home.co.uk', 1);
INSERT INTO group_members VALUES (DEFAULT, 'test.user.enabled@home.co.uk', 2);
INSERT INTO group_members VALUES (DEFAULT, 'test.user.disabled@home.co.uk', 2);
