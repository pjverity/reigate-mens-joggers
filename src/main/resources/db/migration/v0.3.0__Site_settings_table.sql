CREATE TABLE site_settings
(
  id    SERIAL                          NOT NULL
    CONSTRAINT site_settings_pkey
    PRIMARY KEY,
  name  VARCHAR(255) UNIQUE             NOT NULL,
  value VARCHAR(255)                    NULL
);
