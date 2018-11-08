INSERT INTO groups (id, group_name) VALUES (1, 'ADMIN');
INSERT INTO groups (id, group_name) VALUES (2, 'ORGANISER');
INSERT INTO groups (id, group_name) VALUES (3, 'MEMBER');

INSERT INTO group_authorities (group_id, authority) VALUES (1, 'ROLE_ADMIN');
INSERT INTO group_authorities (group_id, authority) VALUES (1, 'ROLE_ORGANISER');
INSERT INTO group_authorities (group_id, authority) VALUES (1, 'ROLE_MEMBER');
INSERT INTO group_authorities (group_id, authority) VALUES (2, 'ROLE_ORGANISER');
INSERT INTO group_authorities (group_id, authority) VALUES (2, 'ROLE_MEMBER');
INSERT INTO group_authorities (group_id, authority) VALUES (3, 'ROLE_MEMBER');