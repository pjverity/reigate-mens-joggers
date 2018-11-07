CREATE TABLE site_settings
(
  id    SERIAL                          NOT NULL
    CONSTRAINT site_settings_pkey
    PRIMARY KEY,
  name  VARCHAR(255) UNIQUE             NOT NULL,
  value VARCHAR(255)                    NULL
);

INSERT INTO site_settings (id, name, value) VALUES (DEFAULT, 'FLICKR_GROUP_NAME', 'RMJ Test Group');
INSERT INTO site_settings (id, name, value) VALUES (DEFAULT, 'FLICKR_GROUP_NSID', '3810360@N20');
